package com.zlc.family.manage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlc.family.common.annotation.Excel;
import com.zlc.family.common.core.domain.BaseEntityFlag;
import com.zlc.family.common.enums.Operator;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zlc
 * @since 2024-03-27
 */
@Getter
@Setter
@TableName("family_favor")
public class Favor extends BaseEntityFlag {

    private static final long serialVersionUID = 1L;

    /**
     * 人情薄ID
     */
    @TableId(value = "favor_id", type = IdType.AUTO)
    @Excel(name = "人情编号", cellType = Excel.ColumnType.NUMERIC)
    private Long favorId;
    @Excel(name = "关联人情编号", cellType = Excel.ColumnType.NUMERIC)
    private Long relationId;

    /**
     * 送/被送礼人
     */
    @NotBlank(message = "礼金人不能为空")
    @Excel(name = "礼金人")
    private String userName;

    /**
     * 资金流向
     */
    @NotNull(message = "资金流向不能为空")
    @Excel(name = "资金流向", dictType = "bill_flow")
    private Integer flow;

    /**
     * 人情时间
     */
    @NotNull(message = "人情时间不能为空")
    @Excel(name = "人情时间", dateFormat = "yyyy-MM-dd")
    private Date favorTime;
    /**
     * 礼金金额
     */
    @NotNull(message = "礼金金额不能为空")
    @Excel(name = "礼金金额", cellType = Excel.ColumnType.NUMERIC)
    private BigDecimal amount;
    /**
     * 平账情况 1不需要 2已平账 3未平账
     */
    @NotNull(message = "平账情况不能为空")
    @Excel(name = "平账情况", dictType = "favor_balanced")
    private Integer balanced;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    public Favor() {
    }

    public Favor(Operator operator) {
        super(operator);
    }
}
