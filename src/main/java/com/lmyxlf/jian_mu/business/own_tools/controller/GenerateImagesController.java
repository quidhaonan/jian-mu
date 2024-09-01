package com.lmyxlf.jian_mu.business.own_tools.controller;

import com.lmyxlf.jian_mu.business.own_tools.model.req.ReqGenerateImages;
import com.lmyxlf.jian_mu.business.own_tools.service.GenerateImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 21:35
 * @description
 * @since 17
 */
@RestController
@Api(tags = "批量复制图片，添加名称后缀")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.own_tools.urlPrefix}/generate")
public class GenerateImagesController {

    private final GenerateImagesService generateImagesService;

    @PostMapping("/images")
    @ApiOperation(value = "批量复制图片，插入指定名称")
    public void generateImages(@Validated ReqGenerateImages reqGenerateImages, @RequestPart MultipartFile file, HttpServletResponse response) throws IOException {

        generateImagesService.generateImages(reqGenerateImages, file, response);
    }

    @PostMapping("/images/excel")
    @ApiOperation(value = "批量复制图片，插入指定名称")
    public void generateImagesExecl(@RequestPart MultipartFile file, @RequestPart MultipartFile excel, HttpServletResponse response) throws IOException {

        generateImagesService.generateImagesExcel(file, excel, response);
    }
}