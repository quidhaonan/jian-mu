package com.lmyxlf.jian_mu.global.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/25 23:45
 * @description 基础数据库实体类
 * @since 17
 */
@Data
@Accessors(chain = true)
public class BaseDBEntity {

    /**
     * 是否被删除
     */
    private Integer isDelete;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateUser;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}