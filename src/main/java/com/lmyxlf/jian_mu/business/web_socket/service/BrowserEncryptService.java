package com.lmyxlf.jian_mu.business.web_socket.service;

import com.lmyxlf.jian_mu.business.web_socket.model.req.ReqBrowserEncrypt;
import com.lmyxlf.jian_mu.business.web_socket.model.resp.RespBrowserEncrypt;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 1:03
 * @description 浏览器加密
 * @since 17
 */
public interface BrowserEncryptService {

    RespBrowserEncrypt browserEncrypt(ReqBrowserEncrypt reqBrowserEncrypt);
}