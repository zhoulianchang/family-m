package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.common.annotation.DataScope;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.domain.model.DingMsgBody;
import com.zlc.family.common.core.query.BaseQuery;
import com.zlc.family.common.enums.MsgType;
import com.zlc.family.common.notify.NotifyFactory;
import com.zlc.family.common.utils.DateUtils;
import com.zlc.family.common.utils.FamilyUtils;
import com.zlc.family.common.utils.StringUtils;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.mapper.AccountMapper;
import com.zlc.family.manage.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * 家庭账户表 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2024-03-30
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Override
    @DataScope(existsAlias = false)
    public BigDecimal statsAccountBalance(BaseQuery query) {
        return getBaseMapper().statsAccountBalance(query);
    }

    @Override
    public void resetBalance() {
        getBaseMapper().resetBalance();
    }

    @Override
    @DataScope(existsAlias = false, useSql = false)
    public List<Account> listAccount(BaseQuery query) {
        QueryWrapper<Account> qw = new QueryWrapper<Account>();
        qw.lambda().eq(Account::getDelFlag, FamilyConstants.DEL_NO);
        FamilyUtils.addDataScope(query, qw);
        return list(qw);
    }

    @Override
    public void notifyAccountAmount(Long deptId) {
        // 1.查询当前部门所有的账户信息
        List<Account> accountList = list(new QueryWrapper<Account>().lambda().eq(Account::getDeptId, deptId).eq(Account::getDelFlag, FamilyConstants.DEL_NO).gt(Account::getBalance, 0));
        if (CollectionUtils.isEmpty(accountList)) {
            log.warn("this dept[{}] not have any account.", deptId);
            return;
        }
        // 2.发送钉钉消息推送
        DingMsgBody msgBody = new DingMsgBody(buildContext(accountList), true);
        msgBody.setTitle("余额");
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

    @Override
    public void notifyAccountAmount(Long accountId, BigDecimal amount, String notifyTarget) {
        if (StringUtils.isEmpty(notifyTarget)) {
            log.warn("not have any notify target,please check");
            return;
        }
        Account account = getById(accountId);
        String template = "### 账户:%s\n---\n* 账户余额：%.2f\n* 操作金额：%.2f\n* 操作时间：%s\n* 操作人：@%s\n";
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        // 2.发送钉钉消息推送
        DingMsgBody msgBody = new DingMsgBody(String.format(template, account.getName(), account.getBalance(), amount, now, notifyTarget));
        msgBody.setTitle("余额");
        msgBody.setAtMobiles(Collections.singletonList(notifyTarget));
        NotifyFactory.createMsgNotify(MsgType.DING).sendMsg(msgBody);
    }
}
