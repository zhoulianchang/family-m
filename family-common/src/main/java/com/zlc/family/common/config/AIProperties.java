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
@ConfigurationProperties(prefix = "ai")
public class AIProperties {
    private static GPT GPT;

    public static GPT getGPT() {
        return AIProperties.GPT;
    }

    public void setGPT(GPT GPT) {
        AIProperties.GPT = GPT;
    }

    @Getter
    @Setter
    public static class GPT {
        private String url;
        private String accessKey;
    }
}
