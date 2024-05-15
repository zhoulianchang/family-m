package com.zlc.family.manage.domain.query;

import com.zlc.family.common.core.query.BaseQuery;
import lombok.Data;

/**
 * @author zlc
 * @date 2024/3/27 20:49
 */
@Data
public class FavorQuery extends BaseQuery {
    /**
     * 是否平账 1不需要 2已平账 3未平账
     */
    private Integer balanced;
    /**
     * 资金流向
     */
    private Integer flow;

    /**
     * 送/被送礼人
     */
    private String userNameLike;
    /**
     * 备注
     */
    private String remarkLike;
}
