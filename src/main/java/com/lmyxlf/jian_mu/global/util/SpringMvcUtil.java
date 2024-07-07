package com.lmyxlf.jian_mu.global.util;


import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description springMvc 工具类
 *              import javax.servlet.http.HttpServletRequest 改为 import jakarta.servlet.http.HttpServletRequest
 * @since 17
 */
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

}