package com.lmyxlf.jian_mu.business.own_tools.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/2 0:04
 * @description
 * @since 17
 */
@Data
@ApiModel("批量获得 ASIN")
public class AsinDto {

    @ExcelProperty(value = "ASIN")
    @ApiModelProperty(value = "ASIN")
    private String asin;
}