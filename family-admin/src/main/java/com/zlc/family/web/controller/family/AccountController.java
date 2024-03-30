package com.zlc.family.web.controller.family;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zlc.family.common.annotation.Log;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.controller.BaseController;
import com.zlc.family.common.core.domain.AjaxResult;
import com.zlc.family.common.core.page.TableDataInfo;
import com.zlc.family.common.core.vo.BaseSelectVo;
import com.zlc.family.common.enums.BusinessType;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.exception.family.FamilyException;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 家庭账户表 前端控制器
 * </p>
 *
 * @author zlc
 * @since 2024-03-30
 */
@RestController
@RequestMapping("/family/account")
@RequiredArgsConstructor
public class AccountController extends BaseController {
    private final IAccountService accountService;

    /**
     * 获取家庭账户列表
     */
    @PreAuthorize("hasPermission('family:account:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<Account> list = accountService.list(new QueryWrapper<Account>().lambda().eq(Account::getDelFlag, FamilyConstants.DEL_NO));
        return getDataTable(list);
    }

    /**
     * 获取家庭账户列表
     */
    @PreAuthorize("hasPermission('family:account:list')")
    @GetMapping("/select")
    public AjaxResult select() {
        List<Account> list = accountService.list(new QueryWrapper<Account>().lambda().eq(Account::getDelFlag, FamilyConstants.DEL_NO).select(Account::getAccountId, Account::getName));
        return success(BaseSelectVo.init(list));
    }

    /**
     * 获取家庭账户列表
     */
    @PreAuthorize("hasPermission('family:account:query')")
    @GetMapping("/{id}")
    public AjaxResult list(@PathVariable("id") Long id) {
        return success(accountService.getById(id));
    }

    /**
     * 新增家庭账户
     */
    @PreAuthorize("hasPermission('family:account:add')")
    @Log(title = "家庭账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Account account) {
        if (account.getAccountId() != null) {
            throw new FamilyException(FamilyException.Code.ID_EXISTS);
        }
        account.init(Operator.CREATE);
        return toAjax(accountService.save(account));
    }

    /**
     * 修改家庭账户
     */
    @PreAuthorize("hasPermission('family:account:edit')")
    @Log(title = "家庭账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Account account) {
        if (account.getAccountId() == null) {
            throw new FamilyException(FamilyException.Code.ID_NULL);
        }
        account.init(Operator.UPDATE);
        return toAjax(accountService.saveOrUpdate(account));
    }

    /**
     * 删除家庭账户
     */
    @PreAuthorize("hasPermission('family:account:remove')")
    @Log(title = "家庭账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(accountService.update(new UpdateWrapper<Account>().lambda().set(Account::getDelFlag, FamilyConstants.DEL_YES).in(Account::getAccountId, ids)));
    }
}