package com.lmyxlf.jian_mu.business.own_tools.controller;

import com.lmyxlf.jian_mu.business.own_tools.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/1 2:24
 * @description 获得日志
 * @since 17
 */
@RestController
@Api(tags = "获得日志")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.own_tools.urlPrefix}/log")
public class LogController {

    private final LogService logService;

    @GetMapping("/latest")
    @ApiOperation(value = "获得最新日志")
    public void getLatest(HttpServletResponse response) {

        logService.getLatest(response);
    }
}