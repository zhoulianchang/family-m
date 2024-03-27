package com.zlc.family.web.controller.family;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zlc.family.common.annotation.Log;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.core.controller.BaseController;
import com.zlc.family.common.core.domain.AjaxResult;
import com.zlc.family.common.core.page.TableDataInfo;
import com.zlc.family.common.enums.BusinessType;
import com.zlc.family.common.enums.Operator;
import com.zlc.family.common.exception.family.FamilyException;
import com.zlc.family.common.utils.StringUtils;
import com.zlc.family.manage.domain.Favor;
import com.zlc.family.manage.query.FavorQuery;
import com.zlc.family.manage.service.IFavorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zlc
 * @since 2024-03-27
 */
@RestController
@RequestMapping("/family/favor")
@RequiredArgsConstructor
public class FavorController extends BaseController {
    private final IFavorService favorService;


    /**
     * 获取人情账薄列表
     */
    @PreAuthorize("hasPermission('family:favor:list')")
    @GetMapping("/list")
    public TableDataInfo list(FavorQuery query) {
        startPage();
        QueryWrapper<Favor> qw = new QueryWrapper<>();
        qw.lambda().eq(Favor::getDelFlag, FamilyConstants.DEL_NO)
                .eq(query.getBalanced() != null, Favor::getBalanced, query.getBalanced())
                .like(StringUtils.isNotEmpty(query.getUserNameLike()), Favor::getUserName, query.getUserNameLike());
        Optional.ofNullable(query.getParams().get("beginTime"))
                .map(String::valueOf)
                .ifPresent(beginTime -> qw.lambda().ge(Favor::getFavorTime, beginTime));
        Optional.ofNullable(query.getParams().get("endTime"))
                .map(String::valueOf)
                .ifPresent(endTime -> qw.lambda().le(Favor::getFavorTime, endTime));
        List<Favor> list = favorService.list(qw);
        return getDataTable(list);
    }

    /**
     * 获取人情账薄列表
     */
    @PreAuthorize("hasPermission('family:favor:query')")
    @GetMapping("/{id}")
    public AjaxResult list(@PathVariable("id") Long id) {
        return success(favorService.getById(id));
    }

    /**
     * 新增人情账薄
     */
    @PreAuthorize("hasPermission('family:favor:add')")
    @Log(title = "人情账薄", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody Favor favor) {
        favor.init(Operator.CREATE);
        return toAjax(favorService.saveFavor(favor));
    }

    /**
     * 修改人情账薄
     */
    @PreAuthorize("hasPermission('family:favor:edit')")
    @Log(title = "人情账薄", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody Favor favor) {
        if (favor.getFavorId() == null) {
            throw new FamilyException(FamilyException.Code.FAVOR_ID_NULL);
        }
        favor.init(Operator.UPDATE);
        return toAjax(favorService.updateFavor(favor));
    }

    /**
     * 删除人情账薄
     */
    @PreAuthorize("hasPermission('family:favor:remove')")
    @Log(title = "人情账薄", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(favorService.update(new UpdateWrapper<Favor>().lambda().set(Favor::getDelFlag, FamilyConstants.DEL_YES).in(Favor::getFavorId, ids)));
    }
}
