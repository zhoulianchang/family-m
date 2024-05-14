package com.zlc.family.common.third.oss;

import com.zlc.family.common.third.oss.impl.MinioC;

/**
 * @author zlc
 * @date 2024/5/13 21:41
 * 创建OssClient的工厂类
 */
public class OssClientFactory {

    public static OssClient create(OssType ossType) {
        switch (ossType) {
            case MINIO:
                return new MinioC();
            default:
                return null;
        }
    }
}
