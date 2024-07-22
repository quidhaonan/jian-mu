package com.lmyxlf.jian_mu.global.exception;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 业务层异常
 * @since 17
 */
@Deprecated
public class ServiceException extends MyException {

    public ServiceException() {
    }

    /**
     * 构造方法
     * @param error 异常信息
     */
    public ServiceException(Object error) {
        super(error);
    }


    /**
     * 构造方法
     * @param error 异常信息
     * @param msg 异常信息
     */
    public ServiceException(Object error, String msg) {
        super(error, msg);
    }
}