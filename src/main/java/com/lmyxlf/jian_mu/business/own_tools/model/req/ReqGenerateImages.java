package com.lmyxlf.jian_mu.business.own_tools.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/28 21:37
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "批量复制图片入参")
public class ReqGenerateImages {

    @ApiModelProperty(value = "后缀")
    // @NotEmpty(message = "后缀不能为空")
    private List<String> suffixs = Arrays.asList("S", "M", "L", "2L", "3L");

    // @ApiModelProperty(value = "图片")
    // @NotNull(message = "压缩包不能为空")
    // private MultipartFile file;
}