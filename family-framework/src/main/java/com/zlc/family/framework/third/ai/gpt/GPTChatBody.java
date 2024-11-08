package com.zlc.family.framework.third.ai.gpt;

import com.zlc.family.framework.third.ai.ChatDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author guancb
 * 2024/11/8
 */
@Getter
@Setter
@NoArgsConstructor
public class GPTChatBody {
    /**
     * {
     * "model": "gpt-3.5-turbo",
     * "messages": [
     * {
     * "role": "user",
     * "content": "帮我写一段500字的文章"
     * }
     * ],
     * "stream": true,
     * "temperature": 0.7
     * }
     */
    private String model;
    private Boolean stream;
    private BigDecimal temperature;
    private List<MessageDto> messages;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class MessageDto {
        private String role;
        private String content;
    }

    public static GPTChatBody defaultChat(ChatDto chatDto) {
        GPTChatBody body = new GPTChatBody();
        body.setModel(chatDto.getModel());
        body.setStream(true);
        body.setTemperature(new BigDecimal("0.7"));
        MessageDto messageDto = new MessageDto();
        messageDto.setContent(chatDto.getContent());
        messageDto.setRole("user");
        body.setMessages(Collections.singletonList(messageDto));
        return body;
    }
}
