package com.lmyxlf.jian_mu.global.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lmyxlf.jian_mu.global.annotation.NoticeErrorAnnotation;
import com.lmyxlf.jian_mu.global.constant.TraceConstant;
import com.lmyxlf.jian_mu.global.util.XiZhiNoticeUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/27 22:00
 * @description 监控添加 NoticeErrorAnnotation 注解的类或方法
 * @since 17
 */
@Slf4j
@Aspect
@Component
public class NoticeErrorAspect {

    @Autowired
    private ApplicationContext context;

    @Pointcut("@within(com.lmyxlf.jian_mu.global.annotation.NoticeErrorAnnotation)" +
            " || @annotation(com.lmyxlf.jian_mu.global.annotation.NoticeErrorAnnotation)")
    public void noticeErrorPointcut() {

    }

    @AfterThrowing(pointcut = "noticeErrorPointcut()", throwing = "ex")
    public void noticeErrorAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        log.error("指定方法异常，ex：{}", ex.getMessage());
        NoticeErrorAnnotation noticeErrorAnnotation = getHandleExceptionAnnotation(joinPoint);

        if (ObjUtil.isNotNull(noticeErrorAnnotation)) {
            List<String> pushUrls = new ArrayList<>();
            // 获得所有实现了 PutPushUrlsAble 接口的实现类
            Map<String, PutPushUrlsAble> providers = context.getBeansOfType(PutPushUrlsAble.class);
            if (!providers.isEmpty()) {
                String[] filter = noticeErrorAnnotation.filter();
                for (PutPushUrlsAble provider : providers.values()) {
                    // 使用过滤参数获得符合条件的实现类
                    if (provider.filter(filter)) {
                        String[] urls = provider.put();
                        // 添加到总列表中
                        pushUrls.addAll(Arrays.asList(urls));
                    }
                }
            }

            String title = noticeErrorAnnotation.title();
            String content = noticeErrorAnnotation.content();

            // 获取全限定名
            String className = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            String fullyLimitedName = className + "." + methodName;

            Map<String, String> contentMap = new HashMap<>() {{
                put("异常位置", fullyLimitedName);
                put("异常信息", StrUtil.isBlank(content) ? ex.getMessage() : content);
                put(TraceConstant.TRACE_ID, MDC.get(TraceConstant.TRACE_ID));
                put("时间", DateUtil.now());
            }};
            XiZhiNoticeUtil.xiZhiMsgNotice(pushUrls, title, JSONUtil.toJsonStr(contentMap));
        }
    }

    /**
     * 获取类或方法添加的 NoticeErrorAnnotation
     *
     * @param joinPoint
     * @return
     */
    private NoticeErrorAnnotation getHandleExceptionAnnotation(JoinPoint joinPoint) {

        if (joinPoint.getSignature() instanceof MethodSignature signature) {
            Method method = signature.getMethod();
            NoticeErrorAnnotation noticeErrorAnnotation = method.getAnnotation(NoticeErrorAnnotation.class);
            if (ObjUtil.isNotNull(noticeErrorAnnotation)) {
                return noticeErrorAnnotation;
            }
        }

        // 检查类级别的注解
        Class<?> targetClass = joinPoint.getTarget().getClass();
        return targetClass.getAnnotation(NoticeErrorAnnotation.class);
    }

    public interface PutPushUrlsAble {

        boolean filter(String[] filter);

        String[] put();
    }
}