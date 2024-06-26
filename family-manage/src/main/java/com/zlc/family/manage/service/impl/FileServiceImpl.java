package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.common.annotation.DataScope;
import com.zlc.family.common.config.OssProperties;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.enums.manage.FileExtPlace;
import com.zlc.family.common.enums.manage.FileType;
import com.zlc.family.common.exception.family.FamilyException;
import com.zlc.family.common.exception.file.InvalidExtensionException;
import com.zlc.family.common.third.oss.OssClientFactory;
import com.zlc.family.common.third.oss.OssResult;
import com.zlc.family.common.third.oss.OssType;
import com.zlc.family.common.utils.AssertUtils;
import com.zlc.family.common.utils.file.FileUploadUtils;
import com.zlc.family.common.utils.file.MimeTypeUtils;
import com.zlc.family.common.utils.uuid.IdUtils;
import com.zlc.family.manage.domain.FamilyFile;
import com.zlc.family.manage.domain.FileExt;
import com.zlc.family.manage.domain.query.FileQuery;
import com.zlc.family.manage.domain.vo.FileVo;
import com.zlc.family.manage.mapper.FileExtMapper;
import com.zlc.family.manage.mapper.FileMapper;
import com.zlc.family.manage.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 文件属性表 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2024-05-15
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, FamilyFile> implements IFileService {
    private final FileExtMapper fileExtMapper;

    @Override
    @DataScope(deptAlias = "f")
    public List<FileVo> listFile(FileQuery query) {
        List<FileVo> fileVoList = this.getBaseMapper().listFile(query);
        fileVoList.stream().filter(f -> FileType.FILE.equals(f.getType()) && f.getFileExt() != null)
                .forEach(fileVo -> {
                    String filePath = fileVo.getFileExt().getFilePath();
                    switch (fileVo.getFileExt().getPlace()) {
                        case MINIO:
                            fileVo.getFileExt().setFilePath(OssProperties.getMinio().getForeignEndpoint() + filePath);
                            break;
                        default:
                            // 如果有其他类型，可以在这里处理
                            break;
                    }
                });
        return fileVoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(FamilyFile familyFile, MultipartFile realFile) throws InvalidExtensionException {
        FamilyFile parentFile = null;
        FileExt fileExt = null;
        familyFile.setParentId(Optional.ofNullable(familyFile.getParentId()).orElse(0L));
        if (familyFile.getParentId() != 0L) {
            parentFile = getById(familyFile.getParentId());
            AssertUtils.isNull(parentFile, FamilyException.Code.FILE_NOT_EXISTS, familyFile.getParentId());
            familyFile.setAncestors(parentFile.getAncestors() + "," + familyFile.getParentId());
        }
        if (FileType.FILE.equals(familyFile.getType())) {
            AssertUtils.isNull(realFile, FamilyException.Code.FILE_REAL_NOT_UPLOAD);
            fileExt = new FileExt();
            fileExt.setPlace(Optional.ofNullable(fileExt.getPlace()).orElse(FileExtPlace.MINIO));
            fileExt.setFileSize(new BigDecimal(realFile.getSize()).divide(new BigDecimal(1024 * 1024)).floatValue());
            fileExt.setFileType(FileUploadUtils.getExtension(realFile));
            StringBuilder sb = new StringBuilder();
            if (FileExtPlace.MINIO.equals(fileExt.getPlace())) {
                sb.append("/").append(OssProperties.getMinio().getDefaultBucket());
            }
            if (familyFile.getParentId() != 0L) {
                // 找到当前文件的所有上级目录将其拼接后再带上文件名称
                List<FamilyFile> ancestorsFileList = listByIds(Arrays.asList(familyFile.getAncestors().split(",")));
                // 如果是文件的话就校验是否挂在目录下
                AssertUtils.isTrue(FileType.FILE.equals(parentFile.getType()), FamilyException.Code.FILE_ONLY_HANG_DIR);
                for (FamilyFile ancestorsFile : ancestorsFileList) {
                    sb.append("/").append(ancestorsFile.getName());
                }
            }
            fileExt.setFilePath(sb.append("/").append(IdUtils.fastSimpleUUID()).append(".").append(fileExt.getFileType()).toString());
            if (!FileUploadUtils.isAllowedExtension(fileExt.getFileType(), MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION)) {
                // 当前文件格式不支持
                throw new InvalidExtensionException(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, fileExt.getFileType(), realFile.getOriginalFilename());
            }
        }
        long count = count(new QueryWrapper<FamilyFile>().lambda().eq(FamilyFile::getDelFlag, FamilyConstants.DEL_NO).eq(FamilyFile::getName, familyFile.getName()).eq(FamilyFile::getParentId, familyFile.getParentId()).eq(familyFile.getDeptId() != null, FamilyFile::getDeptId, familyFile.getDeptId()));
        AssertUtils.isTrue(count > 0, FamilyException.Code.FILE_NAME_REPEAT, new Object[]{familyFile.getParentId() + "-" + familyFile.getName()});
        // 进行数据库操作
        save(familyFile);
        Optional.ofNullable(fileExt).ifPresent(ext -> {
            ext.setFileId(familyFile.getFileId());
            fileExtMapper.insert(ext);
            // 将文件真正上传至OSS上
            try {
                OssResult ossResult = OssClientFactory.create(OssType.MINIO).uploadFile(realFile.getInputStream(), ext.getFilePath());
                AssertUtils.isTrue(ossResult.getCode() != 0, FamilyException.Code.FILE_UPLOAD_ERROR, ossResult.getMsg());
            } catch (IOException e) {
                throw new FamilyException(FamilyException.Code.FILE_UPLOAD_ERROR);
            }
        });
        return true;
    }

    @Override
    public boolean updateFile(FamilyFile newFile) {
        AssertUtils.isNull(newFile.getFileId(), FamilyException.Code.ID_NULL);
        FamilyFile oldFile = getById(newFile.getFileId());
        AssertUtils.isTrue(FileType.FILE.equals(oldFile.getType()), FamilyException.Code.FILE_UPDATE_NOT_SUPPORT_TYPE);
        updateById(newFile);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFile(Long[] ids) {
        // 删除前先校验有无子节点
        long count = count(new QueryWrapper<FamilyFile>().lambda().eq(FamilyFile::getDelFlag, FamilyConstants.DEL_NO).in(FamilyFile::getParentId, ids));
        AssertUtils.isTrue(count > 0, FamilyException.Code.FILE_REMOVE_HAVE_CHILDREN);
        // 更新所有文件的删除标识
        update(new UpdateWrapper<FamilyFile>().lambda().set(FamilyFile::getDelFlag, FamilyConstants.DEL_YES).in(FamilyFile::getFileId, ids));
        // 删除文件存储中的文件
        List<FileExt> fileExtList = fileExtMapper.selectBatchIds(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(fileExtList)) {
            return true;
        }
        for (FileExt fileExt : fileExtList) {
            boolean result = OssClientFactory.create(OssType.MINIO).deleteFile(fileExt.getFilePath());
            AssertUtils.isFalse(result, FamilyException.Code.FILE_REMOVE_ERROR);
        }
        return true;
    }
}
