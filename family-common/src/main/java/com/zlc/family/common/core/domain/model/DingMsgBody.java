package com.zlc.family.common.core.domain.model;

import com.zlc.family.common.enums.DingMsgType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zlc
 * @date 2024/5/10 11:52
 */
@Getter
@Setter
public class DingMsgBody extends MsgBody {
    /**
     * 机器人唯一key（根据这个key来判断使用哪个机器人发送消息）
     */
    private String robotKey;
    /**
     * 消息类型 text|markdown
     */
    private DingMsgType dingMsgType;
    /**
     * 标题
     */
    private String title;
    /**
     * 是否@所有人
     */
    private Boolean allAt;
    /**
     * 被@人的手机号
     */
    private List<String> atMobiles;
    /**
     * 被@人的工号
     */
    private List<String> atUserIds;
}
