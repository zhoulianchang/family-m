package com.zlc.family.common.third.oss.impl;

import cn.hutool.core.io.FileUtil;
import com.zlc.family.common.config.OssProperties;
import com.zlc.family.common.third.oss.OssClient;
import com.zlc.family.common.third.oss.OssCode;
import com.zlc.family.common.third.oss.OssResult;
import com.zlc.family.common.utils.spring.SpringUtils;
import io.minio.*;
import io.minio.messages.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zlc
 * @date 2024/5/13 20:50
 */
@Slf4j
public class MinioC implements OssClient {
    private static final String MINIO_ALL_POLICY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::%s\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}";
    private MinioClient minioClient = SpringUtils.getBean("minioClient");
    private final OssProperties.Minio config = OssProperties.getMinio();

    @Getter
    @Setter
    public static class FileDto {
        private InputStream inputStream;
        private String fileName;
        private String bucketName;
        private Boolean limitFileSize;

        @Builder
        public FileDto(InputStream inputStream, String fileName, String bucketName, Boolean limitFileSize) {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.bucketName = bucketName;
            this.limitFileSize = limitFileSize;
        }
    }

    @Override
    public OssResult uploadFile(File file, String fileName) {
        String[] bucketAndOther = splitBucketAndOther(fileName);
        return uploadFile(file, bucketAndOther[1], bucketAndOther[0], true);
    }

    @Override
    public OssResult uploadFile(InputStream is, String fileName) {
        String[] bucketAndOther = splitBucketAndOther(fileName);
        return uploadFile(is, bucketAndOther[1], bucketAndOther[0], true);
    }

    @Override
    public OssResult uploadFile(File file, String fileName, String bucketName, Boolean limitFileSize) {
        BufferedInputStream is = FileUtil.getInputStream(file);
        return realUpload(FileDto.builder()
                .inputStream(is)
                .fileName(fileName)
                .bucketName(bucketName)
                .limitFileSize(limitFileSize)
                .build());
    }

    @Override
    public OssResult uploadFile(InputStream is, String fileName, String bucketName, Boolean limitFileSize) {
        return realUpload(FileDto.builder()
                .inputStream(is)
                .fileName(fileName)
                .bucketName(bucketName)
                .limitFileSize(limitFileSize)
                .build());
    }

    @Override
    public boolean downloadFile(String fileName, String localFileName) {
        return false;
    }

    @Override
    public boolean deleteFile(String fileName) {
        AtomicBoolean result = new AtomicBoolean(true);
        Optional.ofNullable(splitBucketAndOther(fileName)).ifPresent(nameArr -> {
            try {
                minioClient.removeObject(RemoveObjectArgs.builder().bucket(nameArr[0]).object(nameArr[1]).build());
            } catch (Exception e) {
                log.error("minio removeObject error,reason is:{}", e.getMessage(), e);
                result.set(false);
            }
        });
        return result.get();
    }

    @Override
    public List<String> listObjects(String prefix) {
        List<String> result = new ArrayList<>();
        Optional.ofNullable(splitBucketAndOther(prefix)).ifPresent(nameArr -> {
            Iterable<Result<Item>> listObjects = minioClient.listObjects(ListObjectsArgs.builder().bucket(nameArr[0]).prefix(nameArr[1]).recursive(true).build());
            for (Result<Item> object : listObjects) {
                try {
                    String name = object.get().objectName();
                    result.add(getFilePath(nameArr[0], name));
                } catch (Exception e) {
                    log.error("minio listObjects error,reason is:{}", e.getMessage(), e);
                }
            }
        });
        return result;
    }

    /**
     * 实际调用MinIO SDK上传文件的核心方法
     *
     * @param fileDto
     * @return
     */
    private OssResult realUpload(FileDto fileDto) {
        if (!bucketCreate(fileDto.getBucketName())) {
            return OssResult.fail(OssCode.BUCKET_ERROR);
        }
        try (InputStream inputStream = fileDto.getInputStream()) {
            int available = inputStream.available();
            long objectSize = available <= 0 ? -1L : available;
            if (fileDto.getLimitFileSize() && objectSize != -1L) {
                // 如果开启最大文件大小限制
                // 上传文件最大值 MB->bytes
                long maxSize = config.getFileMaxSize() * 1024 * 1024;
                if (objectSize > maxSize) {
                    return OssResult.fail(OssCode.EXCEED_MAX_SIZE);
                }
            }
            // 如果文件大小已知 则partSize传-1 如果文件大小未知则partSize传10485760L
            long partSize = objectSize == -1L ? 10485760L : -1L;
            String contentType = MediaTypeFactory.getMediaType(fileDto.getFileName()).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
            log.info("upload file. bucket:{}, object:{},size:{},contentType:{}", fileDto.getBucketName(), fileDto.getFileName(), objectSize, contentType);
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder()
                    .bucket(fileDto.getBucketName())
                    .object(fileDto.getFileName())
                    .stream(inputStream, objectSize, partSize)
                    .contentType(contentType)
                    .build());
            return OssResult.ok();
        } catch (Exception e) {
            log.error("minio putObject error,reason is:{}", e.getMessage(), e);
            return OssResult.fail(OssCode.MINIO_ERROR.getCode(), e.getMessage());
        }
    }

    /**
     * bucket是否存在
     *
     * @param bucketName
     * @return
     */
    private boolean bucketExists(String bucketName) {
        try {
            boolean result = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!result) {
                log.warn("The bucket[{}] not exist", bucketName);
            }
            return result;
        } catch (Exception e) {
            log.error("minio bucketExists error,reason is:{}", e.getMessage());
            return false;
        }
    }

    /**
     * 创建bucket，如果已经存在则不创建
     *
     * @param bucketName
     * @return
     */
    private boolean bucketCreate(String bucketName) {
        if (bucketExists(bucketName)) {
            return true;
        }
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(getAllPolicyConfig(bucketName)).build());
            log.info("The bucket[{}] been create", bucketName);
        } catch (Exception e) {
            log.error("minio makeBucket error,reason is:{}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取Bucket名称和其后面的部分字符串
     *
     * @param filePath 文件路径
     * @return 数组 第一个是Bucket名称 第二个是对象名称
     */
    private String[] splitBucketAndOther(String filePath) {
        int firstIndex = filePath.indexOf("/");
        int secondIndex = filePath.indexOf("/", firstIndex + 1);
        if (firstIndex == -1 || secondIndex == -1) {
            log.error("filePath illegality,must contain bucket name");
            return null;
        }
        return new String[]{filePath.substring(firstIndex + 1, secondIndex), filePath.substring(secondIndex + 1)};
    }

    private String getAllPolicyConfig(String bucketName) {
        return String.format(MINIO_ALL_POLICY, bucketName, bucketName);
    }

    private String getFilePath(String bucketName, String objectName) {
        return "/" + bucketName + "/" + objectName;
    }
}
