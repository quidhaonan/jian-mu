package com.lmyxlf.jian_mu.business.raising_numbers.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/25 19:37
 * @description 随机生成 User-Agent 请求返回体
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespUserAgent {

    private String code;

    private List<String> data;

    private String msg;
}