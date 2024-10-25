package com.lmyxlf.jian_mu.admin.handler;

import org.springframework.security.core.Authentication;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 14:15
 * @description 身份验证信息
 * @since 17
 */
public class AuthenticationContextHolder {

    private static final ThreadLocal<Authentication> contextHolder = new ThreadLocal<>();

    public static Authentication getContext() {

        return contextHolder.get();
    }

    public static void setContext(Authentication context) {

        contextHolder.set(context);
    }

    public static void clearContext() {

        contextHolder.remove();
    }
}