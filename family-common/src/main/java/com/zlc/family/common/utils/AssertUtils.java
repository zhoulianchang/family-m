package com.zlc.family.common.utils;

import com.zlc.family.common.exception.family.FamilyException;

/**
 * @author zlc
 * @date 2024/5/15 16:25
 * 判断异常工具类，以FamilyException为主
 */
public class AssertUtils {
    public static void isNull(Object o, FamilyException.Code code, Object... args) {
        if (StringUtils.isNull(o)) {
            throw new FamilyException(code, args);
        }
        return;
    }

    public static void isNotNull(Object o, FamilyException.Code code, Object... args) {
        if (StringUtils.isNotNull(o)) {
            throw new FamilyException(code, args);
        }
        return;
    }

    public static void isEmpty(String str, FamilyException.Code code, Object... args) {
        if (StringUtils.isEmpty(str)) {
            throw new FamilyException(code, args);
        }
        return;
    }

    public static void isTrue(Boolean result, FamilyException.Code code, Object... args) {
        if (result) {
            throw new FamilyException(code, args);
        }
        return;
    }

    public static void isFalse(Boolean result, FamilyException.Code code, Object... args) {
        if (!result) {
            throw new FamilyException(code, args);
        }
        return;
    }
}
