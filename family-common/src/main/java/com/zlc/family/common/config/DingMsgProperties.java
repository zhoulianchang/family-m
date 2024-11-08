package com.zlc.family.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author zlc
 * @date 2024/5/10 14:01
 * 钉钉消息配置类
 */
@Component
@ConfigurationProperties(prefix = "ding-msg")
public class DingMsgProperties {

    private static String url;
    private static Map<String, RobotConfig> robots;

    public static String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        DingMsgProperties.url = url;
    }

    public static Map<String, RobotConfig> getRobots() {
        return robots;
    }

    public void setRobots(Map<String, RobotConfig> robots) {
        DingMsgProperties.robots = robots;
    }

    public static class RobotConfig {
        /**
         * 每个机器人的token
         */
        private String token;
        /**
         * 机器人的类型 1是关键字 2是加签
         */
        private Integer type;
        /**
         * 关键字 针对类型1
         */
        private List<String> keys;
        /**
         * 加签的密钥 针对类型2
         */
        private String secret;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public List<String> getKeys() {
            return keys;
        }

        public void setKeys(List<String> keys) {
            this.keys = keys;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }
}
