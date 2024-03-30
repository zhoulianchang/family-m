package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.vo.EchartPieVo;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.mapper.AccountMapper;
import com.zlc.family.manage.service.IAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
}
