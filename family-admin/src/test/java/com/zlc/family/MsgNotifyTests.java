package com.zlc.family;

import com.alibaba.fastjson2.JSON;
import com.zlc.family.common.config.DingMsgConfig;
import com.zlc.family.common.core.domain.model.DingMsgBody;
import com.zlc.family.common.enums.DingMsgType;
import com.zlc.family.common.enums.MsgType;
import com.zlc.family.common.notify.NotifyFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Map;

/**
 * @author zlc
 * @date 2024/5/11 16:15
 */
@SpringBootTest
@Slf4j
public class MsgNotifyTests {
    @BeforeEach
    void beforeAll() {
        for (Map.Entry<String, DingMsgConfig.RobotConfig> entry : DingMsgConfig.getRobots().entrySet()) {
            log.info("your ding robot [{}] config is:{}", entry.getKey(), JSON.toJSONString(entry.getValue()));
        }
    }

    @Test
    void dingMsgTest() {
        DingMsgBody body = new DingMsgBody("我是一条测试信息");
        body.setDingMsgType(DingMsgType.TEXT);
        body.setAtMobiles(Arrays.asList("18757050703"));
        body.setAllAt(true);
        NotifyFactory.createMsgNotify(MsgType.DING).sendMsg(body);
    }
}
