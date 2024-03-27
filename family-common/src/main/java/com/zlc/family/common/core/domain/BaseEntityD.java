package com.zlc.family.common.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.utils.DateUtils;
import com.zlc.family.common.utils.SecurityUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zlc
 * @date 2024/3/24 20:55
 * 数据库实体类基础
 */
public class BaseEntityD implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    public BaseEntityD() {
    }

    public BaseEntityD(Operator operator) {
        init(operator);
    }

    public void init(Operator operator) {
        switch (operator) {
            case CREATE:
                this.setCreateBy(SecurityUtils.getLoginUser().getUser().getNickName());
                this.setCreateTime(DateUtils.getNowDate());
                break;
            case UPDATE:
            case DELETE:
                // 防止有人传参错误导致更改创建人
                this.setCreateBy(null);
                this.setCreateTime(null);
                this.setUpdateBy(SecurityUtils.getLoginUser().getUser().getNickName());
                this.setUpdateTime(DateUtils.getNowDate());
                break;
        }
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonIgnore
    public Long getSelectId() {
        return null;
    }

    @JsonIgnore
    public String getSelectName() {
        return null;
    }
}
