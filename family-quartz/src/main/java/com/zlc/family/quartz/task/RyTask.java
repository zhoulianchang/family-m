package com.zlc.family.quartz.task;

import org.springframework.stereotype.Component;
import com.zlc.family.common.utils.StringUtils;

/**
 * 定时任务调度测试
 *
 * @author family
 */
@Component("ryTask")
public class RyTask {
    /**
     * ryTask.ryMultipleParams('ry', true, 2000L, 316.50D, 100)
     */
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }
}
