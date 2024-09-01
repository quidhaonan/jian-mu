package com.lmyxlf.jian_mu.log;

import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import com.lmyxlf.jian_mu.global.constant.TraceConstant;
import com.lmyxlf.jian_mu.global.util.IPUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
public class RemoteIpLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        String ipAddr = IPUtils.getIpAddr(request);
        MDC.put(LmyXlfReqParamConstant.REMOTE_IP, ipAddr);
        MDC.put(TraceConstant.TRACE_ID, TraceIdGenerator.generateTraceId(ipAddr));
        return true;
    }
}