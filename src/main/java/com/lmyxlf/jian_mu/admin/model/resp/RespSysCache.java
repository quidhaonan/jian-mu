package com.lmyxlf.jian_mu.admin.model.resp;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/4 22:25
 * @description
 * @since 17
 */
@Data
@ApiModel("缓存信息")
@Accessors(chain = true)
public class RespSysCache {

    @ApiModelProperty("缓存名称")
    private String cacheName;

    @ApiModelProperty("缓存键名")
    private String cacheKey;

    @ApiModelProperty("缓存内容")
    private String cacheValue;

    @ApiModelProperty("备注")
    private String remark;

    public RespSysCache(String cacheName, String remark) {

        this.cacheName = cacheName;
        this.remark = remark;
    }

    public RespSysCache(String cacheName, String cacheKey, String cacheValue) {

        this.cacheName = StrUtil.replace(cacheName, ":", "");
        this.cacheKey = StrUtil.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }
}