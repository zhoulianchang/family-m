package com.zlc.family.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.family.common.core.query.BaseQuery;
import com.zlc.family.manage.domain.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 家庭账户表 服务类
 * </p>
 *
 * @author zlc
 * @since 2024-03-30
 */
public interface IAccountService extends IService<Account> {

    BigDecimal statsAccountBalance(BaseQuery query);

    void resetBalance();

    List<Account> listAccount(BaseQuery query);
}
