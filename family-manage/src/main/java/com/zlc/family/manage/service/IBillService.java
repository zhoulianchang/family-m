package com.zlc.family.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.family.common.core.vo.EchartPieVo;
import com.zlc.family.manage.domain.Bill;
import com.zlc.family.manage.domain.query.BillQuery;
import com.zlc.family.manage.domain.query.BillStatsQuery;
import com.zlc.family.manage.domain.vo.BillStatsVo;
import com.zlc.family.manage.domain.vo.BillVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zlc
 * @since 2024-03-24
 */
public interface IBillService extends IService<Bill> {

    List<BillVo> selectBillList(BillQuery query);

    boolean importBill(List<BillVo> userList, boolean updateSupport, String operName);

    List<BillStatsVo> statsByFlow(BillQuery query);

    Map<String, List<EchartPieVo>> statsByType(BillStatsQuery query);

    boolean saveBill(Bill entity);

    boolean updateBill(Bill entity);

    boolean removeBill(Long[] ids);

    List<EchartPieVo> statsByUserName(BillStatsQuery query);
}
