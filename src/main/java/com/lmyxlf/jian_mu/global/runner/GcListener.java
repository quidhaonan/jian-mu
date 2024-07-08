package com.lmyxlf.jian_mu.global.runner;

import com.sun.management.GarbageCollectionNotificationInfo;
import lombok.extern.slf4j.Slf4j;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.MemoryUsage;
import java.util.Map;
import java.util.Set;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@Slf4j
public class GcListener implements NotificationListener {

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
            GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
            // 耗时
            long duration = info.getGcInfo().getDuration();
            // 类型
            String gctype = info.getGcAction();
            // 开始时间
            long startTime = info.getGcInfo().getStartTime();
            // 结束时间
            long endTime = info.getGcInfo().getEndTime();
            // GC 前参数
            Map<String, MemoryUsage> memoryUsageAfterGc = info.getGcInfo().getMemoryUsageAfterGc();
            // GC 后参数
            Map<String, MemoryUsage> memoryUsageBeforeGc = info.getGcInfo().getMemoryUsageBeforeGc();

            log.info("GC类型：{}，耗时：{}，开始时间：{}，结束时间：{}", gctype, duration, startTime, endTime);

            // 指标名
            Set<String> names = memoryUsageBeforeGc.keySet();
            for (String name : names) {
                MemoryUsage after = memoryUsageAfterGc.get(name);
                MemoryUsage before = memoryUsageBeforeGc.get(name);
                log.info("GC指标：{}， init={}（{}k）->{}（{}k）, used={}（{}k）->{}（{}k）,committed={}（{}k）->{}（{}k）, max={}（{}k）->{}（{}k）",
                        name,
                        before.getInit(), before.getInit() / 1024, after.getInit(), after.getInit() / 1024,
                        before.getUsed(), before.getUsed() / 1024, after.getUsed(), after.getUsed() / 1024,
                        before.getCommitted(), before.getCommitted() / 1024, after.getCommitted(), after.getCommitted() / 1024,
                        before.getMax(), before.getMax() / 1024, after.getMax(), after.getMax() / 1024);
            }
        }
    }
}