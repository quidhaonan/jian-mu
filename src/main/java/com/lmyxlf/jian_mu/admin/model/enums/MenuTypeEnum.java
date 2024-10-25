package com.lmyxlf.jian_mu.admin.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/13 2:46
 * @description 菜单类型
 * @since 17
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum {

    DIRECTORY(0, "目录"),
    MENU(1, "菜单"),
    BUTTON(2, "按钮");

    private final Integer value;
    private final String text;
}