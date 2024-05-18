package com.zlc.family.common.core.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zlc.family.common.core.domain.BaseEntityFlag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zlc
 * @date 2024/5/18 17:18
 */
public class BaseTreeVo<T extends BaseTreeVo<T>> extends BaseEntityFlag {
    private static final long serialVersionUID = 1L;
    /**
     * 树节点的id,因为项目原因 每个实体类的主键id名称都不一样所以重新使用一个父类统一id 方便工具类的编写
     */
    @TableField(exist = false)
    private Long id;
    @TableField(exist = false)
    private String label;
    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 子部门
     */
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
