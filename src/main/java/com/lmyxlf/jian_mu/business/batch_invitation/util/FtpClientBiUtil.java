package com.lmyxlf.jian_mu.business.batch_invitation.util;

import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.business.batch_invitation.model.enums.FtpClientType;
import com.lmyxlf.jian_mu.global.util.FtpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 23:54
 * @description FTP 服务工具类
 * @since 17
 */
@Slf4j
@Component
public class FtpClientBiUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 获得 FTP 服务
     *
     * @param ftpClientType
     * @return
     */
    public static FTPClient getFtpClient(FtpClientType ftpClientType) {

        return getFtpClient(ftpClientType, null);
    }

    /**
     * 获得 FTP 服务，并切换工作目录
     *
     * @param ftpClientType
     * @return
     */
    public static FTPClient getFtpClient(FtpClientType ftpClientType, String basePath) {
        log.info("获得 FTP 服务，并切换工作目录，ftpClientType：{}，basePath：{}", ftpClientType, basePath);

        FTPClient ftpClient = (FTPClient) context.getBean(ftpClientType.name());
        FtpClientUtil.switchDirectory(
                ftpClient,
                StrUtil.isEmpty(basePath) ? "/" : basePath,
                true);
        return ftpClient;
    }

    /**
     * 根据 type，即 ftp_client 表主键 id 获得 FTP 服务
     *
     * @param type
     * @return
     */
    public static FTPClient getFtpClient(Integer type) {

        FtpClientType ftpClientTypeByType = FtpClientType.getFtpClientTypeByType(type);
        return getFtpClient(ftpClientTypeByType);
    }

    /**
     * 根据 type，即 ftp_client 表主键 id 获得 FTP 服务，并切换工作目录
     *
     * @param type
     * @return
     */
    public static FTPClient getFtpClient(Integer type, String basePath) {

        FtpClientType ftpClientTypeByType = FtpClientType.getFtpClientTypeByType(type);
        return getFtpClient(ftpClientTypeByType, basePath);
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}