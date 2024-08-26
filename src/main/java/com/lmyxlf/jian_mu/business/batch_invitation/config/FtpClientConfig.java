package com.lmyxlf.jian_mu.business.batch_invitation.config;

import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.FtpClient;
import com.lmyxlf.jian_mu.business.batch_invitation.service.FtpClientService;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.util.FtpClientUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/23 20:53
 * @description 根据数据库配置注入多个 FTP bean 对象
 * @since 17
 */
@Slf4j
@Configuration
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FtpClientConfig implements InitializingBean, ApplicationContextAware {

    private final FtpClientService ftpClientService;
    private ApplicationContext context;

    @Override
    public void afterPropertiesSet() {

        List<FtpClient> list = ftpClientService.lambdaQuery()
                .eq(FtpClient::getIsDelete, DBConstant.NOT_DELETED)
                .list();
        log.info("注入多个 FTP bean 对象，list：{}", list);

        ConfigurableListableBeanFactory beanFactory =
                ((ConfigurableApplicationContext) context).getBeanFactory();
        list.forEach(item -> {
            FTPClient ftpClient = FtpClientUtil.connect(item.getHost(),
                    item.getPort(),
                    item.getUsername(),
                    item.getPassword());
            beanFactory.registerSingleton(item.getName(), ftpClient);
        });
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}