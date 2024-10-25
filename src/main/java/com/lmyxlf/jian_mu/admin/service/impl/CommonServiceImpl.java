package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.admin.config.AdminConfig;
import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import com.lmyxlf.jian_mu.admin.service.CommonService;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/31 2:30
 * @description
 * @since 17
 */
@Slf4j
@Service("commonService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommonServiceImpl implements CommonService {

    private static final String FILE_DELIMETER = ",";

    @Override
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response) {

        try {

            if (!FileUtil.checkAllowDownload(fileName)) {

                log.warn("文件名称({})非法，不允许下载。 ", fileName);
                throw new Exception(StrUtil.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = AdminConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtil.setAttachmentResponseHeader(response, realFileName);
            FileUtil.writeBytes(filePath, response.getOutputStream());
            if (delete) {

                FileUtil.deleteFile(filePath);
            }
        } catch (Exception e) {

            log.error("下载文件失败", e.getMessage());
        }
    }

    @Override
    public Map<String, String> uploadFile(MultipartFile file) {

        try {

            Map<String, String> result = new HashMap<>();
            // 上传文件路径
            String filePath = AdminConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUtil.upload(filePath, file);
            result.put("fileName", fileName);
            result.put("newFileName", FileUtil.getName(fileName));
            result.put("originalFilename", file.getOriginalFilename());
            return result;
        } catch (Exception e) {

            log.error("文件上次失败，e：{}", e.getMessage());
            throw new LmyXlfException("文件上次失败");
        }
    }

    @Override
    public Map<String, String> uploadFiles(List<MultipartFile> files) {

        try {

            // 上传文件路径
            String filePath = AdminConfig.getUploadPath();
            List<String> fileNames = new ArrayList<>();
            List<String> newFileNames = new ArrayList<>();
            List<String> originalFilenames = new ArrayList<>();
            for (MultipartFile file : files) {

                // 上传并返回新文件名称
                String fileName = FileUtil.upload(filePath, file);
                fileNames.add(fileName);
                newFileNames.add(FileUtil.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            Map<String, String> result = new HashMap<>();
            result.put("fileNames", StrUtil.join(FILE_DELIMETER, fileNames));
            result.put("newFileNames", StrUtil.join(FILE_DELIMETER, newFileNames));
            result.put("originalFilenames", StrUtil.join(FILE_DELIMETER, originalFilenames));
            return result;
        } catch (Exception e) {

            log.error("文件上次失败，e：{}", e.getMessage());
            throw new LmyXlfException("文件上传失败");
        }
    }

    @Override
    public void resourceDownload(String resource, HttpServletResponse response) {

        try {

            if (!FileUtil.checkAllowDownload(resource)) {

                log.warn("资源文件({})非法，不允许下载。 ", resource);
                throw new Exception(StrUtil.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = AdminConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, AdminConstant.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtil.setAttachmentResponseHeader(response, downloadName);
            FileUtil.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {

            log.error("下载文件失败", e.getMessage());
        }
    }
}