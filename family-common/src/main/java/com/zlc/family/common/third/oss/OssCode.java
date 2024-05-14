package com.zlc.family.common.third.oss;

/**
 * @author zlc
 * @date 2024/5/13 21:32
 */
public enum OssCode {
    SUCCESS(0, "success"),
    IO_ERROR(500, "io error"),
    MINIO_ERROR(10000, "minio sdk error"),
    BUCKET_ERROR(10001, "bucket_error"),
    EXCEED_MAX_SIZE(10002, "The file size exceeds the limit.");
    private int code;
    private String reason;

    OssCode(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
