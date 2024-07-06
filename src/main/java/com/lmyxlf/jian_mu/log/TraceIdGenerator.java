package com.lmyxlf.jian_mu.log;

import cn.hutool.core.date.DateUtil;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.management.ManagementFactory;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description 生成 traceId
 * @since 17
 */
public class TraceIdGenerator {
    private static final AtomicLong SEQUENCE = new AtomicLong(1000);
    private static final int MAX_SEQUENCE = 9000;
    private static final int MIN_SEQUENCE = 1000;
    private static final String PROCESS_ID = getProcessId();

    /**
     * 生成 TraceId
     */
    public static String generateTraceId(String ipAddr) {
        StringBuilder sb = new StringBuilder();
        sb.append(convertIpToHex(ipAddr));
        sb.append(DateUtil.current());
        sb.append(nextSequence());
        sb.append(PROCESS_ID);
        return sb.toString();
    }

    /**
     * 获取下一个序列号
     */
    private static long nextSequence() {
        long current = SEQUENCE.getAndIncrement();
        if (current >= MAX_SEQUENCE) {
            SEQUENCE.set(MIN_SEQUENCE);
            return MIN_SEQUENCE;
        }
        return current;
    }

    /**
     * 将 IP 地址转换为十六进制字符串
     */
    private static String convertIpToHex(String ipAddr) {
        String[] octets = ipAddr.split("\\.");
        StringBuilder hexString = new StringBuilder();
        for (String octet : octets) {
            int octetInt = Integer.parseInt(octet);
            String hexPart = String.format("%02x", octetInt);
            hexString.append(hexPart);
        }
        return hexString.toString();
    }

    /**
     * 获取当前进程 ID
     */
    private static String getProcessId() {
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        return processName.split("@")[0];
    }
}