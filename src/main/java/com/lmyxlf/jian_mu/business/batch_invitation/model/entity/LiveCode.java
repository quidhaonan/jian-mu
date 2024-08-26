package com.lmyxlf.jian_mu.business.batch_invitation.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 1:07
 * @description
 * @since 17
 */
@Data
@ApiModel("活码实体类")
@Accessors(chain = true)
public class LiveCode {

    @TableId
    @ApiModelProperty(value = "主键 id")
    private Integer id;

    @ApiModelProperty(value = "ftp_client 表主键 id")
    private Integer ftpClientId;

    @ApiModelProperty(value = "活码名称")
    private String name;

    @ApiModelProperty(value = "访问链接")
    private String url;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "随机字符串，相较于 file_name 少文件类型后缀")
    private String randomStr;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "已查看次数")
    private Long viewedCount;

    @ApiModelProperty(value = "状态，0：正常，1：关闭")
    private Integer status;

    @ApiModelProperty(value = "是否删除，0：未删除，1：已删除")
    private Integer isDelete;

    @ApiModelProperty(value = "备注")
    private String remark;
}