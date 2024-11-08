package com.zlc.family.common.constant;

/**
 * @author zlc
 * @date 2024/3/24 21:08
 */
public class FamilyConstants {
    /**
     * 数据库未删除标志
     */
    public static final int DEL_NO = 0;
    /**
     * 消息通知锁定时间 1分钟
     */
    public static final int MSG_LOCK_TIME = 1;
    /**
     * 数据库已删除标志
     */
    public static final int DEL_YES = 2;
    /**
     * 不需要平账
     */
    public static final int FAVOR_BALANCED_DO_WITHOUT = 1;
    /**
     * 已平账
     */
    public static final int FAVOR_BALANCED_IS = 2;
    /**
     * 未平账
     */
    public static final int FAVOR_BALANCED_NOT = 3;
    /**
     * 收入
     */
    public static final int BILL_FLOW_IN = 1;
    /**
     * 支出
     */
    public static final int BILL_FLOW_OUT = 2;
    /**
     * AI聊天结束标识
     */
    public static final String AI_END_FLAG = "DONE";
    /**
     * http相关
     */
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_TEXT = "text/html";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_GET = "GET";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";

}
