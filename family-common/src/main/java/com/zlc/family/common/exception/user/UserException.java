package com.zlc.family.common.exception.user;

import com.zlc.family.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author family
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
