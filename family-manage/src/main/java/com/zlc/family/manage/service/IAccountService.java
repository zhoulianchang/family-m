package com.zlc.family.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.family.manage.domain.Account;

import java.math.BigDecimal;

/**
 * <p>
 * 家庭账户表 服务类
 * </p>
 *
 * @author zlc
 * @since 2024-03-30
 */
public interface IAccountService extends IService<Account> {

    BigDecimal statsAccountBalance();

    void resetBalance();
}
