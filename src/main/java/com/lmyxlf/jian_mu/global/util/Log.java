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
    private static Logger logger = LoggerFactory.getLogger(Log.class);
    private static Logger proxy = (Logger) Proxy.newProxyInstance(logger.getClass().getClassLoader(), logger.getClass().getInterfaces(), new Log());
    private static long pid = java.lang.management.ManagementFactory.getRuntimeMXBean().getPid();
    private static ThreadLocal<Integer> logIndex = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    public static final int MAX_LOG_INDEX = 9999;

    private static int getLogIndex() {
        int i = logIndex.get();
        if (i < MAX_LOG_INDEX) {
            logIndex.set(i + 1);
        } else {
            logIndex.set(0);
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
                pid, Thread.currentThread().getId(), getLogIndex(),
                className, methodName,
                fileName, lineNumber
        );
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return method.invoke(logger, objects);
    }

    /**
     * @return 返回日志对象
     */
    public static Logger get() {
        return proxy;
    }
}