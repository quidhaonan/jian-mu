package com.lmyxlf.jian_mu.global.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.http.Header;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 调用 HttpClient 返回结果封装
 * @since 17
 */
@Data
@Accessors(chain = true)
public class HttpResult {

    /**
     * http响应状态码
     */
    private Integer statusCode;

    /**
     * http响应数据
     */
    private String result;

    /**
     * http响应字节数组
     */
    private byte[] bytes;

    /**
     * 头部字段
     */
    private Header[] headers;
}