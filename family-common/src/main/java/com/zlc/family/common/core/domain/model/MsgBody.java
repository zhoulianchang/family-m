package com.zlc.family.common.core.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zlc
 * @date 2024/5/10 11:47
 */
@Getter
@Setter
public class MsgBody implements Serializable {
    /**
     * 消息体内容
     */
    private String context;
}
