package com.zlc.family.manage.service;

import com.zlc.family.common.core.vo.EchartPieVo;
import com.zlc.family.manage.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;

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

    BigDecimal statsAccountBalance();
}
