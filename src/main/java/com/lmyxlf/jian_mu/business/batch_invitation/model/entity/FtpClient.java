package com.lmyxlf.jian_mu.business.batch_invitation.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 22:16
 * @description
 * @since 17
 */
@Data
@ApiModel("FTP 服务器")
@Accessors(chain = true)
public class FtpClient {

    @TableId
    @ApiModelProperty(value = "主键 id")
    private Integer id;

    @ApiModelProperty(value = "FTP 服务器名称")
    private String name;

    @ApiModelProperty(value = "访问域名")
    private String domain;

    @ApiModelProperty(value = "FTP 地址")
    private String host;

    @ApiModelProperty(value = "FTP 端口")
    private Integer port;

    @ApiModelProperty(value = "FTP 账号")
    private String username;

    @ApiModelProperty(value = "FTP 密码")
    private String password;

    @ApiModelProperty(value = "是否删除，0：未删除，1：已删除")
    private Integer isDelete;

    @ApiModelProperty(value = "备注")
    private String remark;
}