package com.lmyxlf.jian_mu.admin.service.impl;

import com.lmyxlf.jian_mu.admin.model.resp.RespServer;
import com.lmyxlf.jian_mu.admin.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/7 1:52
 * @description
 * @since 17
 */
@Slf4j
@Service("serverService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerServiceImpl implements ServerService {

    @Override
    public RespServer getInfo() throws Exception {

        RespServer respServer = new RespServer();
        respServer.copyTo();
        return respServer;
    }
}