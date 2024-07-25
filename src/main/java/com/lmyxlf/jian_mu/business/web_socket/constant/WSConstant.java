package com.lmyxlf.jian_mu.business.web_socket.constant;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/18 19:18
 * @description ws 常量
 * @since 17
 */
public class WSConstant {

    /**
     * 浏览器加密，存储 redis key 前缀
     */
    public static final String REDIS_BROWSER_ENCRYPT_PREFIX = "browserEncrypt:";

    /**
     * redis countDownLatch 前缀
     */
    public static final String REDIS_COUNTDOWNLATCH_PREFIX = "countDownLatch:";

    /**
     * redis 过期时间，单位：秒
     */
    public static final Long REDIS_EXPIRE_TIME = 10 * 60L;

    /**
     * redis countDownLatch 超时时间，单位：秒
     */
    public static final Long COUNTDOWNLATCH_AWAIT_TIME = 5L;
}