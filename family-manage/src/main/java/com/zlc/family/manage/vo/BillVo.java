package com.zlc.family.manage.vo;

import com.zlc.family.common.annotation.Excel;
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
    @Excel(name = "账单编号", cellType = Excel.ColumnType.NUMERIC, prompt = "账单编号")
    private Long billId;

    /**
     * 用户名称
     */
    @Excel(name = "消费用户")
    private String userName;

    /**
     * 账单支付日期
     */
    @Excel(name = "消费日期")
    private Date payTime;
    /**
     * 账单金额
     */
    @Excel(name = "消费金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal amount;

    /**
     * 账单分类
     */
    @Excel(name = "分类", dictType = "bill_type")
    private Integer type;

    /**
     * 资金流向 1进 2出
     */
    @Excel(name = "资金流向", dictType = "bill_flow")
    private Integer flow;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;
}
