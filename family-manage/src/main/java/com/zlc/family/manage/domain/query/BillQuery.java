package com.zlc.family.manage.domain.query;

import com.zlc.family.common.core.query.BaseQuery;
import lombok.Data;

/**
 * @author zlc
 * @date 2024/3/24 21:35
 */
@Data
public class BillQuery extends BaseQuery {
    /**
     * 消费用户
     */
    private String userName;
    /**
     * 扣款账户
     */
    private Long accountId;
    /**
     * 资金流向
     */
    private Integer flow;
    /**
     * 分类
     */
    private Integer type;
}
