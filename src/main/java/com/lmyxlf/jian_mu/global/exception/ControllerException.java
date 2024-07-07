package com.lmyxlf.jian_mu.global.exception;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 控制层异常
 * @since 17
 */
public class ControllerException extends MyException {
    public ControllerException() {
    }

    /**
     * 构造方法
     * @param error 异常信息
     */
    public ControllerException(Object error) {
        super(error);
    }


    /**
     * 构造方法
     * @param error 异常信息
     * @param msg 异常信息
     */
    public ControllerException(Object error, String msg) {
        super(msg, msg);
    }
}