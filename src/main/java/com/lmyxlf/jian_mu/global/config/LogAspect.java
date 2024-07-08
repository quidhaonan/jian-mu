package com.lmyxlf.jian_mu.global.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lmyxlf.jian_mu.global.util.XiZhiNoticeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    public static Cache<String, String> cache =
            Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.HOURS)
                    .maximumSize(100).build();

    public static final String responseByteSuffix = "/responseByte";
    public static final String responseTimeSuffix = "/responseTime";

    @Value("#{${lmyxlf.response.urls.map:{}}}")
    private Map<String, Integer> urls;

    private static int defaultExceedByteSize = 1024;
    private static int defaultExceedTime = 5 * 1000;


    // 定义一个切点
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "||@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        byte[] bytes = JSON.toJSONBytes(result);
        if (bytes.length > defaultExceedByteSize) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            boolean flag = true;
            if (urls != null && urls.getOrDefault(request.getRequestURI(), defaultExceedByteSize) > bytes.length) {
                flag = false;
            }
            if (flag && StrUtil.isEmpty(cache.getIfPresent(request.getRequestURI() + responseByteSuffix))) {
                // MonitorUtil.sendMsgToQyApi("接口响应数据超过1K", request.getRequestURI());
                sendWarnMsgToXiZhi("接口响应数据超过1K", request.getRequestURI());
                cache.put(request.getRequestURI() + responseByteSuffix, "1");
            }
        }
        log.info("返回的结果: {}", JSON.toJSONString(result));
        if (System.currentTimeMillis() - startTime > defaultExceedTime) {
            // Monitor.warn("response_exceeds").unLog().inc();
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            if (StrUtil.isEmpty(cache.getIfPresent(request.getRequestURI() + responseTimeSuffix))) {
                // MonitorUtil.sendMsgToQyApi("接口响应时间超过5s", request.getRequestURI());
                sendWarnMsgToXiZhi("接口响应时间超过5s", request.getRequestURI());
                cache.put(request.getRequestURI() + responseTimeSuffix, "1");
            }
        }
        log.info("=== 结束时，总耗时：{} ms ===", System.currentTimeMillis() - startTime);
        return result;
    }

    private JSONObject sendWarnMsgToXiZhi(String message, String url) {
        String content = StrUtil.format("异常描述：异常抛出\n" +
                "接口路径：{}\n" +
                "异常消息：{}\n" +
                "traceId：{}", url, message, MDC.get("traceId"));

        return XiZhiNoticeUtil.xiZhiMsgNotice(message, content);
    }
}