package com.zlc.family.manage.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zlc.family.common.core.vo.BaseTreeVo;
import com.zlc.family.common.enums.manage.FileType;
import com.zlc.family.manage.domain.FileExt;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zlc
 * @date 2024/5/15 13:02
 */
@Getter
@Setter
public class FileVo extends BaseTreeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件Id
     */
    @TableId(value = "file_id", type = IdType.AUTO)
    private Long fileId;

    /**
     * 文件类型，枚举。文件和目录
     */
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
     * 备注
     */
    private String remark;
    private FileExt fileExt;

    public void setFileId(Long fileId) {
        this.fileId = fileId;
        this.setId(fileId);
    }

    public void setName(String name) {
        this.name = name;
        this.setLabel(name);
    }
}
