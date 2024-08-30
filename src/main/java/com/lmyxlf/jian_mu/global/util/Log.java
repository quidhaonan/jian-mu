package com.lmyxlf.jian_mu.global.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 标准日志接口，通过编译器自动增加需要的信息
 * @since 17
 */
public class Log implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Log.class);
    private static final Logger PROXY = (Logger) Proxy.newProxyInstance(LOGGER.getClass().getClassLoader(), LOGGER.getClass().getInterfaces(), new Log());
    private static final Long PID = java.lang.management.ManagementFactory.getRuntimeMXBean().getPid();
    public static final Integer MAX_LOG_INDEX = 9999;

    private static final ThreadLocal<Integer> LOG_INDEX = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    private static int getLogIndex() {
        int i = LOG_INDEX.get();
        if (i < MAX_LOG_INDEX) {
            LOG_INDEX.set(i + 1);
        } else {
            LOG_INDEX.set(0);
        }
        return i;
    }

    /**
     * @param className  类名
     * @param methodName 方法名
     * @param fileName   文件名
     * @param lineNumber 行号
     * @return 生成的日志头
     */
    public static String genLogHead(String className, String methodName, String fileName, int lineNumber) {

        return String.format("[%d:%d:%d][%s:%s][%s:%d]",
                PID, Thread.currentThread().getId(), getLogIndex(),
                className, methodName,
                fileName, lineNumber
        );
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return method.invoke(LOGGER, objects);
    }

    /**
     * @return 返回日志对象
     */
    public static Logger get() {
        return PROXY;
    }
}