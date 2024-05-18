package com.zlc.family.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.vo.BaseTreeVo;
import com.zlc.family.common.core.domain.TreeSelect;
import com.zlc.family.common.core.query.BaseQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义家庭工具类
 *
 * @author zlc
 * @date 2024/4/26 10:51
 */
@Slf4j
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

    /**
     * 获得本次金额的正负值
     *
     * @param entity
     * @return
     */
    public static BigDecimal getAmount(int flow, BigDecimal amount) {
        switch (flow) {
            case FamilyConstants.BILL_FLOW_OUT:
                // 如果是支出 则转为负数
                return amount.negate();
        }
        return amount;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @return 下拉树结构列表
     */
    public static <T extends BaseTreeVo> List<TreeSelect> buildTreeSelect(List<T> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            // 空集合直接返回
            return Collections.emptyList();
        }
        List<Long> tempList = dataList.stream().map(BaseTreeVo::getId).collect(Collectors.toList());
        List<T> returnList = new ArrayList<>();
        for (T data : dataList) {
            Long parentId = data.getParentId();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(parentId)) {
                recursionFn(dataList, data);
                returnList.add(data);
            }
        }
        return returnList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 递归列表
     *
     * @param dataList
     * @param data
     * @param <T>
     */
    private static <T extends BaseTreeVo> void recursionFn(List<T> list, T t) {
        List<T> children = list.stream()
                .filter(data -> t.getId().equals(data.getParentId()))
                .collect(Collectors.toList());
        t.setChildren(children);
        children.forEach(child -> recursionFn(list, child));
    }

}
