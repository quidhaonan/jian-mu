package com.lmyxlf.jian_mu.admin.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 13:03
 * @description 头像上传返回体
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespUploadAvatar {

    private String imgUrl;
}