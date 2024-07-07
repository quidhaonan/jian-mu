package com.lmyxlf.jian_mu.global.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 自定义异常类
 * @since 17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Deprecated
public class MyException extends RuntimeException{

    public MyException() {
        super();
    }

    /**
     * 构造方法
     * @param error 异常信息
     */
    public MyException(Object error) {
        this.error = error;
    }


    /**
     * 构造方法
     * @param error 异常信息
     * @param msg 异常信息
     */
    public MyException(Object error, String msg) {
        super(msg);
        this.error = error;
    }

    /**
     * 自定义异常信息
     */
    private Object error;

}