package com.zlc.family.manage.mapper;

import com.zlc.family.manage.domain.Bill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.family.manage.domain.query.BillQuery;
import com.zlc.family.manage.domain.query.BillStatsQuery;
import com.zlc.family.manage.domain.vo.BillStatsVo;
import com.zlc.family.manage.domain.vo.BillVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-03-24
 */
public interface BillMapper extends BaseMapper<Bill> {

    List<BillVo> selectBillList(@Param("query") BillQuery query);

    List<BillStatsVo> statsByFlow(@Param("query") BillQuery query);

    /**
     * 根据类型进行分组统计
     * @param query
     * @return
     */
    List<Bill> statsByType(@Param("query") BillStatsQuery query);

    /**
     * 根据消费用户进行分组统计支出金额
     * @param query
     * @return
     */
    List<Bill> statsByUser(@Param("query") BillStatsQuery query);
}
