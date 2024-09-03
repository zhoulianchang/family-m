package com.zlc.family.manage.domain.query;

import com.zlc.family.common.core.query.BaseQuery;
import lombok.Data;

/**
 * @author zlc
 * @date 2024/9/3 19:27
 */
@Data
public class AccountQuery extends BaseQuery {
    /**
     * 是否禁用
     */
    private Boolean enabled;
}
