package com.zlc.family.common.third.oss;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zlc
 * @date 2021/10/9 4:46 下午
 */
@Getter
@Setter
@ToString
public class OssResult {
    /**
     * 状态码 0表示成功
     */
    private int code;

    /**
     * 文件路径（如果使用自定义文件路径，会返回完整的路径+文件名，例：cf/abc.png）
     */
    private String filePath;

    /**
     * 上传成功的返回url（带过期时间）
     */
    private String url;

    /**
     * 提示信息
     */
    private String msg;

    public OssResult() {
    }

    public OssResult(int code, String filePath, String url, String msg) {
        this.code = code;
        this.filePath = filePath;
        this.url = url;
        this.msg = msg;
    }

    public static OssResult ok() {
        return new OssResult(OssCode.SUCCESS.getCode(), null, null, null);
    }

    public static OssResult ok(String filePath, String url) {
        return new OssResult(OssCode.SUCCESS.getCode(), filePath, url, null);
    }


    public static OssResult fail(OssCode ossCode) {
        return fail(ossCode.getCode(), ossCode.getReason());
    }

    public static OssResult fail(int code, String msg) {
        return new OssResult(code, null, null, msg);
    }
}
