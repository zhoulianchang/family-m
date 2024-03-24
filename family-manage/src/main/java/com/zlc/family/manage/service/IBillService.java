package com.zlc.family.manage.service;

import com.zlc.family.manage.domain.Bill;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.family.manage.query.BillQuery;
import com.zlc.family.manage.vo.BillVo;

import java.util.List;

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
}
