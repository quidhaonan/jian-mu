package com.lmyxlf.jian_mu.business.batch_invitation.model.req;

import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeFtp;
import com.lmyxlf.jian_mu.global.model.PageData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/25 17:23
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "请求活码列表请求体")
public class ReqLiveCodeList extends PageData<RespLiveCodeFtp> {

    @ApiModelProperty(value = "活码名称")
    private String name;

    @ApiModelProperty(value = "状态，0：正常，1：关闭")
    private Integer status;
}