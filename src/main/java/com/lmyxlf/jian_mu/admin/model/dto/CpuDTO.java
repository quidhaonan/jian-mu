package com.lmyxlf.jian_mu.admin.model.dto;

import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/6 0:56
 * @description
 * @since 17
 */
@Data
@ApiModel("CPU 相关信息")
@Accessors(chain = true)
public class CpuDTO {

    @ApiModelProperty("核心数")
    private int cpuNum;

    @ApiModelProperty("CPU 总的使用率")
    private double total;

    @ApiModelProperty("CPU 系统使用率")
    private double sys;

    @ApiModelProperty("CPU 用户使用率")
    private double used;

    @ApiModelProperty("CPU 当前等待率")
    private double wait;

    @ApiModelProperty("CPU 当前空闲率")
    private double free;

    public double getTotal() {
        return NumberUtil.round(NumberUtil.mul(total, 100), 2).doubleValue();
    }


    public double getSys() {
        return NumberUtil.round(NumberUtil.mul(sys / total, 100), 2).doubleValue();
    }


    public double getUsed() {
        return NumberUtil.round(NumberUtil.mul(used / total, 100), 2).doubleValue();
    }


    public double getWait() {
        return NumberUtil.round(NumberUtil.mul(wait / total, 100), 2).doubleValue();
    }


    public double getFree() {
        return NumberUtil.round(NumberUtil.mul(free / total, 100), 2).doubleValue();
    }

}