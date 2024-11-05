package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.model.resp.RespServer;
import com.lmyxlf.jian_mu.admin.service.ServerService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/6 0:52
 * @description
 * @since 17
 */
@RestController
@Api(tags = "服务器监控")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/monitor/server")
public class ServerController {

    private final ServerService serverService;

    @GetMapping()
    @PreAuthorize("@permissionService.hasPermi('monitor:server:list')")
    public LmyXlfResult<RespServer> getInfo() throws Exception {

        return LmyXlfResult.ok(serverService.getInfo());
    }
}