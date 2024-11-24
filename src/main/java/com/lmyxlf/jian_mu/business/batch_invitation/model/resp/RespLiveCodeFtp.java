package com.lmyxlf.jian_mu.business.batch_invitation.model.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/25 17:30
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "请求活码列表返回体")
public class RespLiveCodeFtp {

    @ApiModelProperty(value = "主键 id")
    private Integer id;

    @ApiModelProperty(value = "访问链接")
    private String url;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "已查看次数")
    private Long viewedCount;

    @ApiModelProperty(value = "状态，0：正常，1：关闭")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "今日已查看次数")
    private Long viewedTodayCount;

    @ApiModelProperty(value = "服务器访问链接")
    private String serverUrl;
}