package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.mapper.AccountMapper;
import com.zlc.family.manage.service.IAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    public BigDecimal statsAccountBalance() {
        return getBaseMapper().statsAccountBalance();
    }

    @Override
    public void resetBalance() {
        getBaseMapper().resetBalance();
    }
}
