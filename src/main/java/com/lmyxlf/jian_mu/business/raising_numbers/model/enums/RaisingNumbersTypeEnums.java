package com.lmyxlf.jian_mu.business.raising_numbers.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/24 14:09
 * @description
 * @since 17
 */
@Getter
@AllArgsConstructor
public enum RaisingNumbersTypeEnums {
    LIBAO_LAND(1, "丽宝乐园");

    private final Integer type;
    private final String name;
}