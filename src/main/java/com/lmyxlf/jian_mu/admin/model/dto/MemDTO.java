package com.lmyxlf.jian_mu.admin.model.dto;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/6 1:07
 * @description
 * @since 17
 */
@Data
@ApiModel("內存相关信息")
@Accessors(chain = true)
public class MemDTO {

    @ApiModelProperty("内存总量")
    private double total;

    @ApiModelProperty("已用内存")
    private double used;

    @ApiModelProperty("剩余内存")
    private double free;

    public double getTotal() {

        return NumberUtil.div(total, (1024 * 1024 * 1024), 2);
    }

    public double getUsed() {

        return NumberUtil.div(used, (1024 * 1024 * 1024), 2);
    }


    public double getFree() {

        return NumberUtil.div(free, (1024 * 1024 * 1024), 2);
    }


    public double getUsage() {

        return NumberUtil.mul(NumberUtil.div(used, total, 4), 100);
    }
}