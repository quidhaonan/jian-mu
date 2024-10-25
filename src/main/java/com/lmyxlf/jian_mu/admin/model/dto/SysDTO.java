package com.lmyxlf.jian_mu.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/6 1:22
 * @description
 * @since 17
 */
@Data
@ApiModel("系统相关信息")
@Accessors(chain = true)
public class SysDTO {

    @ApiModelProperty("服务器名称")
    private String computerName;

    @ApiModelProperty("服务器 Ip")
    private String computerIp;

    @ApiModelProperty("项目路径")
    private String userDir;

    @ApiModelProperty("操作系统")
    private String osName;

    @ApiModelProperty("系统架构")
    private String osArch;
}