package com.lmyxlf.jian_mu.business.batch_invitation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/23 1:31
 * @description
 * @since 17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FtpClientDto {

    /**
     * FTP 访问域名
     */
    private String domain;

    /**
     * ftp 地址
     */
    private String host;

    /**
     * ftp 端口
     */
    private Integer port;

    /**
     * ftp 账号
     */
    private String username;

    /**
     * ftp 密码
     */
    private String password;
}