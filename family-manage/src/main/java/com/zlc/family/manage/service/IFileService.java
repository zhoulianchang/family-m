package com.zlc.family.manage.service;

import com.zlc.family.common.exception.file.InvalidExtensionException;
import com.zlc.family.manage.domain.FamilyFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.family.manage.domain.query.FileQuery;
import com.zlc.family.manage.domain.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文件属性表 服务类
 * </p>
 *
 * @author zlc
 * @since 2024-05-15
 */
public interface IFileService extends IService<FamilyFile> {

    List<FileVo> listFile(FileQuery query);

    boolean add(FamilyFile file, MultipartFile realFile) throws InvalidExtensionException;

    boolean removeFile(Long[] ids);

    boolean updateFile(FamilyFile file);
}
