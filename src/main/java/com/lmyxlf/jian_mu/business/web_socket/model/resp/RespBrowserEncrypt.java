package com.lmyxlf.jian_mu.business.web_socket.model.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 1:51
 * @description 浏览器加密
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel("ws_浏览器加密")
public class RespBrowserEncrypt {

    @ApiModelProperty("clientId")
    private String clientId;

    @ApiModelProperty("明文")
    private String plaintext;

    @ApiModelProperty("密文")
    private String ciphertext;

    @JsonIgnore
    @ApiModelProperty("随机字符串，作为 countDownLatch key，存储 redis 值时加上前缀")
    private String randomStr;
}