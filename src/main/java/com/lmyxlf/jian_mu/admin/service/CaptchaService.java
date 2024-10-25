package com.lmyxlf.jian_mu.admin.service;

import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/31 2:16
 * @description
 * @since 17
 */
public interface CaptchaService {

    /**
     * 生成验证码
     *
     * @return
     */
    Map<String, Object> getCode();
}