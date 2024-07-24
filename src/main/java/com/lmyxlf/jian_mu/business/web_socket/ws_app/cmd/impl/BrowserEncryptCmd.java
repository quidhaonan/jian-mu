package com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmd;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 2:53
 * @description 浏览器加密
 * @since 17
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrowserEncryptCmd implements WsCmd {

    /**
     * clientId
     */
    private String clientId;

    /**
     * 明文
     */
    private String plaintext;

    /**
     * 密文
     */
    private String ciphertext;

    /**
     * 随机字符串，作为 countDownLatch key，存储 redis 值时加上前缀
     */
    private String randomStr;
}