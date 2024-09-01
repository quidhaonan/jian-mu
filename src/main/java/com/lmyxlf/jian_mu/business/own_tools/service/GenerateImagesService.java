package com.lmyxlf.jian_mu.business.own_tools.service;

import com.lmyxlf.jian_mu.business.own_tools.model.req.ReqGenerateImages;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 21:36
 * @description
 * @since 17
 */
public interface GenerateImagesService {

    void generateImages(ReqGenerateImages reqGenerateImages, MultipartFile file, HttpServletResponse response) throws IOException;

    void generateImagesExcel(MultipartFile file, MultipartFile excel, HttpServletResponse response) throws IOException;
}