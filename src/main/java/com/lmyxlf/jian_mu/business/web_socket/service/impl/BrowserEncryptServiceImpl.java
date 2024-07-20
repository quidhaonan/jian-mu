package com.lmyxlf.jian_mu.business.web_socket.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.lmyxlf.jian_mu.business.web_socket.constant.WSConstant;
import com.lmyxlf.jian_mu.business.web_socket.constant.WSExceptionEnum;
import com.lmyxlf.jian_mu.business.web_socket.model.req.ReqBrowserEncrypt;
import com.lmyxlf.jian_mu.business.web_socket.model.resp.RespBrowserEncrypt;
import com.lmyxlf.jian_mu.business.web_socket.service.BrowserEncryptService;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsManager;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.WsStore;
import com.lmyxlf.jian_mu.business.web_socket.ws_app.cmd.WsBaseCmd;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.ServiceException;
import com.lmyxlf.jian_mu.global.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/20 1:04
 * @description 浏览器加密
 * @since 17
 */
@Slf4j
@Service("browserEncryptService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BrowserEncryptServiceImpl implements BrowserEncryptService {

    private final RedissonClient redissonClient;

    /**
     * 浏览器加密
     *
     * @param reqBrowserEncrypt
     * @return
     */
    @Override
    public RespBrowserEncrypt browserEncrypt(ReqBrowserEncrypt reqBrowserEncrypt) {
        log.info("浏览器加密，reqBrowserEncrypt：{}", reqBrowserEncrypt);
        String clientId = reqBrowserEncrypt.getClientId();
        // 返回值赋值
        RespBrowserEncrypt respBrowserEncrypt = new RespBrowserEncrypt();
        BeanUtils.copyProperties(reqBrowserEncrypt, respBrowserEncrypt);

        WsStore wsStore = WsManager.getStore(clientId);
        if (ObjUtil.isNull(wsStore)) {
            log.error("浏览器已下线，reqBrowserEncrypt：{}", reqBrowserEncrypt);
            throw new ServiceException(WSExceptionEnum.BROWSER_OFFLINE.getMsg());
        }

        // 存储 redis
        String randomStr = RandomUtil.generateRandomStr(9);
        String redisKey = WSConstant.REDIS_BROWSER_ENCRYPT_PREFIX + randomStr;
        respBrowserEncrypt.setRandomStr(randomStr);
        RBucket<RespBrowserEncrypt> rBucket = redissonClient.getBucket(redisKey);
        rBucket.set(respBrowserEncrypt, WSConstant.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);

        // 等待浏览器返回密文
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(randomStr);
        countDownLatch.trySetCount(1);

        // 发送明文至浏览器加密
        WsManager.sendOK(wsStore.getSession(), new WsBaseCmd(), respBrowserEncrypt);
        try {
            boolean completed = countDownLatch.await(WSConstant.COUNTDOWNLATCH_AWAIT_TIME, TimeUnit.SECONDS);
            if (!completed) {
                log.error("countDownLatch 等待超时，respBrowserEncrypt：{}", respBrowserEncrypt);
                throw new ServiceException(WSExceptionEnum.BROWSER_TIMEOUT.getMsg());
            }
        } catch (InterruptedException e) {
            log.error("countDownLatch 出现错误，respBrowserEncrypt：{}", respBrowserEncrypt);
            throw new ServiceException(CODE_MSG.ERROR);
        }

        // 获得最新 redis 值
        respBrowserEncrypt = rBucket.get();

        return respBrowserEncrypt;
    }
}