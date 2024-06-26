package com.zlc.family.common.exception.job;


import com.zlc.family.common.exception.base.BaseException;

/**
 * @author zlc
 * @date 2024/3/12 17:13
 */
public class DingException extends BaseException {
    private static final long serialVersionUID = 1L;

    public DingException(String code) {
        super("quartz", code, null, null);
    }

    public DingException(String code, Object[] args) {
        super("quartz", code, args, null);
    }
}
