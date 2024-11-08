package com.zlc.family.framework.utils;

import com.alibaba.fastjson2.JSON;
import com.zlc.family.common.constant.FamilyConstants;
import com.zlc.family.common.exception.family.FamilyException;
import com.zlc.family.common.utils.AssertUtils;
import com.zlc.family.common.utils.StringUtils;
import com.zlc.family.common.utils.spring.SpringUtils;
import com.zlc.family.framework.third.ai.gpt.GPTChatResp;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * okhttp接口请求工具类
 * 整块的核心方法分为两块
 * 一、buildRequest 构造okhttp的Request请求体
 * 二、call 发起接口请求
 *
 * @author zlc
 * @date 2024/7/30 10:24
 */
@Slf4j
public class OkHttpUtils {
    private static final OkHttpClient CLIENT = SpringUtils.getBean(OkHttpClient.class);
    public static final MediaType APPLICATION_JSON = MediaType.get("application/json; charset=utf-8");
    public static final MediaType NORMAL_FORM = MediaType.parse("application/x-www-form-urlencoded");
    public static final MediaType OCTET_STREAM = MediaType.get("application/octet-stream; charset=utf-8");


    /**
     * Send Get
     *
     * @param path    Request path
     * @param headers Request header
     * @return
     */
    public static String get(String path, Map<String, String> headers) {
        Request request = buildRequest(path, FamilyConstants.METHOD_GET, null, null, headers);
        return realCall(request);
    }

    /**
     * Send Get Repeat
     * Until the end identifier of the request is obtained or the maximum number of retries is exceeded
     *
     * @param path        Request path
     * @param headers     Request header
     * @param endMark     End mark
     * @param repeatCount Retries available
     * @param sleepTime   Wait interval Unit s
     * @return
     */
    public static String getRepeat(String path, Map<String, String> headers, String endMark, int repeatCount, long sleepTime) {
        Request request = buildRequest(path, FamilyConstants.METHOD_GET, null, null, headers);
        return repeatCall(request, endMark, repeatCount, sleepTime);
    }

    /**
     * Send Delete
     *
     * @param path    Request path
     * @param headers Request header
     * @return
     */
    public static String delete(String path, Map<String, String> headers) {
        Request request = buildRequest(path, FamilyConstants.METHOD_DELETE, null, null, headers);
        return realCall(request);
    }

    /**
     * Send Post
     *
     * @param path        Request path
     * @param params      Rquest params
     * @param contentType Request content type @see
     * @param headers     Request header
     * @return
     */
    public static String post(String path, String params, String contentType, Map<String, String> headers) {
        Request request = buildRequest(path, FamilyConstants.METHOD_POST, contentType, params, headers);
        return realCall(request);
    }

    /**
     * Send Post For Json
     *
     * @param path    Request path
     * @param params  Rquest paramsjson字符串
     * @param headers Request header
     * @return
     */
    public static String postJson(String path, String params, Map<String, String> headers) {
        Request request = buildRequest(path, FamilyConstants.METHOD_POST, FamilyConstants.CONTENT_TYPE_JSON, params, headers);
        return realCall(request);
    }

    /**
     * Send Post For Json
     *
     * @param path    Request path
     * @param params  Rquest paramsjson字符串
     * @param headers Request header
     * @return
     */
    public static void postStream(String path, String params, Map<String, String> headers, SseEmitter sseEmitter) {
        Request request = buildRequest(path, FamilyConstants.METHOD_POST, FamilyConstants.CONTENT_TYPE_JSON, params, headers);
        realCallStreamAsync(request, sseEmitter);
    }

    /**
     * Send Put
     *
     * @param path        Request path
     * @param params      Rquest params
     * @param contentType Request content type @see
     * @param headers     Request header
     * @return
     */
    public static String put(String path, String params, String contentType, Map<String, String> headers) {
        Request request = buildRequest(path, FamilyConstants.METHOD_PUT, contentType, params, headers);
        return realCall(request);
    }

    /**
     * Send Put Json
     *
     * @param path    Request path
     * @param params  Rquest params
     * @param headers Request header
     * @return
     */
    public static String putJson(String path, String params, Map<String, String> headers) {
        Request request = buildRequest(path, FamilyConstants.METHOD_PUT, FamilyConstants.CONTENT_TYPE_JSON, params, headers);
        return realCall(request);
    }

    /**
     * build okhttp request
     *
     * @param path        Request path
     * @param method      Request mode
     * @param contentType Request content type @see
     * @param params      Rquest params
     * @param headers     Request header
     * @return
     */
    private static Request buildRequest(String path, String method, String contentType, String params, Map<String, String> headers) {
        log.debug("req params is:{},path is:{},headers:{}", params, path, headers);
        RequestBody body = getRequestBody(contentType, params);
        Request.Builder requestBuilder = new Request.Builder()
                .url(path)
                .method(method, body);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!FamilyConstants.METHOD_GET.equals(method)) {
            requestBuilder.addHeader("Content-Type", contentType);
        }
        return requestBuilder.build();
    }

    /**
     * Gets the request body
     *
     * @param contentType Request content type @see
     * @param params      Rquest params
     * @return
     */
    private static RequestBody getRequestBody(String contentType, String params) {
        RequestBody body = null;
        if (contentType == null) {
            return body;
        }
        switch (contentType) {
            case FamilyConstants.CONTENT_TYPE_FORM:
                // 使用 FormBody.Builder 构建请求体
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                if (params != null) {
                    Map<String, String> realParam = JSON.parseObject(params, Map.class);
                    for (Map.Entry<String, String> entry : realParam.entrySet()) {
                        formBodyBuilder.add(entry.getKey(), entry.getValue());
                    }
                    body = formBodyBuilder.build();
                }
                break;
            case FamilyConstants.CONTENT_TYPE_JSON:
                body = RequestBody.create(APPLICATION_JSON, params);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + contentType);
        }
        return body;
    }

    /**
     * Requests can be made repeatedly
     *
     * @param request     okhttp request
     * @param endMark     End mark
     * @param repeatCount Retries available
     * @param sleepTime   Wait interval Unit s
     * @return Interface request content
     */
    private static String repeatCall(Request request, String endMark, int repeatCount, long sleepTime) {
        int i = 0;
        while (i <= repeatCount) {
            String result = realCall(request);
            if (result != null && result.contains(endMark)) {
                return result;
            }
            i++;
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                log.error("sleep error,reason :{}", e.getMessage());
            }
        }
        AssertUtils.isTrue(false, FamilyException.Code.SYS_ERROR, " repeatCall error,please see logs");
        return null;
    }

    /**
     * okhttp client
     * Core calls request methods
     *
     * @param request okhttp request
     * @return String
     */
    private static String realCall(Request request) {
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.body() != null) {
                String result = response.body().string();
                if (response.isSuccessful()) {
                    log.debug("result:{}", result);
                    return result;
                } else {
                    log.error("req error,result:{}", result);
                }
            } else {
                log.error("req error,body is null");
            }
        } catch (IOException e) {
            log.error("io exception:{}", e.getMessage());
        }
        AssertUtils.isTrue(false, FamilyException.Code.SYS_ERROR, "realCall error,please see logs");
        return null;
    }

    /**
     * okhttp client
     * Core calls request methods
     *
     * @param request okhttp request
     */
    private static void realCallStreamAsync(Request request, SseEmitter sseEmitter) {
        CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("io exception:{}", e.getMessage());
                sseEmitter.completeWithError(e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.body() != null) {
                        BufferedSource source = response.body().source();
                        // Keep reading until the source is exhausted
                        while (!source.exhausted()) {
                            String line = source.readUtf8Line();
                            if (StringUtils.isNotEmpty(line)) {
                                log.debug("stream result is:{}", line);
                                if (line.contains(FamilyConstants.AI_END_FLAG)) {
                                    sseEmitter.send(SseEmitter.event().name(FamilyConstants.AI_END_FLAG).data("{}"));
                                    break;
                                }
                                if (line.startsWith("data:")) {
                                    String realContent = line.substring(5).trim();
                                    GPTChatResp gptChatResp = JSON.parseObject(realContent, GPTChatResp.class);
                                    if (CollectionUtils.isEmpty(gptChatResp.getChoices())) {
                                        continue;
                                    }
                                    for (GPTChatResp.Choices choice : gptChatResp.getChoices()) {
                                        if (choice.getDelta() != null && !"stop".equals(choice.getFinishReason())) {
                                            sseEmitter.send(choice.getDelta());
                                        }
                                    }
                                }
                            }
                        }
                        // 完成数据发送后调用 complete 关闭连接
                        sseEmitter.complete();
                    } else {
                        log.error("req error,body is null");
                        sseEmitter.completeWithError(new NullPointerException("Response body is null"));
                    }
                } catch (Exception e) {
                    log.error("exception:{}", e.getMessage());
                    sseEmitter.completeWithError(e);
                } finally {
                    response.close(); // 确保关闭响应
                }
            }
        });
    }

}
