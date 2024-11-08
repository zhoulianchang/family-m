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
        ID_EXISTS("id.exists"),
        ID_NULL("id.null"),
        DATA_ERROR("data.error"),
        SYS_ERROR("system.error"),
        EXPORT_NO_DATA("export.no.data"),
        FAVOR_ID_NULL("favor.id.null"),
        FAVOR_ID_ERROR("favor.id.error"),
        FAVOR_RELATION_ID_ERROR("favor.relation.id.error"),
        BILL_ID_NULL("bill.id.null"),
        BILL_ID_EXISTS("bill.id.exists"),
        FILE_ONLY_HANG_DIR("file.only.hang.dir"),
        FILE_NAME_REPEAT("file.name.repeat"),
        FILE_UPLOAD_ERROR("file.upload.error"),
        FILE_NAME_EMPTY("file.name.empty"),
        FILE_NAME_LENGTH("file.name.length"),
        FILE_REMOVE_HAVE_CHILDREN("file.remove.have.children"),
        FILE_REMOVE_ERROR("file.remove.error"),
        FILE_NOT_EXISTS("file.not.exists"),
        FILE_UPDATE_NOT_SUPPORT_TYPE("file.update.not.support.type"),
        FILE_REAL_NOT_UPLOAD("file.real.not.upload"),
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
