package com.zlc.family.quartz.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.domain.model.DingMsgBody;
import com.zlc.family.common.enums.MsgType;
import com.zlc.family.common.notify.NotifyFactory;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 家庭任务定时任务
 *
 * @author family
 */
@Component("familyTask")
@Slf4j
public class FamilyTask {
    @Autowired
    private IAccountService accountService;

    /**
     * 统计家庭账户余额任务
     *
     * @param deptId 统计指定的部门id
     */
    public void statsAmount(Long deptId) {
        accountService.notifyAccountAmount(deptId);
    }
}
