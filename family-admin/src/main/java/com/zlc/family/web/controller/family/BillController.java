package com.zlc.family.web.controller.family;

import com.zlc.family.common.annotation.Log;
import com.zlc.family.common.annotation.RepeatSubmit;
import com.zlc.family.common.constant.CacheConstants;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.controller.BaseController;
import com.zlc.family.common.core.domain.AjaxResult;
import com.zlc.family.common.core.page.TableDataInfo;
import com.zlc.family.common.core.redis.RedisCache;
import com.zlc.family.common.enums.BusinessType;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.exception.family.FamilyException;
import com.zlc.family.common.utils.bean.BeanUtils;
import com.zlc.family.common.utils.poi.ExcelUtil;
import com.zlc.family.framework.manager.AsyncManager;
import com.zlc.family.framework.manager.factory.AsyncFactory;
import com.zlc.family.manage.domain.Account;
import com.zlc.family.manage.domain.Bill;
import com.zlc.family.manage.dto.BillDto;
import com.zlc.family.manage.query.BillQuery;
import com.zlc.family.manage.service.IAccountService;
import com.zlc.family.manage.service.IBillService;
import com.zlc.family.manage.vo.BillStatsVo;
import com.zlc.family.manage.vo.BillVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
    private final IAccountService accountService;
    private final RedisCache redisCache;

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
     * 统计账单根据资金流向
     */
    @PreAuthorize("hasPermission('family:bill:list')")
    @GetMapping("/stats/flow")
    public AjaxResult stats(BillQuery query) {
        List<BillStatsVo> list = billService.statsByFlow(query);
        return success(list);
    }

    /**
     * 获取账单详情
     */
    @PreAuthorize("hasPermission('family:bill:query')")
    @GetMapping("/{id}")
    public AjaxResult query(@PathVariable("id") Long id) {
        Bill bill = billService.getById(id);
        if (bill.getAccountId() != null) {
            Account account = accountService.getById(bill.getAccountId());
            bill.setAccountName(account.getName());
        }
        return success(bill);
    }

    /**
     * 新增账单
     */
    @RepeatSubmit
    @PreAuthorize("hasPermission('family:bill:add')")
    @Log(title = "账单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BillDto dto) {
        dto.setBillId(null);
        Bill entity = new Bill(Operator.CREATE);
        BeanUtils.copyBeanProp(entity, dto);
        AjaxResult ajaxResult = toAjax(billService.saveBill(entity));
        String phonenumber = getLoginUser().getUser().getPhonenumber();
        AsyncManager.me().execute(AsyncFactory.notifyAccountAmount(entity.getAccountId(), dto.getAmount(), phonenumber));
        return ajaxResult;
    }

    /**
     * 修改账单
     */
    @RepeatSubmit
    @PreAuthorize("hasPermission('family:bill:edit')")
    @Log(title = "账单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BillDto dto) {
        if (dto.getBillId() == null) {
            throw new FamilyException(FamilyException.Code.BILL_ID_NULL);
        }
        Bill entity = new Bill(Operator.UPDATE);
        BeanUtils.copyBeanProp(entity, dto);
        AjaxResult ajaxResult = toAjax(billService.updateBill(entity));
        String phonenumber = getLoginUser().getUser().getPhonenumber();
        AsyncManager.me().execute(AsyncFactory.notifyAccountAmount(entity.getAccountId(), dto.getAmount(), phonenumber));
        return ajaxResult;
    }

    /**
     * 删除账单
     */
    @PreAuthorize("hasPermission('family:bill:remove')")
    @Log(title = "账单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        AjaxResult ajaxResult = toAjax(billService.removeBill(ids));
        executeTask(AsyncFactory.notifyAccountAmount(getLoginUser().getDeptId()), CacheConstants.getCacheKey(CacheConstants.MSG_NOTIFY_ACCOUNT, getLoginUser().getDeptId()));
        return ajaxResult;
    }

    /**
     * 根据缓存key来异步执行通知任务
     *
     * @param timerTask 具体任务
     * @param cacheKey  缓存的key
     */
    private void executeTask(TimerTask timerTask, String cacheKey) {
        if (!redisCache.hasKey(cacheKey)) {
            AsyncManager.me().execute(timerTask, FamilyConstants.MSG_LOCK_TIME, TimeUnit.MINUTES);
            redisCache.setCacheObject(cacheKey, FamilyConstants.MSG_LOCK_TIME, FamilyConstants.MSG_LOCK_TIME, TimeUnit.MINUTES);
        }
    }

    /**
     * 导出账单管理列表
     */
    @PreAuthorize("hasPermission('family:bill:export')")
    @Log(title = "账单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BillQuery query) {
        List<BillVo> voList = billService.selectBillList(query);
        if (CollectionUtils.isEmpty(voList)) {
            throw new FamilyException(FamilyException.Code.EXPORT_NO_DATA);
        }
        ExcelUtil<BillVo> util = new ExcelUtil<BillVo>(BillVo.class);
        util.exportExcel(response, voList, "账单数据");
    }


    @Log(title = "账单管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("hasPermission('family:bill:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<BillVo> util = new ExcelUtil<BillVo>(BillVo.class);
        List<BillVo> voList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        return toAjax(billService.importBill(voList, updateSupport, operName));
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<BillVo> util = new ExcelUtil<BillVo>(BillVo.class);
        util.importTemplateExcel(response, "账单数据");
    }
}
