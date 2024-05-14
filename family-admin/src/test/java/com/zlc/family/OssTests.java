package com.zlc.family;

import com.zlc.family.common.config.OssProperties;
import com.zlc.family.common.third.oss.OssClient;
import com.zlc.family.common.third.oss.OssClientFactory;
import com.zlc.family.common.third.oss.OssResult;
import com.zlc.family.common.third.oss.OssType;
import com.zlc.family.common.utils.spring.SpringUtils;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * @author zlc
 * @date 2024/5/15 01:00
 */
@SpringBootTest
@Slf4j
public class OssTests {
    public static OssClient ossClient;
    public static String bucketName;

    @BeforeEach
    void beforeAll() {
        log.info("your minio accessKey is:{}", OssProperties.getMinio().getAccessKey());
        log.info("your minio secretKey is:{}", OssProperties.getMinio().getSecretKey());
        ossClient = OssClientFactory.create(OssType.MINIO);
        bucketName = "family";
    }

    @SneakyThrows
    @Test
    void uploadFile() {
        File file = new File("/Users/zlc/Downloads/chou.mp4");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String module = "zyh";
        OssResult ossResult = ossClient.uploadFile(file, module + "/" + year + "/" + month + "/" + day + "/" + file.getName(), bucketName, true);
        log.info(ossResult.toString());
    }

    @Test
    void delFile() {
        boolean result = ossClient.deleteFile("/test/1.mp4");
        log.info("result is:{}", result);
    }

    @Test
    void listUrl() {
        List<String> urlList = ossClient.listObjects("/" + bucketName + "/");
        for (String url : urlList) {
            log.info(url);
        }
    }

    @SneakyThrows
    @Test
    void removeBucket() {
        String bucketName = "test";
        List<String> urlList = ossClient.listObjects("/" + bucketName + "/");
        for (String url : urlList) {
            boolean result = ossClient.deleteFile(url);
            log.info("result is:{}", result);
        }
        MinioClient minioClient = SpringUtils.getBean("minioClient");
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }
}
