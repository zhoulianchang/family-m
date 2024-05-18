package com.zlc.family.web.controller.family;

import com.zlc.family.common.annotation.Log;
import com.zlc.family.common.annotation.RepeatSubmit;
import com.zlc.family.common.core.controller.BaseController;
import com.zlc.family.common.core.domain.AjaxResult;
import com.zlc.family.common.core.page.TableDataInfo;
import com.zlc.family.common.enums.BusinessType;
import com.zlc.family.common.enums.manage.FileType;
import com.zlc.family.common.exception.file.InvalidExtensionException;
import com.zlc.family.common.utils.DateUtils;
import com.zlc.family.common.utils.FamilyUtils;
import com.zlc.family.manage.domain.FamilyFile;
import com.zlc.family.manage.domain.query.FileQuery;
import com.zlc.family.manage.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zlc
 * @date 2024/5/15 12:46
 */
@RestController
@RequestMapping("/family/file")
@RequiredArgsConstructor
public class FileController extends BaseController {
    private final IFileService fileService;

    /**
     * 获取文件列表
     */
    @PreAuthorize("hasPermission('family:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(FileQuery query) {
        startPage();
        return getDataTable(fileService.listFile(query));
    }

    /**
     * 获取文件列表（树状）
     */
    @PreAuthorize("hasPermission('family:file:list')")
    @GetMapping("/tree")
    public AjaxResult tree(FileQuery query) {
        // 树状仅仅支持查询目录
        query.setType(FileType.DIR);
        return AjaxResult.success(FamilyUtils.buildTreeSelect(fileService.listFile(query)));
    }

    /**
     * 新增文件
     */
    @RepeatSubmit
    @PreAuthorize("hasPermission('family:file:add')")
    @Log(title = "文件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam(value = "realFile", required = false) MultipartFile realFile, @Validated FamilyFile file) throws InvalidExtensionException {
        file.setFileId(null);
        file.setDeptId(getDeptId());
        file.setCreateBy(getUsername());
        file.setCreateTime(DateUtils.getNowDate());
        return toAjax(fileService.add(file, realFile));
    }

    /**
     * 更新文件夹
     */
    @RepeatSubmit
    @PreAuthorize("hasPermission('family:file:edit')")
    @Log(title = "文件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated FamilyFile file) {
        file.setDeptId(getDeptId());
        file.setUpdateBy(getUsername());
        file.setUpdateTime(DateUtils.getNowDate());
        return toAjax(fileService.updateFile(file));
    }

    /**
     * 删除文件/文件夹
     */
    @RepeatSubmit
    @PreAuthorize("hasPermission('family:file:remove')")
    @Log(title = "文件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(fileService.removeFile(ids));
    }
}
