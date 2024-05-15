package com.zlc.family.manage.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zlc
 * @date 2024/3/25 21:04
 */
@Data
public class BillStatsVo implements Serializable {
    /**
     * 资金流向
     */
    private Integer flow;
    /**
     * 总额
     */
    private BigDecimal amount;
}
