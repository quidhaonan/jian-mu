package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 1:47
 * @description 图片工具类
 * @since 17
 */
@Slf4j
public class ImageUtil {

    /**
     * 判断是否是图片
     *
     * @param imageFile
     * @return
     */
    public static boolean isImage(MultipartFile imageFile) {
        log.info("判断是否是图片，imageFile：{}", imageFile.getOriginalFilename());

        if (ObjectUtil.isNull(imageFile) || imageFile.isEmpty()) {
            return Boolean.FALSE;
        }

        try {
            Image img = ImageIO.read(imageFile.getInputStream());
            return img != null && img.getWidth(null) > 0 && img.getHeight(null) > 0;
        } catch (Exception e) {
            log.error("判断是否是图片出错，imageFile：{}，e：{}", imageFile.getOriginalFilename(), e.getMessage());
            return Boolean.FALSE;
        }
    }
}