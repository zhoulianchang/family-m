package com.zlc.family.common.notify;

import com.zlc.family.common.core.domain.model.MsgBody;

/**
 * @author zlc
 * @date 2024/5/10 11:44
 * 消息通知抽象类
 */
public abstract class MsgNotify<T extends MsgBody> {
    /**
     * 发送消息
     *
     * @param <T> 消息体的类型，必须是 MsgBody 的子类
     * @return
     */
    public abstract void sendMsg(T msgBody);
}
