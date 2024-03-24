package com.zlc.family.common.core.domain;

import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.enums.Operator;

/**
 * @author zlc
 * @date 2024/3/24 20:55
 * 数据库实体类基础+删除标识
 */
public class BaseEntityFlag extends BaseEntityD {
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private Integer delFlag;

    public BaseEntityFlag() {
    }

    public BaseEntityFlag(Operator operator) {
        super(operator);
        switch (operator) {
            case DELETE:
                this.delFlag = FamilyConstants.DEL_YES;
                break;
        }
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
