package com.zlc.family.manage.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlc.family.common.core.domain.BaseEntityFlag;
import com.zlc.family.common.enums.manage.FileType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 文件属性表
 * </p>
 *
 * @author zlc
 * @since 2024-05-15
 */
@Getter
@Setter
@TableName("family_file")
public class FamilyFile extends BaseEntityFlag {

    private static final long serialVersionUID = 1L;

    /**
     * 文件Id
     */
    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    /**
     * 文件类型，枚举。文件和目录
     */
    @NotNull(message = "文件类型不能为空")
    private FileType type;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 所属部门id
     */
    private Long deptId;

    /**
     * 文件扩展信息
     */
    @TableField(exist = false)
    private FileExt fileExt;
}
