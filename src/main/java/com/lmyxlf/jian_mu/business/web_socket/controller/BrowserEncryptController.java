package com.lmyxlf.jian_mu.business.web_socket.controller;

import com.lmyxlf.jian_mu.business.web_socket.model.req.ReqBrowserEncrypt;
import com.lmyxlf.jian_mu.business.web_socket.model.resp.RespBrowserEncrypt;
import com.lmyxlf.jian_mu.business.web_socket.service.BrowserEncryptService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 0:32
 * @description 浏览器加密
 * @since 17
 */
@Slf4j
@RestController
@Api(tags = "ws_浏览器加密")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.web_socket.urlPrefix}/browser")
public class BrowserEncryptController {

    private final BrowserEncryptService browserEncryptService;

    @PostMapping("/encrypt")
    @ApiOperation("ws_浏览器加密")
    public LmyXlfResult<RespBrowserEncrypt> browserEncrypt(@RequestBody @Validated ReqBrowserEncrypt reqBrowserEncrypt) {

        return LmyXlfResult.ok(browserEncryptService.browserEncrypt(reqBrowserEncrypt));
    }
}