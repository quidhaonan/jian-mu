package com.lmyxlf.jian_mu.global.util;

import com.lmyxlf.jian_mu.global.model.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description Http 请求工具类
 * @since 17
 */
@Slf4j
public class HttpClientUtils {

    private static final HttpClient HTTP_CLIENT;
    private static final HttpClientManager HTTP_CLIENT_MANAGER;

    static {
        // 配置连接池
        HTTP_CLIENT_MANAGER = new HttpClientManager();
        // 最大连接
        HTTP_CLIENT_MANAGER.setMaxTotal(200);
        // 默认的每个路由的最大连接数
        HTTP_CLIENT_MANAGER.setDefaultMaxPerRoute(100);
        // 超过多长时间检查旧的连接是否存活
        HTTP_CLIENT_MANAGER.setValidateAfterInactivity(2000);

        // socket 配置
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(false)     // 是否立即发送数据，设置为 true 会关闭 Socket 缓冲，默认为 false
//                .setSoReuseAddress(true) // 是否可以在一个进程关闭 Socket 后，即使它还没有释放端口，其它进程还可以立即重用端口
//                .setSoTimeout(500)       // 接收数据的等待超时时间，单位 ms
//                .setSoLinger(6)         // 关闭 Socket 时，要么发送完所有数据，要么等待 60s 后，就关闭连接，此时 socket.close() 是阻塞的
//                .setSoKeepAlive(true)    // 开启监视 TCP 连接是否有效
                .build();
        HTTP_CLIENT_MANAGER.setDefaultSocketConfig(socketConfig);

        // 配置请求的超时设置
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)         // 连接超时时间
                .setSocketTimeout(5 * 1000)          // 读超时时间（等待数据超时时间）
                .setConnectionRequestTimeout(-1)    // 从池中获取连接超时时间
                .build();

        //自定义重试策略
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            if (executionCount >= 3) {// 如果已经重试了 3 次，就放弃
                return false;
            }
            if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                return true;
            }
            if (exception instanceof SSLHandshakeException) {// 不要重试 SSL 握手异常
                return false;
            }
            if (exception instanceof InterruptedIOException) {// 超时
                return false;
            }
            if (exception instanceof UnknownHostException) {// 目标服务器不可达
                return false;
            }
            if (exception instanceof SSLException) {// SSL 握手异常
                return false;
            }
            HttpClientContext clientContext = HttpClientContext
                    .adapt(context);
            HttpRequest request = clientContext.getRequest();
            // Retry if the request is considered idempotent
            // 如果请求类型不是 HttpEntityEnclosingRequest，被认为是幂等的，那么就重试
            // HttpEntityEnclosingRequest 指的是有请求体的 request，比 HttpRequest 多一个 Entity 属性
            // 而常用的 GET 请求是没有请求体的，POST、PUT 都是有请求体的
            // Rest 一般用 GET 请求获取数据，故幂等，POST 用于新增数据，故不幂等
            return !(request instanceof HttpEntityEnclosingRequest);
        };

        HTTP_CLIENT = HttpClients.custom()
                .setConnectionManager(HTTP_CLIENT_MANAGER)
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultRequestConfig(config)
                //.setRedirectStrategy(new LaxRedirectStrategy())
                .evictIdleConnections(30, TimeUnit.SECONDS) // 定期清理过期连接与空闲连接
                .build();
    }

    public static HttpClient getHttpClient() {
        return HTTP_CLIENT;
    }

    public static PoolingHttpClientConnectionManager getConnectManager() {
        return HTTP_CLIENT_MANAGER;
    }

    /**
     * get请求
     * @param url 请求地址
     * @param params 请求参数
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get请求
     * @param url 请求地址
     * @param queryParams 请求参数
     * @param headParams head参数
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult doGet(String url, Map<String, String> queryParams, Map<String, String> headParams) {
        long t1 = System.currentTimeMillis();
        // 返回结果
        HttpResult result = new HttpResult();
        HttpGet httpGet = null;
        try {
            // 拼接参数,可以用 URIBuilder,也可以直接拼接在 ？ 传值，拼在 url 后面，如下 --httpGet = new
            // HttpGet(uri+"?id=123");
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != queryParams && !queryParams.isEmpty()) {
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = uriBuilder.build();
            // 创建 get 请求
            httpGet = new HttpGet(uri);
            // 添加请求头
            if (headParams != null && !headParams.isEmpty()) {
                for (Map.Entry<String, String> entry : headParams.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            log.debug("访问路径：" + uri);
            HttpResponse response = HTTP_CLIENT.execute(httpGet);
            long duration = System.currentTimeMillis() - t1;
            // Monitor.info("http_ret").tag("host", uri.getHost()).tag("path", uri.getRawPath()).time(duration);
            // 结果返回
            String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            int statusCode = response.getStatusLine().getStatusCode();
            result.setResult(content).setStatusCode(statusCode).setHeaders(response.getAllHeaders());
            log.debug("返回数据：{} ms >>>{}", (System.currentTimeMillis() - t1), result);
        } catch (Exception e) {
            log.error("请求 url：{}失败! {}", url, ExceptionUtils.getStackTrace(e));
        } finally {
            // 释放连接
            if (null != httpGet) {
                httpGet.releaseConnection();
            }
        }
        return result;
    }

    /**
     * post请求
     * @param url url地址
     * @param params 请求参数
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult doPost(String url, Map<String, String> params) {
        return doPost(url, params, null);
    }

    /**
     * post请求
     * @param url url地址
     * @param queryParams 请求参数
     * @param headParams head参数
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult doPost(String url, Map<String, String> queryParams, Map<String, String> headParams) {
        long t1 = System.currentTimeMillis();
        URI uri = URI.create(url);
        HttpResult result = new HttpResult();
        HttpPost httpPost = new HttpPost(uri);
        try { // 参数键值对
            if (null != queryParams && !queryParams.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<>();
                NameValuePair pair = null;
                for (String key : queryParams.keySet()) {
                    pair = new BasicNameValuePair(key, queryParams.get(key));
                    pairs.add(pair);
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(entity);
            }
            // 添加请求头
            if (headParams != null && !headParams.isEmpty()) {
                for (Map.Entry<String, String> entry : headParams.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = HTTP_CLIENT.execute(httpPost);
            long duration = System.currentTimeMillis() - t1;
            // Monitor.info("http_ret").tag("host", uri.getHost()).tag("path", uri.getRawPath()).time(duration);
            int statusCode = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            result.setResult(content).setStatusCode(statusCode).setHeaders(response.getAllHeaders());
            log.debug("响应时间: {}, 返回数据：>>>{}" , System.currentTimeMillis() - t1, result);
        } catch (Exception e) {
            // Monitor.error("http_req_error")
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .log("请求url：{}失败! {}", uri, ExceptionUtils.getStackTrace(e))
            //         .inc();
            log.error("请求 url：{}失败! {}", uri, ExceptionUtils.getStackTrace(e));
        } finally {
            // 释放连接
            httpPost.releaseConnection();
        }
        return result;
    }

    /**
     * Post请求发送 json 数据
     * @param url 请求地址
     * @param params json 数据
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult sendJsonStr(String url, String params) {
        return sendJsonStr(url, params, null);
    }

    /**
     * Post请求发送 json 数据
     * @param url 请求地址
     * @param centent json 数据
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult sendJsonStr(String url, String centent, Map<String, String> headParams) {
        return sendStr(url, "application/json", centent, headParams);
    }

    /**
     * Post 请求发送 xml 数据
     * @param url 请求地址
     * @param params xml 数据
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult sendXmlStr(String url, String params) {
        return sendXmlStr(url, params, null);
    }

    /**
     * Post 请求发送 xml 数据
     * @param url 请求地址
     * @param centent xml 数据
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult sendXmlStr(String url, String centent, Map<String, String> headParams) {
        return sendStr(url, "text/xml", centent, headParams);
    }

    /**
     * Post 请求发送字符串数据
     * @param url 请求地址
     * @param centent 字符串数据
     * @param headParams head 参数
     * @return HttpResult {@link HttpResult}
     */
    public static HttpResult sendStr(String url, String contentType , String centent, Map<String, String> headParams) {
        long t1 = System.currentTimeMillis();
        URI uri = URI.create(url);
        HttpResult result = new HttpResult();
        HttpPost httpPost = new HttpPost(uri);
        try {
            httpPost.addHeader("Content-type", contentType + "; charset=utf-8");
            httpPost.setHeader("Accept", contentType);
            if (StringUtils.isNotBlank(centent)) {
                httpPost.setEntity(new StringEntity(centent, StandardCharsets.UTF_8));
            }
            // 添加请求头
            if (headParams != null && !headParams.isEmpty()) {
                for (Map.Entry<String, String> entry : headParams.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            log.debug("访问路径：" + url);
            HttpResponse response = HTTP_CLIENT.execute(httpPost);
            long duration = System.currentTimeMillis() - t1;
            // Monitor.info("http_ret").tag("host", uri.getHost()).tag("path", uri.getRawPath()).time(duration);
            int statusCode = response.getStatusLine().getStatusCode();
            String content = EntityUtils.toString(response.getEntity());
            result.setStatusCode(statusCode).setResult(content);
            log.debug("返回数据：" + result);
        } catch (IOException e) {
            // Monitor.error("http_req_error")
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .log("请求url：{}失败! {}", uri, ExceptionUtils.getStackTrace(e))
            //         .inc();
            log.error("请求 url：{}失败! {}", uri, ExceptionUtils.getStackTrace(e));
        } finally {
            httpPost.releaseConnection();
        }
        return result;
    }

    /**
     * get请求
     * @param url 请求地址
     * @param queryParams 请求参数
     * @param headParams head参数
     * @return Header[]
     */
    public static Header[] doGetStream(String url, Map<String, String> queryParams, Map<String, String> headParams, OutputStream outputStream) {
        long t1 = System.currentTimeMillis();
        Header[] allHeaders = null;
        HttpGet httpGet = null;
        URI uri = null;
        try {
            // 拼接参数,可以用 URIBuilder,也可以直接拼接在 ？ 传值，拼在 url 后面，如下 --httpGet = new
            // HttpGet(uri+"?id=123");
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != queryParams && !queryParams.isEmpty()) {
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            // 创建 get 请求
            httpGet = new HttpGet(uri);
            // 添加请求头
            if (headParams != null && !headParams.isEmpty()) {
                for (Map.Entry<String, String> entry : headParams.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            log.debug("访问路径：" + uri);
            HttpResponse response = HTTP_CLIENT.execute(httpGet);
            long duration = System.currentTimeMillis() - t1;
            // Monitor.info("http_ret").tag("host", uri.getHost()).tag("path", uri.getRawPath()).time(duration);
            response.getEntity().writeTo(outputStream);
            allHeaders = response.getAllHeaders();
        } catch (Exception e) {
            // Monitor.error("http_req_error")
            //         .tag("host", uri == null ? "" : uri.getHost())
            //         .tag("path", uri == null ? "" : uri.getRawPath())
            //         .log("请求url：{}失败! {}", uri, ExceptionUtils.getStackTrace(e))
            //         .inc();
            log.error("请求 url：{}失败! {}", uri, ExceptionUtils.getStackTrace(e));
        } finally {
            // 释放连接
            if (null != httpGet) {
                httpGet.releaseConnection();
            }
        }
        return allHeaders;
    }
}