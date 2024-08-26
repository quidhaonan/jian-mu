package com.lmyxlf.jian_mu.business.batch_invitation.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/25 13:39
 * @description 活码状态枚举
 * @since 17
 */
@AllArgsConstructor
public enum LiveCodeStatusEnum implements IEnum<Integer> {

    NORMAL(0),
    CLOSE(1);

    private final Integer type;

    @Override
    public Integer getValue() {
        return type;
    }
}