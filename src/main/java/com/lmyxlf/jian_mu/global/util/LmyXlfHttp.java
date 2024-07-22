package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.exception.LmyXlfIOException;
import com.lmyxlf.jian_mu.global.model.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description
 * @since 17
 */
@Slf4j
public class LmyXlfHttp {


    private final HttpRequestBase request;

    LmyXlfHttp(HttpRequestBase request) {
        this.request = request;
    }

    public static void printManagerStatus() {
        PoolingHttpClientConnectionManager connectManager = HttpClientUtils.getConnectManager();
        // 获取每个路由的状态
        Set<HttpRoute> routes = connectManager.getRoutes();
        routes.forEach(e -> {
            PoolStats stats = connectManager.getStats(e);
            log.info("Per route: {} {}", e, stats);
        });
        // 获取所有路由的连接池状态
//        PoolStats totalStats = connectManager.getTotalStats();
//        log.info("Total status: {} ",totalStats);
    }

    private HttpResponse execute() {
        long t1 = System.currentTimeMillis();
        URI uri = request.getURI();

        try {
            HttpResponse response = HttpClientUtils.getHttpClient().execute(request);
            long duration = System.currentTimeMillis() - t1;
            // Monitor.info("http_ret")
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .time(duration);
            if (log.isDebugEnabled()) {
                log.debug("LmyXlf_HTTP: {}, {}", uri, duration);
            }
            return response;
        } catch (IOException e) {
            this.releaseConnection();
            // Monitor.error("http_req_error")
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .log("请求url：{}失败! {}, 异常出现间隔:{}", uri, ExceptionUtils.getStackTrace(e), System.currentTimeMillis()-t1)
            //         .inc();
            log.error("请求 url：{}失败! {}, 异常出现间隔:{}", uri, ExceptionUtils.getStackTrace(e), System.currentTimeMillis() - t1);
            throw new LmyXlfException("http 请求异常: " + uri);
        }
    }

    public HttpResponse streamExecute() {
        return this.execute();
    }

    /**
     * 返回以响应头 byte[] 格式填充 HttpResult 类
     * 数据位于 bytes 字段
     * 只适用于小数据响应头返回，如果有下载文件等需要，请使用 execute() 方法
     *
     * @return
     */
    public HttpResult bodyBytes() {
        HttpResponse execute = execute();
        InputStream content = null;
        try {
            content = execute.getEntity().getContent();
            Header[] allHeaders = execute.getAllHeaders();
            int statusCode = execute.getStatusLine().getStatusCode();
            byte[] bytes = IoUtil.readBytes(content);
            return new HttpResult()
                    .setHeaders(allHeaders)
                    .setStatusCode(statusCode)
                    .setBytes(bytes);
        } catch (IOException e) {
            URI uri = request.getURI();
            // Monitor.error("http_req_error")
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .log("读取URI:{}输入流失败! {}", uri, ExceptionUtils.getStackTrace(e))
            //         .inc();
            log.error("读取{}输入流失败", request.getURI());
            throw new LmyXlfIOException(e, StrUtil.format("读取 URI:{} IO 流失败", uri));
        } finally {
            this.releaseConnection();
        }
    }


    public HttpResult exchange() {

        HttpResult result = new HttpResult();
        HttpResponse response = execute();
        String content = exchange(response);
        result.setStatusCode(response.getStatusLine().getStatusCode()).setResult(content);
        result.setHeaders(response.getAllHeaders());
        return result;
    }

    public String exchange(HttpResponse response) {
        URI uri = request.getURI();
        String content = null;
        try {
            content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Monitor.error("http_body_read_error")
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .log("解析请求结果失败! url：{} {}", uri, ExceptionUtils.getStackTrace(e))
            //         .inc();
            log.error("解析请求结果失败! url：{} {}", uri, ExceptionUtils.getStackTrace(e));
            throw new LmyXlfException("http 请求异常: " + uri);
        } finally {
            this.releaseConnection();
        }
        return content;
    }


    public <T> T json(Class<T> tClass) {
        HttpResult result = this.exchange();
        if (result.getStatusCode() == HttpStatus.SC_OK) {
            return JSON.parseObject(result.getResult(), tClass);
        } else {
            URI uri = this.request.getURI();
            // Monitor.error("http_no_200")
            //         .tag("status", result.getStatusCode().toString())
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .log("http请求异常: {} : {}", result.getStatusCode(), result.getResult())
            //         .inc();
            log.error("http 请求异常: {} : {}", result.getStatusCode(), result.getResult());
            throw new LmyXlfException("http 请求异常: " + result.getStatusCode() + ": " + result.getResult());
        }
    }

    public <T> T json(TypeReference<T> tClass) {
        HttpResult result = this.exchange();
        if (result.getStatusCode() == HttpStatus.SC_OK) {
            return JSON.parseObject(result.getResult(), tClass);
        } else {
            URI uri = this.request.getURI();
            // Monitor.error("http_no_200")
            //         .tag("status", result.getStatusCode().toString())
            //         .tag("host", uri.getHost())
            //         .tag("path", uri.getRawPath())
            //         .log("http请求异常: {} : {}", result.getStatusCode(), result.getResult())
            //         .inc();
            log.error("http 请求异常: {} : {}", result.getStatusCode(), result.getResult());
            throw new LmyXlfException("http 请求异常: " + result.getStatusCode() + ": " + result.getResult());
        }
    }

    public HttpRequestBase getRequest() {
        return request;
    }

    public void releaseConnection() {
        request.releaseConnection();
    }

    private static enum Method {
        /**
         * http请求方法
         */
        GET, POST, DELETE, PUT;
    }

    /**
     * 创建一个 http 请求 builder 类
     *
     * @return LmyXlfHttpBuilder
     */
    public static LmyXlfHttpBuilder builder() {
        return new LmyXlfHttpBuilder();
    }

    /**
     * 创建一个 get 请求 builder 类
     *
     * @return LmyXlfHttpBuilder
     */
    public static LmyXlfHttpBuilder get(String url) {
        return new LmyXlfHttpBuilder().get().url(url);
    }

    /**
     * 创建一个 post 请求 builder 类
     *
     * @return LmyXlfHttpBuilder
     */
    public static LmyXlfHttpBuilder post(String url) {
        return new LmyXlfHttpBuilder().post().url(url);
    }

    /**
     * 创建一个 put 请求 builder 类
     *
     * @return LmyXlfHttpBuilder
     */
    public static LmyXlfHttpBuilder put(String url) {
        return new LmyXlfHttpBuilder().put().url(url);
    }

    /**
     * 创建一个 delete 请求 builder 类
     *
     * @return LmyXlfHttpBuilder
     */
    public static LmyXlfHttpBuilder delete(String url) {
        return new LmyXlfHttpBuilder().delete().url(url);
    }

    @SuppressWarnings("unchecked")
    public static class LmyXlfHttpBuilder {
        /**
         * 请求 url
         */
        private String url;
        /**
         * url 参数
         */
        private Map<String, Object> urlParams;

        private byte[] byteArrayBody;

        /**
         * 请求头
         */
        private Map<String, Object> header;
        /**
         * 请求体，默认的 content-type 为 ”application/x-www-form-urlencoded“
         * 如果同时设置了 multipart 时，content-type 会设置为 ”text/plain“ 的键值对形式
         */
        private Map<String, Object> body;
        /**
         * 请求体，默认为 form-data 类型
         * 如果被设置了，请求体优先使用顺序：multipart >= body > json > xml > json
         */
        private Map<String, Object> multipart;
        /**
         * 请求超时时间，默认为 5 秒
         */
        private Integer timeout;
        /**
         * 请求方法
         * 如果被设置了，请求体优先使用顺序：multipart >= body > json > xml > json
         */
        private Method method;
        /**
         * 请求体 json
         * 如果被设置了，请求体优先使用顺序：multipart >= body > json > xml > json
         */
        private Object json;
        /**
         * 请求体 xml
         * 如果被设置了，请求体优先使用顺序：multipart >= body > json > xml > json
         */
        private String xml;
        /**
         * 请求体 text
         * 如果被设置了，请求体优先使用顺序：multipart >= body > json > xml > json
         */
        private String text;
        /**
         * 请求体的类型
         */
        private String contentType;

        LmyXlfHttpBuilder() {
        }

        /**
         * 设置请求 url
         *
         * @param url 请求地址
         * @return LmyXlfHttpBuilder
         */
        public LmyXlfHttpBuilder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置 url 参数
         *
         * @param urlParams url 参数
         * @return LmyXlfHttpBuilder
         */
        public LmyXlfHttpBuilder urlParams(Map<String, ?> urlParams) {
            if (null != this.urlParams) {
                this.urlParams.putAll(urlParams);
            } else {
                this.urlParams = (Map<String, Object>) urlParams;
            }
            return this;
        }

        /**
         * 打一个 tag 监控
         */
        public LmyXlfHttpBuilder tag(String thirdKey) {
            // Monitor.info("third_key_used").tag("thirdkey",thirdKey);
            return this;
        }


        /**
         * 设置 url 参数
         *
         * @param key   参数名
         * @param value 参数值
         * @return LmyXlfHttpBuilder
         */
        public LmyXlfHttpBuilder urlParams(String key, Object value) {
            if (null == this.urlParams) {
                this.urlParams = new HashMap<>();
            }
            this.urlParams.put(key, value);
            return this;
        }

        public LmyXlfHttpBuilder header(Map<String, ?> header) {
            if (null != this.header) {
                this.header.putAll(header);
            } else {
                this.header = (Map<String, Object>) header;
            }
            return this;
        }

        public LmyXlfHttpBuilder header(String key, Object value) {
            if (null == this.header) {
                this.header = new HashMap<>();
            }
            this.header.put(key, value);
            return this;
        }

        /**
         * 伪装 ip
         *
         * @return
         */
        public LmyXlfHttpBuilder proxy() {
            if (null == this.header) {
                this.header = new HashMap<>();
            }
            this.header.put(LmyXlfReqParamConstant.KEY_X_FORWARDED_FOR, RandomUtil.generateRandomIp());
            return this;
        }

        public LmyXlfHttpBuilder body(Map<String, ?> body) {
            if (null != this.body) {
                this.body.putAll(body);
            } else {
                this.body = (Map<String, Object>) body;
            }
            return this;
        }

        public LmyXlfHttpBuilder body(String key, Object value) {
            if (null == this.body) {
                this.body = new HashMap<>();
            }
            this.body.put(key, value);
            return this;
        }

        public LmyXlfHttpBuilder body(byte[] bytes) {
            this.byteArrayBody = bytes;
            return this;
        }

        public LmyXlfHttpBuilder multipart(Map<String, ?> multipart) {
            if (null != this.multipart) {
                this.multipart.putAll(multipart);
            } else {
                this.multipart = (Map<String, Object>) multipart;
            }
            return this;
        }

        public LmyXlfHttpBuilder multipart(String key, byte[] value) {
            if (null == this.multipart) {
                this.multipart = new HashMap<>();
            }
            this.multipart.put(key, value);
            return this;
        }

        public LmyXlfHttpBuilder multipart(String key, File value) {
            if (null == this.multipart) {
                this.multipart = new HashMap<>();
            }
            this.multipart.put(key, value);
            return this;
        }

        public LmyXlfHttpBuilder multipart(String key, InputStream value) {
            if (null == this.multipart) {
                this.multipart = new HashMap<>();
            }
            this.multipart.put(key, value);
            return this;
        }

        public LmyXlfHttpBuilder multipart(String key, String value) {
            if (null == this.multipart) {
                this.multipart = new HashMap<>();
            }
            this.multipart.put(key, value);
            return this;
        }

        /**
         * 设置 http 请求超时时间
         *
         * @param timeout 毫秒（默认为 5000 毫秒）
         * @return LmyXlfHttpBuilder
         */
        public LmyXlfHttpBuilder timeout(Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        public LmyXlfHttpBuilder get() {
            this.method = Method.GET;
            return this;
        }

        public LmyXlfHttpBuilder post() {
            this.method = Method.POST;
            return this;
        }

        public LmyXlfHttpBuilder delete() {
            this.method = Method.DELETE;
            return this;
        }

        public LmyXlfHttpBuilder put() {
            this.method = Method.PUT;
            return this;
        }

        public LmyXlfHttpBuilder json(Object json) {
            this.json = json;
            return this;
        }

        public LmyXlfHttpBuilder xml(String xml) {
            this.xml = xml;
            return this;
        }

        public LmyXlfHttpBuilder text(String text) {
            this.text = text;
            return this;
        }

        public LmyXlfHttpBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public LmyXlfHttp build() {
            HttpRequestBase request;
            URIBuilder uriBuilder = null;
            URI uri;
            try {
                uriBuilder = new URIBuilder(url);
                if (null != this.urlParams && !this.urlParams.isEmpty()) {
                    for (Map.Entry<String, Object> entry : urlParams.entrySet()) {
                        uriBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
                uri = uriBuilder.build();
            } catch (URISyntaxException e) {
                // Monitor.error("url_build_fail").log("url：{}构建失败! {}", url, ExceptionUtils.getStackTrace(e)).inc();
                log.error("url：{}构建失败! {}", url, ExceptionUtils.getStackTrace(e));
                throw new LmyXlfException("url：" + url + "构建失败!");
            }
            switch (this.method) {
                case POST:
                    HttpPost httpPost = new HttpPost(uri);
                    addBody(httpPost);
                    request = httpPost;
                    break;
                case PUT:
                    HttpPut httpPut = new HttpPut(uri);
                    addBody(httpPut);
                    request = httpPut;
                    break;
                case DELETE:
                    request = new HttpDelete(uri);
                    break;
                default:
                    request = new HttpGet(uri);
            }
            if (null != this.header && !this.header.isEmpty()) {
                for (Map.Entry<String, Object> entry : this.header.entrySet()) {
                    request.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            if (StringUtils.isNotBlank(this.contentType)) {
                request.setHeader(HTTP.CONTENT_TYPE, this.contentType);
            }
            if (this.timeout == null || this.timeout <= 0) {
                this.timeout = 5 * 1000;
            }
            request.setConfig(RequestConfig.custom()
                    .setConnectTimeout(this.timeout)
                    .setSocketTimeout(this.timeout).build());
            return new LmyXlfHttp(request);
        }


        private void addBody(HttpEntityEnclosingRequest request) {
            if (null != this.multipart && !this.multipart.isEmpty()) {
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                ContentType contentType = ContentType.create("text/plain", Consts.UTF_8);
                for (Map.Entry<String, Object> entry : this.multipart.entrySet()) {
                    Object value = entry.getValue();
                    String key = entry.getKey();
                    if (value instanceof File) {
                        multipartEntityBuilder.addBinaryBody(key, (File) value);
                    } else if (value instanceof byte[]) {
                        multipartEntityBuilder.addBinaryBody(key, (byte[]) value);
                    } else if (value instanceof InputStream) {
                        multipartEntityBuilder.addBinaryBody(key, (InputStream) value);
                    } else {
                        multipartEntityBuilder.addTextBody(key, String.valueOf(value), contentType);
                    }
                }
                if (null != this.body && !this.body.isEmpty()) {
                    for (Map.Entry<String, Object> entry : this.body.entrySet()) {
                        multipartEntityBuilder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), contentType);
                    }
                }
                request.setEntity(multipartEntityBuilder.build());
                return;
            }

            if (null != this.body && !this.body.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<>();
                for (Map.Entry<String, Object> entry : this.body.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/x-www-form-urlencoded");
                request.setEntity(entity);
                return;
            }

            if (null != this.byteArrayBody && this.byteArrayBody.length > 0) {
                ByteArrayEntity entity = new ByteArrayEntity(this.byteArrayBody);
                entity.setContentType("application/octet-stream");
                request.setEntity(entity);
                return;
            }

            String sendStr;
            if (this.json != null) {
                sendStr = this.json instanceof String ? (String) this.json : JSON.toJSONString(this.json);
                request.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
                request.setHeader("Accept", "application/json;");
            } else if (StringUtils.isNotBlank(this.xml)) {
                request.setHeader(HTTP.CONTENT_TYPE, "text/xml; charset=utf-8");
                sendStr = this.xml;
            } else if (StringUtils.isNotBlank(this.text)) {
                request.setHeader(HTTP.CONTENT_TYPE, "text/plain; charset=utf-8");
                sendStr = this.text;
            } else {
                return;
            }
            request.setEntity(new StringEntity(sendStr, StandardCharsets.UTF_8));
        }


        @Override
        public String toString() {
            return "LmyXlfHttp.LmyXlfHttpBuilder(url=" + this.url + ", urlParams=" + this.urlParams + ", header=" + this.header + ", body=" + this.body + ", method=" + this.method + ", json=" + this.json + ", xml=" + this.xml + ", text=" + this.text + ", contentType=" + this.contentType + ")";
        }
    }
}