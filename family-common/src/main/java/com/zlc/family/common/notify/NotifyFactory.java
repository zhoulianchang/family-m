package com.zlc.family.common.notify;

import com.zlc.family.common.enums.MsgType;

/**
 * @author zlc
 * @date 2024/5/10 14:56
 * 消息通知工厂类
 */
public class NotifyFactory {
    /**
     * 创建具体消息通知对象
     *
     * @param msgType
     * @return
     */
    public static MsgNotify createMsgNotify(MsgType msgType) {
        switch (msgType) {
            case DING:
                return new DingMsgNotify();
            default:
                break;
        }
        return null;
    }
}
