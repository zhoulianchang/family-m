package com.zlc.family.common.core.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zlc
 * @date 2024/3/29 20:48
 */

public class EchartPieVo implements Serializable {
    private String name;
    private BigDecimal value;

    public EchartPieVo() {
    }

    public EchartPieVo(String name, BigDecimal value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
