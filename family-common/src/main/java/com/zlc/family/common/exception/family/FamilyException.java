package com.zlc.family.common.exception.family;


import com.zlc.family.common.exception.base.BaseException;

/**
 * @author zlc
 * @date 2024/3/12 17:13
 */
public class FamilyException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FamilyException(Code code, Object[] args) {
        super("family", code.getCode(), args, null);
    }

    public FamilyException(Code code) {
        super("family", code.getCode(), null, null);
    }

    public enum Code {
        BILL_ID_NULL("bill.id.null"),
        BILL_ID_EXISTS("bill.id.exists"),
        ;
        private String code;

        Code(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
