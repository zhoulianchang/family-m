package com.zlc.family.framework.third.ai;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author guancb
 * 2024/11/8
 */
@Getter
@Setter
public class ChatDto {
    private String model;
    @NotBlank(message = "聊天内容不能为空")
    private String content;

    public ChatDto() {
        this.model = "gpt-3.5-turbo";
    }
}
