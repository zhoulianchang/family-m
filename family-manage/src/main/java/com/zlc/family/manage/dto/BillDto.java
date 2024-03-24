package com.zlc.family.manage.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zlc
 * @date 2024/3/24 22:10
 */
@Data
public class BillDto {

    /**
     * 账单编号
     */
    private Long billId;

    /**
     * 账单金额
     */
    @NotNull(message = "消费金额不能为空")
    private BigDecimal amount;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 账单分类
     */
    @NotNull(message = "分类不能为空")
    private Integer type;

    /**
     * 资金流向 1收入 2支出
     */
    @NotNull(message = "资金流向不能为空")
    private Integer flow;

    /**
     * 账单支付日期
     */
    @NotNull(message = "账单支付时间不能为空")
    private Date payTime;

    /**
     * 备注
     */
    private String remark;
}
