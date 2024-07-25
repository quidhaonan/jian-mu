package com.lmyxlf.jian_mu.global.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/25 23:21
 * @description 数据库常量
 * @since 17
 */
public class DBConstant {

    /**
     * 未删除
     */
    public static final Integer NOT_DELETED = 0;

    /**
     * 已删除
     */
    public static final Integer DELETED = 1;

    /**
     * 初始时间
     */
    public static final LocalDateTime INITIAL_TIME = LocalDateTime.parse("1970-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
}