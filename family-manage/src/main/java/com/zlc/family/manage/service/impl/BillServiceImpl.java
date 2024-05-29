package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.common.annotation.DataScope;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.vo.EchartPieVo;
import com.zlc.family.common.enums.BillType;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.exception.ServiceException;
import com.zlc.family.common.utils.FamilyUtils;
import com.zlc.family.common.utils.bean.BeanUtils;
import com.zlc.family.common.utils.bean.BeanValidators;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.domain.Bill;
import com.zlc.family.manage.mapper.AccountMapper;
import com.zlc.family.manage.mapper.BillMapper;
import com.zlc.family.manage.domain.query.BillQuery;
import com.zlc.family.manage.domain.query.BillStatsQuery;
import com.zlc.family.manage.service.IBillService;
import com.zlc.family.manage.domain.vo.BillStatsVo;
import com.zlc.family.manage.domain.vo.BillVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    private final AccountMapper accountMapper;

    @Override
    @DataScope(deptAlias = "fb")
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
        accountMapper.resetBalance();
        return true;
    }

    @Override
    @DataScope(deptAlias = "fb")
    public List<BillStatsVo> statsByFlow(BillQuery query) {
        return getBaseMapper().statsByFlow(query);
    }

    @Override
    @DataScope(deptAlias = "fb")
    public Map<String, List<EchartPieVo>> statsByType(BillStatsQuery query) {
        // 获取分类统计后的账单数据
        List<Bill> billList = getBaseMapper().statsByType(query);
        return billList.stream()
                .filter(bill -> BillType.OUT_CODES.contains(bill.getType()) || BillType.IN_CODES.contains(bill.getType()))
                .collect(Collectors.groupingBy(bill -> {
                    if (BillType.OUT_CODES.contains(bill.getType())) {
                        return "out";
                    } else {
                        return "in";
                    }
                }, Collectors.mapping(bill -> new EchartPieVo(BillType.getNameByCode(bill.getType()), bill.getAmount()), Collectors.toList())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBill(Bill entity) {
        updateAccount(entity.getAccountId(), FamilyUtils.getAmount(entity.getFlow(), entity.getAmount()));
        save(entity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBill(Bill entity) {
        Bill oldBill = getById(entity.getBillId());
        if (oldBill.getAccountId() != entity.getAccountId()) {
            // 如果账户id发生变化 那么需要将旧的撤回 新的做更新
            // 1. 先获取上一次的金额复原
            BigDecimal lastAmountFixed = FamilyUtils.getAmount(oldBill.getFlow(), oldBill.getAmount()).negate();
            updateAccount(oldBill.getAccountId(), lastAmountFixed);
            // 2. 再得到本次的金额
            BigDecimal curAmount = FamilyUtils.getAmount(entity.getFlow(), entity.getAmount());
            updateAccount(entity.getAccountId(), curAmount);
        } else if (oldBill.getAmount().compareTo(entity.getAmount()) != 0 || oldBill.getFlow() != entity.getFlow()) {
            // 如果本次修改了金额或者是资金流向 那么账户余额也要重新计算
            // 1. 先获取上一次的金额复原
            BigDecimal lastAmountFixed = FamilyUtils.getAmount(oldBill.getFlow(), oldBill.getAmount()).negate();
            // 2. 再得到本次的金额
            BigDecimal curAmount = FamilyUtils.getAmount(entity.getFlow(), entity.getAmount());
            // 3. 两次金额作加法 就是本次应该修改的金额
            BigDecimal realAmount = lastAmountFixed.add(curAmount);
            updateAccount(entity.getAccountId(), realAmount);
        }
        updateById(entity);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeBill(Long[] ids) {
        // 1. 删除账单数据
        update(new UpdateWrapper<Bill>().lambda().set(Bill::getDelFlag, FamilyConstants.DEL_YES).in(Bill::getBillId, ids));
        // 2. 查询出所有的账单 并统计汇总 按照 accountId amount 存储并对account操作 恢复对应的金额数量
        Map<Long, BigDecimal> accountMap = new HashMap<>();
        List<Bill> billList = listByIds(Arrays.asList(ids));
        for (Bill bill : billList) {
            BigDecimal amount = FamilyUtils.getAmount(bill.getFlow(), bill.getAmount());
            if (accountMap.containsKey(bill.getAccountId())) {
                accountMap.put(bill.getAccountId(), accountMap.get(bill.getAccountId()).add(amount));
            } else {
                accountMap.put(bill.getAccountId(), amount);
            }
        }
        // 因为一次删除理论上最多同时操作10个账户，所以挨个修改即可。 因为实际上不可能出现这种情况
        for (Map.Entry<Long, BigDecimal> entry : accountMap.entrySet()) {
            updateAccount(entry.getKey(), entry.getValue().negate());
        }
        return true;
    }

    @Override
    @DataScope(deptAlias = "fb")
    public List<EchartPieVo> statsByUserName(BillStatsQuery query) {
        List<Bill> billList = getBaseMapper().statsByUser(query);
        return billList.stream().collect(Collectors.mapping(bill -> new EchartPieVo(bill.getUserName(), bill.getAmount()), Collectors.toList()));
    }

    private void updateAccount(Long accountId, BigDecimal realAmount) {
        accountMapper.update(null, new UpdateWrapper<Account>()
                .lambda().setSql("balance = balance +" + realAmount)
                .eq(Account::getAccountId, accountId));
    }
}
