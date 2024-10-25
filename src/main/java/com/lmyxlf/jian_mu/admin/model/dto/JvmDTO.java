package com.lmyxlf.jian_mu.admin.model.dto;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/6 1:10
 * @description
 * @since 17
 */
@Data
@ApiModel("JVM 相关信息")
@Accessors(chain = true)
public class JvmDTO {

    @ApiModelProperty("当前 JVM 占用的内存总数（M）")
    private double total;

    @ApiModelProperty("JVM 最大可用内存总数（M）")
    private double max;

    @ApiModelProperty("JVM 空闲内存（M）")
    private double free;

    @ApiModelProperty("JDK 版本")
    private String version;

    @ApiModelProperty("JDK 路径")
    private String home;

    public double getTotal() {

        return NumberUtil.div(total, (1024 * 1024), 2);
    }


    public double getMax() {

        return NumberUtil.div(max, (1024 * 1024), 2);
    }


    public double getFree() {

        return NumberUtil.div(free, (1024 * 1024), 2);
    }


    public double getUsed() {

        return NumberUtil.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage() {

        return NumberUtil.mul(NumberUtil.div(total - free, total, 4), 100);
    }

    /**
     * 获取 JDK 名称
     */
    public String getName() {

        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    /**
     * JDK 启动时间
     */
    public String getStartTime() {

        return DateUtil.format(getServerStartDate(), DatePattern.NORM_DATETIME_PATTERN);
    }

    /**
     * JDK 运行时间
     */
    public String getRunTime() {

        // 时间差
        long diffMillis = DateUtil.betweenMs(getServerStartDate(), DateUtil.date());

        // 获取天数、小时数和分钟数
        long days = diffMillis / (1000 * 60 * 60 * 24);
        long hours = (diffMillis / (1000 * 60 * 60)) % 24;
        long minutes = (diffMillis / (1000 * 60)) % 60;

        // 拼接结果
        return days + "天 " + hours + "小时 " + minutes + "分钟";
    }

    /**
     * 运行参数
     */
    public String getInputArgs() {

        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {

        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }
}