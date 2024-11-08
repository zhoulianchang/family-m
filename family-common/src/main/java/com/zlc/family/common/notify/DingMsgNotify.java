package com.zlc.family.common.notify;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import com.zlc.family.common.config.DingMsgProperties;
import com.zlc.family.common.constant.ErrorMsgCode;
import com.zlc.family.common.core.domain.model.DingMsgBody;
import com.zlc.family.common.exception.job.DingException;
import com.zlc.family.common.utils.DingUtils;
import com.zlc.family.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

/**
 * @author zlc
 * @date 2024/5/10 11:57
 */
@Slf4j
public class DingMsgNotify extends MsgNotify<DingMsgBody> {

    @Override
    public void sendMsg(DingMsgBody msgBody) {
        if (StringUtils.isEmpty(msgBody.getRobotKey()) || !DingMsgProperties.getRobots().containsKey(msgBody.getRobotKey())) {
            log.error("Ding message sending failure.config error");
            throw new DingException(ErrorMsgCode.DING_SEND_ERROR_CONFIG);
        }
        // 1.获取钉钉机器人的配置信息
        DingMsgProperties.RobotConfig robotConfig = DingMsgProperties.getRobots().get(msgBody.getRobotKey());
        // 2.获取钉钉客户端对象类
        DingTalkClient client = getDingTalkClient(robotConfig);
        if (client == null) {
            log.error("Ding message sending failure.not support type:{}", robotConfig.getType());
            throw new DingException(ErrorMsgCode.DING_SEND_ERROR_TYPE);
        }
        // 3.构造请求对象类
        OapiRobotSendRequest req = getReq(msgBody);
        try {
            // 4.发送http请求进行消息发送
            OapiRobotSendResponse rsp = client.execute(req, robotConfig.getToken());
            if (!rsp.isSuccess()) {
                log.error("Ding message sending failure.code is :{},reason is :{}", rsp.getErrcode(), rsp.getErrmsg());
                throw new DingException(ErrorMsgCode.DING_SEND_ERROR, new Object[]{rsp.getErrmsg()});
            }
        } catch (ApiException e) {
            log.error("Ding message sending failure.code is :{},reason is :{}", e.getErrCode(), e.getErrMsg());
            throw new DingException(ErrorMsgCode.DING_SEND_ERROR, new Object[]{e.getErrMsg()});
        }
    }

    /**
     * 获取DingTalk客户端
     *
     * @param robotConfig 钉钉机器人配置
     * @return
     */
    private DingTalkClient getDingTalkClient(DingMsgProperties.RobotConfig robotConfig) {
        String url = DingMsgProperties.getUrl();
        switch (robotConfig.getType()) {
            case DingUtils.ROBOT_TYPE_SECRET:
                String sign = DingUtils.getSign(robotConfig.getSecret(), System.currentTimeMillis());
                url += sign;
                break;
            default:
                return null;
        }
        return new DefaultDingTalkClient(url);
    }

    /**
     * 获取钉钉发送消息的请求对象
     *
     * @param msgBody
     * @return
     */
    private OapiRobotSendRequest getReq(DingMsgBody msgBody) {
        OapiRobotSendRequest req = new OapiRobotSendRequest();
        //设置消息类型
        req.setMsgtype(msgBody.getDingMsgType().name().toLowerCase(Locale.ROOT));
        switch (msgBody.getDingMsgType()) {
            case TEXT:
                OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                text.setContent(msgBody.getContext());
                req.setText(text);
                break;
            case MARKDOWN:
                OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
                markdown.setTitle(msgBody.getTitle());
                markdown.setText(msgBody.getContext());
                req.setMarkdown(markdown);
                break;
        }
        req.setAt(getAt(msgBody));
        return req;
    }

    /**
     * 获取@对象
     *
     * @param msgBody
     * @return
     */
    private OapiRobotSendRequest.At getAt(DingMsgBody msgBody) {
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setIsAtAll(msgBody.getAllAt());
        at.setAtMobiles(msgBody.getAtMobiles());
        at.setAtUserIds(msgBody.getAtUserIds());
        return at;
    }
}
