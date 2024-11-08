package com.zlc.family.common.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author zlc
 * @date 2024/6/25 16:24
 */
@Configuration
public class OkHttpClientConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(Optional.ofNullable(HttpProperties.getReadTimeout()).orElse(30), TimeUnit.SECONDS)
                .connectTimeout(Optional.ofNullable(HttpProperties.getConnectTimeout()).orElse(5), TimeUnit.SECONDS)
                .build();
    }
}
