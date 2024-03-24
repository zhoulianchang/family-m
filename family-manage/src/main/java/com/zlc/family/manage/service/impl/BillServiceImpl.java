package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlc.family.manage.domain.Bill;
import com.zlc.family.manage.mapper.BillMapper;
import com.zlc.family.manage.query.BillQuery;
import com.zlc.family.manage.service.IBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.manage.vo.BillVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2024-03-24
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {

    @Override
    public List<BillVo> selectBillList(BillQuery query) {
        return getBaseMapper().selectBillList(query);
    }
}
