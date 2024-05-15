package com.zlc.family.web.controller;

import com.zlc.family.common.core.controller.BaseController;
import com.zlc.family.common.core.domain.AjaxResult;
import com.zlc.family.common.core.query.BaseQuery;
import com.zlc.family.common.core.vo.EchartPieVo;
import com.zlc.family.manage.domain.query.BillStatsQuery;
import com.zlc.family.manage.service.IAccountService;
import com.zlc.family.manage.service.IBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zlc
 * @date 2024/3/29 20:24
 */
@RestController
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexController extends BaseController {
    private final IBillService billService;
    private final IAccountService accountService;

    /**
     * 统计收入支出账单根据分类
     */
    @PreAuthorize("hasPermission('family:bill:list')")
    @GetMapping("/stats/bill/by/type")
    public AjaxResult statsByType(BillStatsQuery query) {
        Map<String, List<EchartPieVo>> result = billService.statsByType(query);
        return success(result);
    }

    /**
     * 统计支出账单根据用户
     */
    @PreAuthorize("hasPermission('family:bill:list')")
    @GetMapping("/stats/bill/by/user")
    public AjaxResult statsByUser(BillStatsQuery query) {
        List<EchartPieVo> result = billService.statsByUserName(query);
        return success(result);
    }

    /**
     * 查询账户余额
     */
    @PreAuthorize("hasPermission('family:account:list')")
    @GetMapping("/stats/account/balance")
    public AjaxResult statsAccountBalance(BaseQuery query) {
        return success(accountService.statsAccountBalance(query));
    }
}
