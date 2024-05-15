package com.zlc.family.common.third.oss;

import com.zlc.family.common.third.oss.impl.MinioC;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zlc
 * @date 2024/5/13 21:41
 * 创建OssClient的工厂类
 */
public class OssClientFactory {
    private static final ConcurrentHashMap<OssType, OssClient> clientMap = new ConcurrentHashMap<>();

    public static OssClient create(OssType ossType) {
        if (ossType == null) {
            throw new IllegalArgumentException("OssType cannot be null");
        }
        return clientMap.computeIfAbsent(ossType, type -> {
            switch (type) {
                case MINIO:
                    return new MinioC();
                default:
                    throw new UnsupportedOperationException("Unsupported OssType: " + type);
            }
        });
    }
}