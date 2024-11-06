package com.lmyxlf.jian_mu.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/22 23:53
 * @description
 * @since 17
 */
@Slf4j
public class ThreadPoolsHandler {

    /**
     * 通用线程池
     */
    public static final AsyncTaskExecutor ASYNC_COMMON_POOL =
            createAsyncTaskExecutor("async_common_pool_", 20, 30, 60);
    /**
     * 异步操作任务调度线程池
     */
    public static final ScheduledExecutorService ASYNC_SCHEDULED_POOL =
            createScheduledExecutorService(5,"async_scheduled_pool_");


    private static AsyncTaskExecutor createAsyncTaskExecutor(String threadNamePrefix,
                                                             Integer corePoolSize,
                                                             Integer maxPoolSize,
                                                             Integer queueCapacity) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 核心线程数
        taskExecutor.setCorePoolSize(corePoolSize);
        // 最大线程数
        taskExecutor.setMaxPoolSize(maxPoolSize);
        // 队列最大长度
        taskExecutor.setQueueCapacity(queueCapacity);
        // 线程池维护线程所允许的空闲时间
        taskExecutor.setKeepAliveSeconds(60);
        // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的 Bean
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        taskExecutor.setAwaitTerminationSeconds(60);
        // 线程池对拒绝任务（无线程可用）的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.initialize();

        return taskExecutor;
    }

    private static ScheduledExecutorService createScheduledExecutorService(Integer corePoolSize,
                                                                           String namingPatternPrefix) {

        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern(namingPatternPrefix+"%d").daemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()) {

            @Override
            protected void afterExecute(Runnable r, Throwable t) {

                super.afterExecute(r, t);
                printException(r, t);
            }
        };
    }

    /**
     * 打印线程异常信息
     */
    public static void printException(Runnable r, Throwable t) {

        if (t == null && r instanceof Future<?>) {

            try {

                Future<?> future = (Future<?>) r;
                if (future.isDone()) {

                    future.get();
                }
            } catch (CancellationException ce) {

                t = ce;
            } catch (ExecutionException ee) {

                t = ee.getCause();
            } catch (InterruptedException ie) {

                Thread.currentThread().interrupt();
            }
        }

        if (t != null) {

            log.error(t.getMessage(), t);
        }
    }
}