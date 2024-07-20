package com.lmyxlf.jian_mu.business.web_socket.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 0:56
 * @description 浏览器加密
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("ws_浏览器加密")
public class ReqBrowserEncrypt {

    @ApiModelProperty("clientId")
    @NotBlank(message = "clientId 不能为空")
    private String clientId;

    @ApiModelProperty("明文")
    @NotBlank(message = "明文不能为空")
    private String plaintext;
}