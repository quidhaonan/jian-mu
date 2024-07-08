package com.lmyxlf.jian_mu.global.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.management.NotificationEmitter;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@Slf4j
public class GcInitRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            // 添加监听器
            emitter.addNotificationListener(new GcListener(), null, null);
        }
    }
}