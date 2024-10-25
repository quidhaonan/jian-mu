package com.lmyxlf.jian_mu.admin.model.dto;

import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/13 2:18
 * @description 路由显示信息
 * @since 17
 */
@Data
@Accessors(chain = true)
public class MetaDTO {

    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径 src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为 true，则不会被 <keep-alive> 缓存
     */
    private boolean noCache;

    /**
     * 内链地址（http(s):// 开头）
     */
    private String link;

    public MetaDTO() {

    }

    public MetaDTO(String title, String icon) {

        this.title = title;
        this.icon = icon;
    }

    public MetaDTO(String title, String icon, String link) {

        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaDTO(String title, String icon, boolean noCache, String link) {

        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        if (StringUtils.startsWithAny(link, AdminConstant.HTTP, AdminConstant.HTTPS)) {

            this.link = link;
        }
    }
}