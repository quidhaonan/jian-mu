package com.lmyxlf.jian_mu.log;

import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description javax 升级为 jakarta，因此 HttpServletRequest，HttpServletResponse 需要更换导入，同时
 *              String ipAddr = IpUtils.getIpAddr(request); 也需要进行强转
 *              强转报错，修改 getIpAddr 接收参数
 *              添加随机 traceId
 * @since 17
 */
public class RemoteIpLogInterceptor implements HandlerInterceptor {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RemoteIpLogInterceptor.class);

    public static final String REMOTE_IP = "remoteIp";

    public static final String TRACE_ID = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddr = IpUtils.getIpAddr(request);
        // 强转报错，修改 getIpAddr 接收参数
//        String ipAddr = IpUtils.getIpAddr((javax.servlet.http.HttpServletRequest) request);
        MDC.put(REMOTE_IP, ipAddr);
        MDC.put(TRACE_ID, TraceIdGenerator.generateTraceId(ipAddr));
        return true;
    }

}
