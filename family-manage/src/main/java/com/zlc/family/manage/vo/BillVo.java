package com.zlc.family.manage.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zlc
 * @date 2024/3/24 21:34
 */
@Data
public class BillVo implements Serializable {

    /**
     * 账单编号
     */
    private Long billId;

    /**
     * 账单金额
     */
    private BigDecimal amount;

    /**
     * 用户名称
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
    /**
     * 备注
     */
    private String remark;
}
