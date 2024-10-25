package com.lmyxlf.jian_mu.admin.util;

import com.lmyxlf.jian_mu.admin.config.AdminConfig;
import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/12 1:06
 * @description 图片处理工具类
 * @since 17
 */
@Slf4j
@Component
public class ImageUtil {

    public static byte[] getImage(String imagePath) {

        InputStream is = getFile(imagePath);
        try {

            return IOUtils.toByteArray(is);
        } catch (Exception e) {

            log.error("图片加载异常 {}", e.getMessage());
            return null;
        } finally {

            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath) {

        try {

            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {

            log.error("获取图片异常 {}", e.getMessage());
        }

        return null;
    }

    /**
     * 读取文件为字节数据
     *
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url) {

        InputStream in = null;
        try {

            if (url.startsWith("http")) {

                // 网络地址
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            } else {

                // 本机地址
                String localPath = AdminConfig.getProfile();
                String downloadPath = localPath + StringUtils.substringAfter(url, AdminConstant.RESOURCE_PREFIX);
                in = new FileInputStream(downloadPath);
            }

            return IOUtils.toByteArray(in);
        } catch (Exception e) {

            log.error("获取文件路径异常 {}", e.getMessage());
            return null;
        } finally {

            IOUtils.closeQuietly(in);
        }
    }
}