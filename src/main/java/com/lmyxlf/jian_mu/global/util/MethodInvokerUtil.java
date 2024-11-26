package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.util.ObjUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/9 15:38
 * @description 通过类名、方法名来调用指定方法
 * @since 17
 */
@Slf4j
public class MethodInvokerUtil {

    private static final Cache<String, MethodHandle> CACHE =
            Caffeine.newBuilder().expireAfterAccess(1, TimeUnit.DAYS)
                    .maximumSize(100).build();

    /**
     * @param beanName       bean 名称
     * @param methodName     类名
     * @param parameterTypes 方法参数类型
     * @param args           参数
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(String beanName, String methodName, Class<?>[] parameterTypes, Object... args) {

        // 构建缓存的键
        String cacheKey = String.format(beanName + "." + methodName + "$" +
                Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.joining(",")));

        Object bean = SpringContextHolder.getBean(beanName);

        try {

            // 获取目标方法
            Method method = bean.getClass().getDeclaredMethod(methodName, parameterTypes);
            // 判断方法是否为 static
            boolean isStatic = Modifier.isStatic(method.getModifiers());

            Object[] newArgs = isStatic ? args : Stream.concat(Stream.of(bean), Arrays.stream(args)).toArray();

            // 如果缓存中已有方法句柄，直接返回
            MethodHandle methodHandle = CACHE.getIfPresent(cacheKey);
            if (ObjUtil.isNull(methodHandle)) {

                method.setAccessible(true);
                // 创建 MethodHandle
                methodHandle = MethodHandles.lookup().unreflect(method);

                // 缓存该方法句柄
                CACHE.put(cacheKey, methodHandle);
            }

            // 调用方法并返回结果
            return (T) methodHandle.invokeWithArguments(newArgs);
        } catch (Throwable e) {

            log.error("垮分支方法调用出错，beanName：{}，methodName：{}，parameterTypes：{}，args：{}，e：{}",
                    beanName, methodName, parameterTypes, args, e.getMessage());
            throw new LmyXlfException(CODE_MSG.ERROR);
        }
    }
}