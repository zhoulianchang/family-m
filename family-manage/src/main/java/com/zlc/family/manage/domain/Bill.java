package com.zlc.family.manage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlc.family.common.core.domain.BaseEntityFlag;
import com.zlc.family.common.enums.Operator;
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
    private Date payTime;

    public Bill() {
    }

    public Bill(Operator operator) {
        super(operator);
    }
}
