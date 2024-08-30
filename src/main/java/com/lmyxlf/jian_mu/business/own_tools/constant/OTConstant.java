package com.lmyxlf.jian_mu.business.own_tools.constant;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/29 16:41
 * @description own_tools 常量
 * @since 17
 */
public class OTConstant {

    /**
     * 当前项目文件路径
     */
    public static final String PRIMARY_PATH = System.getProperty("user.dir");

    /**
     * 操作文件路径
     */
    public static final String OWN_TOOLS_PATH = "/jian_mu/own_tools";

    /**
     * redis countDownLatch 前缀
     */
    public static final String REDIS_COUNTDOWNLATCH_PREFIX = "countDownLatchOT:";

    /**
     * 最新日志文件路径
     */
    public static final String LOG_PATH = "/jian_mu/logs/jian_mu.log";
}