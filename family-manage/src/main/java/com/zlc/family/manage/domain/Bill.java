package com.zlc.family.manage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zlc.family.common.core.domain.BaseEntityFlag;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.utils.SecurityUtils;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zlc
 * @since 2024-03-24
 */
@Getter
@Setter
@TableName("family_bill")
public class Bill extends BaseEntityFlag {

    private static final long serialVersionUID = 1L;

    /**
     * 账单编号
     */
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Long billId;
    /**
     * 账单所属部门
     */
    private Long deptId;
    /**
     * 所属账户编号
     */
    private Long accountId;

    /**
     * 账单金额
     */
    private BigDecimal amount;

    /**
     * 谁花的
     */
    private String userName;

    /**
     * 账单分类
     */
    private Integer type;

    /**
     * 资金流向 1进 2出
     */
    private Integer flow;

    /**
     * 账单支付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payTime;
    /**
     * 所属账户别名
     */
    @TableField(exist = false)
    private String accountName;

    public Bill() {
    }

    public Bill(Operator operator) {
        super(operator);
        switch (operator) {
            case CREATE:
                this.setDeptId(SecurityUtils.getLoginUser().getUser().getDeptId());
                break;
        }
    }
}
