package com.zlc.family.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.exception.ServiceException;
import com.zlc.family.common.exception.family.FamilyException;
import com.zlc.family.common.utils.bean.BeanValidators;
import com.zlc.family.manage.domain.Favor;
import com.zlc.family.manage.mapper.FavorMapper;
import com.zlc.family.manage.service.IFavorService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
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
 * @since 2024-03-27
 */
@Service
@RequiredArgsConstructor
public class FavorServiceImpl extends ServiceImpl<FavorMapper, Favor> implements IFavorService {
    private final Validator validator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFavor(Favor favor) {
        Favor relationFavor = null;
        if (favor.getRelationId() != null) {
            relationFavor = getById(favor.getRelationId());
            if (relationFavor == null) {
                throw new FamilyException(FamilyException.Code.FAVOR_ID_ERROR);
            }
        }
        try {
            save(favor);
            if (relationFavor != null) {
                relationFavor.setRelationId(favor.getFavorId());
                relationFavor.setBalanced(FamilyConstants.FAVOR_BALANCED_IS);
                updateById(relationFavor);
            }
        } catch (DuplicateKeyException e) {
            throw new FamilyException(FamilyException.Code.FAVOR_RELATION_ID_ERROR);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFavor(Favor favor) {
        Favor oldFavor = getById(favor.getFavorId());
        // 先重置旧的关联关系
        if (oldFavor.getRelationId() != null && oldFavor.getRelationId() != favor.getRelationId()) {
            update(new UpdateWrapper<Favor>().lambda().set(Favor::getBalanced, FamilyConstants.FAVOR_BALANCED_NOT).set(Favor::getRelationId, null).eq(Favor::getFavorId, oldFavor.getRelationId()));
        }
        if (favor.getRelationId() == null) {
            update(new UpdateWrapper<Favor>().lambda().set(Favor::getRelationId, null).eq(Favor::getFavorId, favor.getFavorId()));
            updateById(favor);
            return true;
        }
        // 再建立新的关联关系
        Favor relationFavor = null;
        if (favor.getRelationId() != null) {
            relationFavor = getById(favor.getRelationId());
            if (relationFavor == null) {
                throw new FamilyException(FamilyException.Code.FAVOR_ID_ERROR);
            }
        }
        try {
            updateById(favor);
            if (relationFavor != null && (relationFavor.getRelationId() == null || relationFavor.getRelationId() != favor.getFavorId())) {
                relationFavor.setRelationId(favor.getFavorId());
                relationFavor.setBalanced(FamilyConstants.FAVOR_BALANCED_IS);
                relationFavor.init(Operator.UPDATE);
                updateById(relationFavor);
            }
        } catch (DuplicateKeyException e) {
            throw new FamilyException(FamilyException.Code.FAVOR_RELATION_ID_ERROR);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importFavor(List<Favor> favorList, boolean updateSupport, String operName) {
        int failureNum = 0;
        StringBuilder failureMsg = new StringBuilder();
        List<Favor> addList = new ArrayList<>();
        List<Favor> editList = new ArrayList<>();
        for (int i = 0; i < favorList.size(); i++) {
            Favor favor = favorList.get(i);
            try {
                BeanValidators.validateWithException(validator, favor);
            } catch (ConstraintViolationException e) {
                failureNum++;
                failureMsg.append("<br/>第 ").append(i).append(" 行导入失败。")
                        .append(e.getMessage());
                log.error(failureMsg.toString());
                continue;
            }
            if (favor.getFavorId() == null) {
                favor.init(Operator.CREATE);
                addList.add(favor);
            } else if (updateSupport) {
                favor.init(Operator.UPDATE);
                editList.add(favor);
            } else {
                failureNum++;
                failureMsg.append("<br/>第 ").append(i).append(" 行导入失败，不允许更新已存在的人情账薄");
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        saveBatch(addList);
        saveOrUpdateBatch(editList);
        return true;
    }
}
