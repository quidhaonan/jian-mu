package com.lmyxlf.jian_mu.global.util;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
// import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description springMvc 工具类
 * import javax.servlet.http.HttpServletRequest 改为 import jakarta.servlet.http.HttpServletRequest
 * @since 17
 */
@Slf4j
public class SpringMvcUtil {

    /**
     * 获取当前 http 请求
     *
     * @return Optional<HttpServletRequest>
     */
    public static Optional<HttpServletRequest> getRequest() {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        if (attributes != null) {

            return Optional.of(attributes.getRequest());
        }

        return Optional.empty();
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name) {

        return getRequest().orElseThrow(() -> new IllegalStateException("Request not present")).getParameter(name);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue) {

        return Convert.toStr(getRequest().orElseThrow(() -> new IllegalStateException("Request not present")).getParameter(name), defaultValue);
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {

        return Convert.toInt(getRequest().orElseThrow(() -> new IllegalStateException("Request not present")).getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {

        return Convert.toInt(getRequest().orElseThrow(() -> new IllegalStateException("Request not present")).getParameter(name), defaultValue);
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name) {

        return Convert.toBool(getRequest().orElseThrow(() -> new IllegalStateException("Request not present")).getParameter(name));
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name, Boolean defaultValue) {

        return Convert.toBool(getRequest().orElseThrow(() -> new IllegalStateException("Request not present")).getParameter(name), defaultValue);
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String[]> getParams(ServletRequest request) {

        final Map<String, String[]> map = request.getParameterMap();
        return Collections.unmodifiableMap(map);
    }

    /**
     * 获得所有请求参数
     *
     * @param request 请求对象{@link ServletRequest}
     * @return Map
     */
    public static Map<String, String> getParamMap(ServletRequest request) {

        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {

            params.put(entry.getKey(), StringUtils.join(entry.getValue(), ","));
        }

        return params;
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {

        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {

        return getRequest().orElseThrow(() -> new IllegalStateException("Request not present")).getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     */
    public static void renderString(HttpServletResponse response, String string) {

        try {

            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {

            log.error("将字符串渲染到客户端失败，e：{}", e.getMessage());
        }
    }

    /**
     * 是否是 ajax 异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        if (accept != null && accept.contains("application/json")) {

            return Boolean.TRUE;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {

            return Boolean.TRUE;
        }

        String uri = request.getRequestURI();
        if (inStringIgnoreCase(uri, ".json", ".xml")) {

            return Boolean.TRUE;
        }

        String ajax = request.getParameter("__ajax");
        return inStringIgnoreCase(ajax, "json", "xml");
    }

    /**
     * 内容编码
     *
     * @param str 内容
     * @return 编码后的内容
     */
    public static String urlEncode(String str) {

        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    /**
     * 内容解码
     *
     * @param str 内容
     * @return 解码后的内容
     */
    public static String urlDecode(String str) {

        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    private static boolean inStringIgnoreCase(String str, String... strs) {

        if (str != null && strs != null) {

            for (String s : strs) {

                if (str.equalsIgnoreCase(StrUtil.trim(s))) {

                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }
}