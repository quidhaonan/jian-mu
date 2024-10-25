package com.lmyxlf.jian_mu.admin.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/21 17:19
 * @description
 * @since 17
 */
@RestController
@Api(tags = "首页")
@RequestMapping("${jian_mu.admin.urlPrefix}/system/dict/type")
public class SysIndexController {

    @Value("${spring.application.name}")
    private String serverName;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {

        return StrUtil.format("欢迎使用{}后台管理框架，需通过前端地址访问", serverName);
    }
}