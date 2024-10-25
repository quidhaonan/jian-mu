package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:31
 * @description 缓存监控信息返回体
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespMonitorCache {

    private Properties info;

    private Long dbSize;

    private List<Map<String, String>> commandStats;
}