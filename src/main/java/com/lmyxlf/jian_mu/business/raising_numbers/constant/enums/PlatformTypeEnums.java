package com.lmyxlf.jian_mu.business.raising_numbers.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/25 19:14
 * @description 随机生成 User-Agent 所需平台类型枚举
 * @since 17
 */
@Getter
@AllArgsConstructor
public enum PlatformTypeEnums {
    WINDOWS(1, "windows"),
    ANDROID(2, "android"),
    IPAD(3, "ipad"),
    IPHONE(4, "iphone"),
    MAC_OX(5, "macs");

    private final Integer type;
    private final String name;
}