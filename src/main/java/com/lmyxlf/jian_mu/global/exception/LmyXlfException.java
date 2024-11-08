package com.lmyxlf.jian_mu.global.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 主要异常
 * @since 17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LmyXlfException extends RuntimeException {
    private Object error;

    public LmyXlfException() {
    }

    public LmyXlfException(Object error) {
        this.error = error;
    }

    public LmyXlfException(Object error, String msg) {
        super(msg);
        this.error = error;
    }
}