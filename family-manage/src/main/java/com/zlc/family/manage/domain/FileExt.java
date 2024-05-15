package com.zlc.family.manage.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlc.family.common.enums.manage.FileExtPlace;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文件属性扩展表
 * </p>
 *
 * @author zlc
 * @since 2024-05-15
 */
@Getter
@Setter
@TableName("family_file_ext")
public class FileExt {

    private static final long serialVersionUID = 1L;

    /**
     * family_file表的主键id
     */
    @TableId
    private Long fileId;

    /**
     * 文件的云存储路径（不含域名）
     */
    private String filePath;

    /**
     * 文件存储位置
     */
    private FileExtPlace place;

    /**
     * 文件大小,单位MB
     */
    private Float fileSize;

    /**
     * 文件类型
     */
    private String fileType;
}
