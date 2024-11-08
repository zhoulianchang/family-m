package com.zlc.family.framework.third.ai.gpt;

import com.zlc.family.common.config.AIProperties;
import com.zlc.family.framework.utils.OkHttpUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zlc
 * 2024/11/8
 */
public class GPTUtils {
    public static void chat(String params, SseEmitter sseEmitter) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", AIProperties.getGPT().getAccessKey());
        OkHttpUtils.postStream(AIProperties.getGPT().getUrl() + "/v1/chat/completions", params, headers, sseEmitter);
    }
}
