package com.zlc.family.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlc.family.common.core.query.BaseQuery;

import java.util.Map;

/**
 * 自定义家庭工具类
 *
 * @author zlc
 * @date 2024/4/26 10:51
 */
public class FamilyUtils {

    /**
     * 为sql添加数据范围权限
     *
     * @param query 基础查询条件
     * @param qw
     */
    public static void addDataScope(BaseQuery query, QueryWrapper qw) {
        Map<String, Object> params = query.getParams();
        if (params.containsKey("dataScope")) {
            qw.apply(params.get("dataScope").toString());
        }
    }
}
