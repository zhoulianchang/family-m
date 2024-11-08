package com.zlc.family.web.controller.family;

import com.alibaba.fastjson2.JSON;
import com.zlc.family.common.core.controller.BaseController;
import com.zlc.family.framework.third.ai.ChatDto;
import com.zlc.family.framework.third.ai.gpt.GPTChatBody;
import com.zlc.family.framework.third.ai.gpt.GPTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;

/**
 * @author zlc
 * 2024/11/8
 */
@RestController
@RequestMapping("/family/ai")
@RequiredArgsConstructor
public class AIController extends BaseController {
    /**
     * 获取账单列表
     */
    @PostMapping("/chat")
    public SseEmitter chat(@RequestBody @Valid ChatDto chatDto) {
        SseEmitter sseEmitter = new SseEmitter();
        GPTUtils.chat(JSON.toJSONString(GPTChatBody.defaultChat(chatDto)), sseEmitter);
        return sseEmitter;
    }
}
