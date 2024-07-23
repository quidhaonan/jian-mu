package com.lmyxlf.jian_mu.business.web_socket.ws_app.handler.impl;

import cn.hutool.core.util.ObjUtil;
import com.lmyxlf.jian_mu.business.web_socket.constant.WSConstant;
import com.lmyxlf.jian_mu.business.web_socket.constant.WSExceptionEnum;
import com.lmyxlf.jian_mu.business.web_socket.model.resp.RespBrowserEncrypt;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsManager;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsServerEndpoint;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsStore;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsBaseCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsCmdType;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.impl.BrowserEncryptCmd;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.handler.WsHandler;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.Session;
import java.util.concurrent.TimeUnit;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 3:08
 * @description 浏览器加密
 * @since 17
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrowserEncryptHandler implements WsHandler<BrowserEncryptCmd> {

    private static final WsCmdType CMD_TYPE = WsCmdType.BROWSER_ENCRYPT;

    private final RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        WsServerEndpoint.registerHandler(CMD_TYPE, this);
    }

    @Override
    public Object handle(BrowserEncryptCmd cmd, Session session, WsBaseCmd wsWebBaseCmd) {
        log.info("浏览器发送密文，cmd：{}，wsWebBaseCmd：{}", cmd, wsWebBaseCmd);
        String clientId = cmd.getClientId();
        String randomStr = cmd.getRandomStr();
        String ciphertext = cmd.getCiphertext();
        String redisKey = WSConstant.REDIS_BROWSER_ENCRYPT_PREFIX + randomStr;

        WsStore wsStore = WsManager.getStore(clientId);
        if (ObjUtil.isNull(wsStore)) {
            log.error("浏览器已下线，cmd：{}，wsWebBaseCmd：{}", cmd, wsWebBaseCmd);
            throw new LmyXlfException(WSExceptionEnum.BROWSER_OFFLINE.getMsg());
        }

        // 获得 redis 数据
        RBucket<RespBrowserEncrypt> rBucket = redissonClient.getBucket(redisKey);
        RespBrowserEncrypt respBrowserEncrypt = rBucket.get();
        if (ObjUtil.isNull(respBrowserEncrypt)) {
            log.error("redis 数据为空，加密失败，redisKey：{}", redisKey);
            throw new LmyXlfException(CODE_MSG.ERROR.getMsg());
        }

        // 赋值 redis 数据的密文并减少计数
        respBrowserEncrypt.setCiphertext(ciphertext);
        // 更新 redis 数据
        rBucket.set(respBrowserEncrypt, WSConstant.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
        // 放行加密请求
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(WSConstant.REDIS_COUNTDOWNLATCH_PREFIX + randomStr);
        countDownLatch.countDown();

        return null;
    }
}