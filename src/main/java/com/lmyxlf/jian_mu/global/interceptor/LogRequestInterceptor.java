package com.lmyxlf.jian_mu.global.interceptor;

import com.lmyxlf.jian_mu.global.constant.TraceConstant;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@Slf4j
public class LogRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        String traceId = MDC.get(TraceConstant.TRACE_ID);
        log.info("url：{}，traceId：{}", request.getServletPath(), traceId);
        return true;
    }
}