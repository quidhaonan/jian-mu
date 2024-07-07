package com.lmyxlf.jian_mu.global.constant;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description 错误码枚举接口
 * @since 17
 */
public interface CodeMsg{

    default Object getOrigin() {
        return null;
    }

    Object getCode();

    String getMsg();
}