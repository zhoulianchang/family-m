package com.zlc.family.manage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zlc.family.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;
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

    @Excel(name = "账户编号", cellType = Excel.ColumnType.NUMERIC,type = Excel.Type.IMPORT, prompt = "对应的是账户管理中的账户编号")
    @NotNull(message = "账户编号不能为空")
    private Long accountId;

    @Excel(name = "账户编号",type = Excel.Type.EXPORT, prompt = "对应的是账户管理中的账户别名")
    private String accountName;

    /**
     * 用户名称
     */
    @Excel(name = "消费用户")
    private String userName;

    /**
     * 账单支付日期
     */
    @NotNull(message = "账单支付时间不能为空")
    @Excel(name = "消费日期", dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payTime;
    /**
     * 账单金额
     */
    @NotNull(message = "消费金额不能为空")
    @Excel(name = "消费金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal amount;

    /**
     * 账单分类
     */
    @NotNull(message = "分类不能为空")
    @Excel(name = "分类", dictType = "bill_type")
    private Integer type;

    /**
     * 资金流向 1进 2出
     */
    @NotNull(message = "资金流向不能为空")
    @Excel(name = "资金流向", dictType = "bill_flow")
    private Integer flow;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;
}
