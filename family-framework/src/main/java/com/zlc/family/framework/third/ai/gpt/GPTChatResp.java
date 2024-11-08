package com.zlc.family.framework.third.ai.gpt;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author guancb
 * 2024/11/8
 */
@Getter
@Setter
@NoArgsConstructor
public class GPTChatResp {
    private String id;
    private List<Choices> choices;
    private Long created;
    private String model;
    private String object;
    private String systemFingerprint;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Choices {
        private int index;
        private JSONObject delta;
        private String logprobs;
        private String finishReason;
    }
}
