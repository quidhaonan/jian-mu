package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import com.google.code.kaptcha.Producer;
import com.lmyxlf.jian_mu.admin.config.AdminConfig;
import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.service.CaptchaService;
import com.lmyxlf.jian_mu.admin.service.SysConfigService;
import com.lmyxlf.jian_mu.global.constant.CODE_MSG;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/31 2:16
 * @description
 * @since 17
 */
@Slf4j
@Service("captchaService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CaptchaServiceImpl implements CaptchaService {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final SysConfigService sysConfigService;

    @Override
    public Map<String, Object> getCode() {

        Map<String, Object> result = new HashMap<>();
        Boolean captchaEnabled = sysConfigService.selectCaptchaEnabled();
        result.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {

            return result;
        }

        // 保存验证码信息
        String uuid = UUID.fastUUID().toString(true);
        String verifyKey = CacheConstant.CAPTCHA_CODE_KEY + uuid;

        String capStr, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = AdminConfig.getCaptchaType();
        if ("math".equals(captchaType)) {

            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {

            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        RedisUtil.set(verifyKey, code, AdminConstant.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            assert image != null;
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            throw new LmyXlfException(CODE_MSG.ERROR);
        }

        result.put("uuid", uuid);
        result.put("img", Base64.encode(os.toByteArray()));

        return result;
    }
}