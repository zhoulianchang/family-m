package com.zlc.family.framework.config;

import com.zlc.family.common.config.OssProperties;
import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author zlc
 * @date 2024/5/13 21:05
 */
@Configuration
public class OssConfig {

    @Bean
    public MinioClient minioClient() {
        OssProperties.Minio minio = OssProperties.getMinio();
        if (minio.getEnable()) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(5, TimeUnit.SECONDS) //连接超时
                    .readTimeout(15, TimeUnit.SECONDS) //读取超时
                    .writeTimeout(30, TimeUnit.SECONDS) //写超时
                    .build();
            return MinioClient.builder()
                    .endpoint(minio.getEndpoint())
                    .credentials(minio.getAccessKey(), minio.getSecretKey())
                    .httpClient(httpClient)
                    .build();
        }
        return null;
    }
}
