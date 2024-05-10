package com.zlc.family.common.utils;

import com.zlc.family.common.constant.Constants;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

/**
 * @author zlc
 * @date 2024/5/10 14:08
 * 钉钉的工具类
 */
public class DingUtils {
    /**
     * 机器人唯一key
     */
    public static final String ROBOT_YUQUE = "yuque";
    public static final String ROBOT_FAMILY = "family";
    /**
     * 机器人认证类型
     * 1：关键字
     * 2：加签
     */
    public static final int ROBOT_TYPE_KEY = 1;
    public static final int ROBOT_TYPE_SECRET = 2;

    /**
     * 获取签名
     *
     * @param secret    密钥
     * @param timestamp 时间戳
     * @return 返回请求url后拼接的加签参数
     */
    @SneakyThrows
    public static String getSign(String secret, Long timestamp) {
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance(Constants.HMAC_SHA256);
        mac.init(new SecretKeySpec(secret.getBytes(Constants.UTF8), Constants.HMAC_SHA256));
        byte[] signData = mac.doFinal(stringToSign.getBytes(Constants.UTF8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), Constants.UTF8);
        return "?sign=" + sign + "&timestamp=" + timestamp;
    }
}
