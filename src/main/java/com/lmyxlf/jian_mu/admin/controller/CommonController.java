package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.service.CommonService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/31 2:28
 * @description
 * @since 17
 */
@RestController
@Api(tags = "通用请求处理")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/common")
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/download")
    @ApiOperation(value = "通用下载请求")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response) {

        commonService.fileDownload(fileName, delete, response);
    }

    @PostMapping("/upload")
    @ApiOperation(value = "通用上传请求（单个）")
    public LmyXlfResult<Map<String, String>> uploadFile(MultipartFile file) {

        return LmyXlfResult.ok(commonService.uploadFile(file));
    }

    @PostMapping("/uploads")
    @ApiOperation(value = "通用上传请求（多个）")
    public LmyXlfResult<Map<String, String>> uploadFiles(List<MultipartFile> files) {

        return LmyXlfResult.ok(commonService.uploadFiles(files));
    }

    @GetMapping("/download/resource")
    @ApiOperation(value = "本地资源通用下载")
    public void resourceDownload(String resource, HttpServletResponse response) {

        commonService.resourceDownload(resource, response);
    }
}