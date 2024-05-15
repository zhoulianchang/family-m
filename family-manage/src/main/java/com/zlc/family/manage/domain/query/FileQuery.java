package com.zlc.family.manage.domain.query;

import com.zlc.family.common.core.query.BaseQuery;
import com.zlc.family.common.enums.manage.FileType;
import lombok.Data;

/**
 * @author zlc
 * @date 2024/5/15 13:06
 */
@Data
public class FileQuery extends BaseQuery {
    private Long parentId;
    private FileType type;
}
