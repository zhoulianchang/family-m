package com.zlc.family.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zlc
 * @date 2024/6/25 16:24
 */
@Component
@ConfigurationProperties(prefix = "http-properties")
public class HttpProperties {
    /**
     * 接口请求超时时间
     */
    private static Integer readTimeout;
    /**
     * tcp 连接超时时间
     */
    private static Integer connectTimeout;

    public static Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        HttpProperties.readTimeout = readTimeout;
    }

    public static Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        HttpProperties.connectTimeout = connectTimeout;
    }
}
