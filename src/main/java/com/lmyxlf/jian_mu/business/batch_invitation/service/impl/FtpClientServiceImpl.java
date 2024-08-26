package com.lmyxlf.jian_mu.business.batch_invitation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.business.batch_invitation.dao.FtpClientDao;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.FtpClient;
import com.lmyxlf.jian_mu.business.batch_invitation.service.FtpClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 22:23
 * @description
 * @since 17
 */
@Slf4j
@Service("ftpClientService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FtpClientServiceImpl extends ServiceImpl<FtpClientDao, FtpClient> implements FtpClientService {

}