package com.lmyxlf.jian_mu.global.exception;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description
 * @since 17
 */
public class LmyXlfIOException extends LmyXlfException{
    public LmyXlfIOException() {
    }

    public LmyXlfIOException(Object error) {
        super(error);
    }

    public LmyXlfIOException(Object error, String msg) {
        super(error, msg);
    }
}