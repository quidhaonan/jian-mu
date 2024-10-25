package com.lmyxlf.jian_mu.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/6 1:24
 * @description
 * @since 17
 */
@Data
@ApiModel("系统文件相关信息")
@Accessors(chain = true)
public class SysFileDTO {

    @ApiModelProperty("盘符路径")
    private String dirName;

    @ApiModelProperty("盘符类型")
    private String sysTypeName;

    @ApiModelProperty("文件类型")
    private String typeName;

    @ApiModelProperty("总大小")
    private String total;

    @ApiModelProperty("剩余大小")
    private String free;

    @ApiModelProperty("已经使用量")
    private String used;

    @ApiModelProperty("资源的使用率")
    private double usage;
}