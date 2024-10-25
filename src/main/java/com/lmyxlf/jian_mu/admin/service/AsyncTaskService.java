package com.lmyxlf.jian_mu.admin.service;

import com.lmyxlf.jian_mu.admin.model.entity.SysOperLog;

import java.util.TimerTask;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/22 2:18
 * @description 异步工厂（产生任务用）
 * @since 17
 */
public interface AsyncTaskService {

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务 task
     */
    TimerTask recordLogininfor(final String username,
                               final String status,
                               final String message,
                               final Object... args);

    /**
     * 操作日志记录
     *
     * @param sysOperLog 操作日志信息
     * @return 任务 task
     */
    TimerTask recordOper(final SysOperLog sysOperLog);
}