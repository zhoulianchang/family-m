package com.zlc.family.manage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlc.family.common.core.domain.BaseEntityFlag;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.utils.SecurityUtils;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 家庭账户表
 * </p>
 *
 * @author zlc
 * @since 2024-03-30
 */
@Getter
@Setter
@TableName("family_account")
public class Account extends BaseEntityFlag {

    private static final long serialVersionUID = 1L;

    /**
     * 账户编号
     */
    @TableId(value = "account_id", type = IdType.AUTO)
    private Long accountId;
    /**
     * 账单所属部门
     */
    private Long deptId;
    /**
     * 账户别名
     */
    private String name;

    /**
     * 账户类型 1银行卡 2支付宝 3微信
     */
    @NotNull(message = "账户类型不能为空")
    private Integer type;

    /**
     * 所属用户id
     */
    @NotNull(message = "所属用户不能为空")
    private Long userId;

    /**
     * 银行卡号
     */
    private String cardNo;
    /**
     * 账户初始余额
     */
    private BigDecimal initAmount;
    /**
     * 账户当前余额
     */
    private BigDecimal balance;

    public Account() {
    }

    public Account(Operator operator) {
        super(operator);
    }

    @Override
    public Long getSelectId() {
        return this.accountId;
    }

    @Override
    public String getSelectName() {
        return this.name;
    }

    @Override
    public void init(Operator operator) {
        super.init(operator);
        switch (operator) {
            case CREATE:
                this.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
                break;
        }
    }
}
