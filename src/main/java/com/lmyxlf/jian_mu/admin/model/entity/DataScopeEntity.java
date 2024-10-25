package com.lmyxlf.jian_mu.admin.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/10 23:20
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
public class DataScopeEntity {

    private String dataScopeSql;
}