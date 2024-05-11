package com.zlc.family.common.notify;

import com.zlc.family.common.constant.ErrorMsgCode;
import com.zlc.family.common.core.domain.model.DingMsgBody;
import com.zlc.family.common.core.domain.model.MsgBody;
import com.zlc.family.common.enums.MsgType;
import com.zlc.family.common.exception.job.DingException;

import java.util.Collections;

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
                throw new DingException(ErrorMsgCode.DING_CREATE_ERROR);
        }
    }
}
