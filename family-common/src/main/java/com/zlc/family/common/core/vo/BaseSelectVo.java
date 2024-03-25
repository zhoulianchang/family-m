package com.zlc.family.common.core.vo;

import com.zlc.family.common.core.domain.BaseEntityD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zlc
 * @date 2024/3/25 14:50
 */
public class BaseSelectVo implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 下拉框显示名称
     */
    private String name;

    public BaseSelectVo() {
    }

    public BaseSelectVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static <T extends BaseEntityD> BaseSelectVo init(T entity) {
        return new BaseSelectVo(entity.getSelectId(), entity.getSelectName());
    }

    public static <T extends BaseEntityD> List<BaseSelectVo> init(List<T> entities) {
        List<BaseSelectVo> list = new ArrayList<>();
        for (T entity : entities) {
            list.add(init(entity));
        }
        return list;
    }
}