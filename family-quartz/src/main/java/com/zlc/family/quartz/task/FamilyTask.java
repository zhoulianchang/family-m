package com.zlc.family.quartz.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.domain.model.DingMsgBody;
import com.zlc.family.common.enums.DingMsgType;
import com.zlc.family.common.enums.MsgType;
import com.zlc.family.common.notify.NotifyFactory;
import com.zlc.family.common.utils.DateUtils;
import com.zlc.family.common.utils.DingUtils;
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
        // 1.查询当前部门所有的账户信息
        List<Account> accountList = accountService.list(new QueryWrapper<Account>().lambda().eq(Account::getDeptId, deptId).eq(Account::getDelFlag, FamilyConstants.DEL_NO).gt(Account::getBalance, 0));
        if (CollectionUtils.isEmpty(accountList)) {
            log.warn("this dept[{}] not have any account.", deptId);
            return;
        }
        // 2.发送钉钉消息推送
        DingMsgBody msgBody = new DingMsgBody();
        msgBody.setContext(buildContext(accountList));
        msgBody.setAllAt(true);
        msgBody.setTitle("账户情况");
        msgBody.setRobotKey(DingUtils.ROBOT_FAMILY);
        msgBody.setDingMsgType(DingMsgType.MARKDOWN);
        NotifyFactory.createMsgNotify(MsgType.DING).sendMsg(msgBody);
    }

    /**
     * 构造发送的具体内容
     *
     * @param accountList
     * @return
     */
    private String buildContext(List<Account> accountList) {
        String template = "---\n" +
                "* 账户别名：%s\n" +
                "* 账户余额：%.2f\n" +
                "* 账户号码：%s\n";
        StringBuilder sb = new StringBuilder("### 账户详情\n#### 总余额：%.2f\n");
        BigDecimal totalSum = new BigDecimal(0);
        for (Account account : accountList) {
            totalSum = totalSum.add(account.getBalance());
            sb.append(String.format(template, account.getName(), account.getBalance(), account.getCardNo()));
        }
        return String.format(sb.toString(), totalSum);
    }
}
