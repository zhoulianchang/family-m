package com.zlc.family.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zlc
 * @date 2024/5/13 21:07
 */
@Component
@ConfigurationProperties(prefix = "oss")
public class OssProperties {
    private static Minio minio;

    public static Minio getMinio() {
        return OssProperties.minio;
    }

    public void setMinio(Minio minio) {
        OssProperties.minio = minio;
    }

    @Getter
    @Setter
    public static class Minio {
        /**
         * 是否启用
         */
        private Boolean enable;
        /**
         * 认证密钥
         */
        private String accessKey;
        private String secretKey;
        /**
         * 内网地址
         */
        private String endpoint;
        /**
         * 外网地址
         */
        private String foreignEndpoint;
        /**
         * 默认的桶名
         */
        private String defaultBucket;
        /**
         * 上传文件最大体积，单位MB
         */
        private Integer fileMaxSize;
        /**
         * 临时生成的url 过期时间 单位s
         */
        private Integer policyExpire;
    }
}
