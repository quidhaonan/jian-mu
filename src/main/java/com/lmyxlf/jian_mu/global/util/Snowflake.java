package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 雪花算法
 * tweeter的snowflake 移植到Java: (a) id构成: 42位的时间前缀 + 10位的节点标识 +
 * 12位的sequence避免并发的数字(12位不够用时强制得到新的时间前缀) 注意这里进行了小改动:
 * snowkflake是5位的datacenter加5位的机器id; 这里变成使用10位的机器id (b)
 * 对系统时间的依赖性非常强，需关闭ntp的时间同步功能。当检测到ntp时间调整后，将会拒绝分配id
 *
 * @author sam
 * @since 2020/1/2 19:37
 */
/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 雪花算法
 *              tweeter 的 snowflake 移植到 Java: (a) id构成: 42位的时间前缀 + 10位的节点标识 +
 *              12位的 sequence 避免并发的数字(12 位不够用时强制得到新的时间前缀) 注意这里进行了小改动:
 *              snowkflake 是 5 位的 datacenter 加 5 位的机器 id; 这里变成使用 10 位的机器 id (b)
 *              对系统时间的依赖性非常强，需关闭 ntp 的时间同步功能。当检测到 ntp 时间调整后，将会拒绝分配id
 * @since 17
 */
@Slf4j
public class Snowflake {

    private final long workerId;
    // 2020.01.01 00:00:00 时间起始标记点，作为基准，一般取系统的最近时间
    private final long epoch = 1577808000000L;
    // 机器标识位数
    private final long workerIdBits = 10L;
    // 机器ID最大值:
    private final long maxWorkerId = ~(-1L << this.workerIdBits);
    // 0，并发控制
    private long sequence = 0L;
    // 毫秒内自增位
    private final long sequenceBits = 12L;
    // 12
    private final long workerIdShift = this.sequenceBits;
    // 22
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;
    // 4095,111111111111,12位
    private final long sequenceMask = ~(-1L << this.sequenceBits);
    private long lastTimestamp = -1L;

    private Snowflake() {
        long workerId = 156;
        try {
            InetAddress ia = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
            byte[] mac = ni.getHardwareAddress();
            String s = Integer.toHexString(mac[mac.length - 1] & 0xFF);
            int macWorker = Integer.parseInt(s, 16);
            if (macWorker < 1030) {
                workerId = macWorker;
            }
        } catch (SocketException e) {
            // Monitor.error("get_network_interface_by_inet_address_error").log("通过ip地址获取网卡失败：{}", ExceptionUtil.getMessage(e)).inc();
            log.error("通过 ip 地址获取网卡失败：{}", ExceptionUtil.getMessage(e));
        } catch (UnknownHostException e) {
            // Monitor.error("get_local_host_error").log("获取本地host异常：{}", ExceptionUtil.getMessage(e)).inc();
            log.error("获取本地 host 异常：{}", ExceptionUtil.getMessage(e));
        }

        if (workerId > this.maxWorkerId || workerId < 0) {
            log.debug("workerId : ---------------------------- " + workerId);
            log.debug("maxWorkerId : ---------------------------- " + this.maxWorkerId);
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = Snowflake.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }
        // 如果上一个 timestamp 与新产生的相等，则 sequence 加一(0-4095 循环);
        if (this.lastTimestamp == timestamp) {
            // 对新的 timestamp，sequence 从 0 开始
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                // 重新生成 timestamp
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        this.lastTimestamp = timestamp;
        return timestamp - this.epoch << this.timestampLeftShift | this.workerId << this.workerIdShift | this.sequence;
    }

    private static Snowflake flowIdWorker = new Snowflake();

    public static Snowflake getInstance() {
        return flowIdWorker;
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数 lastTimestamp 之后
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = Snowflake.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = Snowflake.timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

}