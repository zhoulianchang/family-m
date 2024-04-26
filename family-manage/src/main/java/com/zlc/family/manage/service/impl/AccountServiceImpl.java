package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.common.annotation.DataScope;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.query.BaseQuery;
import com.zlc.family.common.utils.FamilyUtils;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.mapper.AccountMapper;
import com.zlc.family.manage.service.IAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

}
