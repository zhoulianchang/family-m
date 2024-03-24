package com.zlc.family.web.controller.family;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zlc.family.common.annotation.Log;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.controller.BaseController;
import com.zlc.family.common.core.domain.AjaxResult;
import com.zlc.family.common.core.page.TableDataInfo;
import com.zlc.family.common.enums.BusinessType;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.exception.family.FamilyException;
import com.zlc.family.common.utils.bean.BeanUtils;
import com.zlc.family.manage.domain.Bill;
import com.zlc.family.manage.dto.BillDto;
import com.zlc.family.manage.query.BillQuery;
import com.zlc.family.manage.service.IBillService;
import com.zlc.family.manage.vo.BillVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zlc
 * @since 2024-03-24
 */
@RestController
@RequestMapping("/family/bill")
@RequiredArgsConstructor
public class BillController extends BaseController {
    private final IBillService billService;

    /**
     * 获取账单列表
     */
    @PreAuthorize("hasPermission('family:bill:list')")
    @GetMapping("/list")
    public TableDataInfo list(BillQuery query) {
        startPage();
        List<BillVo> list = billService.selectBillList(query);
        return getDataTable(list);
    }

    /**
     * 获取账单详情
     */
    @PreAuthorize("hasPermission('family:bill:query')")
    @GetMapping("/{id}")
    public AjaxResult query(@PathVariable("id") Long id) {
        return success(billService.getById(id));
    }

    /**
     * 新增账单
     */
    @PreAuthorize("hasPermission('family:bill:add')")
    @Log(title = "账单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BillDto dto) {
        dto.setBillId(null);
        Bill entity = new Bill(Operator.CREATE);
        BeanUtils.copyBeanProp(entity, dto);
        return toAjax(billService.save(entity));
    }

    /**
     * 修改账单
     */
    @PreAuthorize("hasPermission('family:bill:edit')")
    @Log(title = "账单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BillDto dto) {
        if (dto.getBillId() == null) {
            throw new FamilyException(FamilyException.Code.BILL_ID_NULL);
        }
        Bill entity = new Bill(Operator.UPDATE);
        BeanUtils.copyBeanProp(entity, dto);
        return toAjax(billService.updateById(entity));
    }

    /**
     * 删除账单
     */
    @PreAuthorize("hasPermission('family:bill:remove')")
    @Log(title = "账单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(billService.update(new UpdateWrapper<Bill>().lambda().set(Bill::getDelFlag, FamilyConstants.DEL_YES).in(Bill::getBillId, ids)));
    }
}