package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.exception.ServiceException;
import com.zlc.family.common.utils.bean.BeanUtils;
import com.zlc.family.common.utils.bean.BeanValidators;
import com.zlc.family.manage.domain.Bill;
import com.zlc.family.manage.mapper.BillMapper;
import com.zlc.family.manage.query.BillQuery;
import com.zlc.family.manage.service.IBillService;
import com.zlc.family.manage.vo.BillStatsVo;
import com.zlc.family.manage.vo.BillVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
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
@RequiredArgsConstructor
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {
    private final Validator validator;

    @Override
    public List<BillVo> selectBillList(BillQuery query) {
        return getBaseMapper().selectBillList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importBill(List<BillVo> billVoList, boolean updateSupport, String operName) {
        int failureNum = 0;
        StringBuilder failureMsg = new StringBuilder();
        List<Bill> addList = new ArrayList<>();
        List<Bill> editList = new ArrayList<>();
        for (int i = 0; i < billVoList.size(); i++) {
            BillVo billVo = billVoList.get(i);
            try {
                BeanValidators.validateWithException(validator, billVo);
            } catch (ConstraintViolationException e) {
                failureNum++;
                failureMsg.append("<br/>第 ").append(i).append(" 行导入失败。")
                        .append(e.getMessage());
                log.error(failureMsg.toString());
                continue;
            }
            Bill bill = null;
            if (billVo.getBillId() == null) {
                bill = new Bill(Operator.CREATE);
                addList.add(bill);
            } else if (updateSupport) {
                bill = new Bill(Operator.UPDATE);
                editList.add(bill);
            } else {
                failureNum++;
                failureMsg.append("<br/>第 ").append(i).append(" 行导入失败，不允许更新已存在的账单");
            }
            BeanUtils.copyProperties(billVo, bill);
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        saveBatch(addList);
        saveOrUpdateBatch(editList);
        return true;
    }

    @Override
    public List<BillStatsVo> statsByFlow(BillQuery query) {
        return getBaseMapper().statsByFlow(query);
    }
}
