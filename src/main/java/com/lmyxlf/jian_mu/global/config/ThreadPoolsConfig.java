package com.lmyxlf.jian_mu.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/19 2:06
 * @description 线程池配置
 * @since 17
 */
// 开启异步任务支持
@EnableAsync
@Configuration
public class ThreadPoolsConfig {

    // 线程池维护线程所允许的空闲时间
    private static final int KEEP_ALIVE_SECONDS = 60;

    /**
     * 核心线程池
     *
     * @return
     */
    @Bean("async_executor_core")
    public AsyncTaskExecutor getAsyncExecutorCore() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 核心线程数
        taskExecutor.setCorePoolSize(5);
        // 最大线程数
        taskExecutor.setMaxPoolSize(10);
        // 队列最大长度
        taskExecutor.setQueueCapacity(20);
        // 线程池维护线程所允许的空闲时间
        taskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的 Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(KEEP_ALIVE_SECONDS);
        // 线程池对拒绝任务（无线程可用）的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadNamePrefix("async_executor_core_");
        taskExecutor.initialize();

        return taskExecutor;
    }

    /**
     * 分支 feat/lmyxlf/web_socket 线程池
     *
     * @return
     */
    @Bean("async_executor_ws")
    public AsyncTaskExecutor getAsyncExecutorWs() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 核心线程数
        taskExecutor.setCorePoolSize(10);
        // 最大线程数
        taskExecutor.setMaxPoolSize(20);
        // 队列最大长度
        taskExecutor.setQueueCapacity(30);
        // 线程池维护线程所允许的空闲时间
        taskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的 Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(KEEP_ALIVE_SECONDS);
        // 线程池对拒绝任务（无线程可用）的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadNamePrefix("async_executor_ws_");
        taskExecutor.initialize();

        return taskExecutor;
    }

    /**
     * 分支 feat/lmyxlf/养号 线程池
     * Raising numbers
     *
     * @return
     */
    @Bean("async_executor_rn")
    public AsyncTaskExecutor getAsyncExecutorRn() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 核心线程数
        taskExecutor.setCorePoolSize(2);
        // 最大线程数
        taskExecutor.setMaxPoolSize(2);
        // 队列最大长度
        taskExecutor.setQueueCapacity(30);
        // 线程池维护线程所允许的空闲时间
        taskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的 Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(KEEP_ALIVE_SECONDS);
        // 线程池对拒绝任务（无线程可用）的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadNamePrefix("async_executor_rn_");
        taskExecutor.initialize();

        return taskExecutor;
    }
}