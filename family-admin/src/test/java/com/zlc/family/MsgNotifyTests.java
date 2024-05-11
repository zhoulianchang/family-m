package com.zlc.family;

import com.zlc.family.common.core.domain.model.DingMsgBody;
import com.zlc.family.common.enums.DingMsgType;
import com.zlc.family.common.enums.MsgType;
import com.zlc.family.common.notify.NotifyFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author zlc
 * @date 2024/5/11 16:15
 */
@SpringBootTest
public class MsgNotifyTests {
    @Test
    void dingMsgTest() {
        DingMsgBody body = new DingMsgBody("我是一条测试信息");
        body.setDingMsgType(DingMsgType.TEXT);
        body.setAtMobiles(Arrays.asList("18757050703"));
        body.setAllAt(true);
        NotifyFactory.createMsgNotify(MsgType.DING).sendMsg(body);
    }
}
