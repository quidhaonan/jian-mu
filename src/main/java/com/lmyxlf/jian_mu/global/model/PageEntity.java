package com.lmyxlf.jian_mu.global.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/8 2:09
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class PageEntity {

    private Integer page = 1;

    private Integer size = 10;
}