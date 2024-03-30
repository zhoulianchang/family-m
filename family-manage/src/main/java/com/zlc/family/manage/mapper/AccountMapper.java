package com.zlc.family.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.family.manage.domain.Account;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 家庭账户表 Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-03-30
 */
public interface AccountMapper extends BaseMapper<Account> {

    void resetBalance();

    @Select("SELECT SUM(balance) FROM family_account where del_flag = 0")
    BigDecimal statsAccountBalance();
}
