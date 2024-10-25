package com.lmyxlf.jian_mu.admin.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/31 2:29
 * @description
 * @since 17
 */
public interface CommonService {

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    void fileDownload(String fileName, Boolean delete, HttpServletResponse response);

    /**
     * 通用上传请求（单个）
     *
     * @param file
     * @return
     */
    Map<String, String> uploadFile(MultipartFile file);

    /**
     * 通用上传请求（多个）
     *
     * @param files
     * @return
     */
    Map<String, String> uploadFiles(List<MultipartFile> files);

    /**
     * 本地资源通用下载
     *
     * @param resource
     * @param response
     */
    void resourceDownload(String resource, HttpServletResponse response);
}