package com.lmyxlf.jian_mu.log;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;
import java.lang.management.ManagementFactory;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description 生成 traceId
 * @since 17
 */
@Slf4j
public class TraceIdGenerator {

    private static final AtomicLong SEQUENCE = new AtomicLong(1000);
    private static final Integer MAX_SEQUENCE = 9000;
    private static final Integer MIN_SEQUENCE = 1000;
    private static final String PROCESS_ID = getProcessId();

    /**
     * 生成 traceId
     */
    public static String generateTraceId(String ipAddr) {
        return convertIpToHex(ipAddr) +
                DateUtil.current() +
                nextSequence() +
                PROCESS_ID;
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
        if (!Validator.isIpv4(ipAddr) && !Validator.isIpv6(ipAddr)) {
            log.error("生成 traceId 失败，ipAddr：{}", ipAddr);
            throw new LmyXlfException(CODE_MSG.IP_ERROR);
        }

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