package com.lmyxlf.jian_mu.admin.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 0:13
 * @description 自动添加数据库通用字段
 * @since 17
 */
public class AutoMetaFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        metaObject.setValue("create_user", SecurityUtil.getLoginUser().getUsername());
        metaObject.setValue("create_time", DateUtil.now());
        metaObject.setValue("update_user", SecurityUtil.getLoginUser().getUsername());
        metaObject.setValue("update_time", DateUtil.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        metaObject.setValue("update_user", SecurityUtil.getLoginUser().getUsername());
        metaObject.setValue("update_time", DateUtil.now());
    }
}