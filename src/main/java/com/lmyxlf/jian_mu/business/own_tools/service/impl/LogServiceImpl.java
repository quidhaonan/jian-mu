package com.lmyxlf.jian_mu.business.own_tools.service.impl;

import com.lmyxlf.jian_mu.business.own_tools.constant.OTConstant;
import com.lmyxlf.jian_mu.business.own_tools.service.LogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/1 2:27
 * @description
 * @since 17
 */
@Slf4j
@Service("logService")
@AllArgsConstructor(onConstructor_ = @Autowired)
public class LogServiceImpl implements LogService {

    @Override
    public void getLatest(HttpServletResponse response) {

        // 设置响应内容类型
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"jian_mu.log\"");

        String path = OTConstant.PRIMARY_PATH + OTConstant.LOG_PATH;

        // 使用 try-with-resources 确保所有资源都被正确关闭
        try (FileInputStream fileInputStream = new FileInputStream(Paths.get(path).toString());
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            // 读取文件并写入响应输出流
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush(); // 确保所有内容都被写出
        } catch (IOException e) {
            log.error("读取文件出错：e：{}", e.getMessage());
        }
    }
}