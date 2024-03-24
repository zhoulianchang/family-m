package com.zlc.family.manage.mapper;

import com.zlc.family.manage.domain.Bill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.family.manage.query.BillQuery;
import com.zlc.family.manage.vo.BillVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-03-24
 */
public interface BillMapper extends BaseMapper<Bill> {

    List<BillVo> selectBillList(@Param("query") BillQuery query);
}
