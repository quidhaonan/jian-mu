package com.lmyxlf.jian_mu.global.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lmyxlf.jian_mu.global.constant.SysConstant;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/17 0:13
 * @description 自动添加数据库通用字段
 * @since 17
 */
@Order(0)
public class AutoMetaFieldHandler implements MetaObjectHandler {

    private static final String CREATE_USER = "create_user";
    private static final String CREATE_TIME = "create_time";
    private static final String UPDATE_USER = "update_user";
    private static final String UPDATE_TIME = "update_time";

    @Override
    public void insertFill(MetaObject metaObject) {

        LocalDateTime now = LocalDateTimeUtil.now();
        metaObject.setValue(CREATE_USER, SysConstant.LMYXLF);
        metaObject.setValue(CREATE_TIME, now);
        metaObject.setValue(UPDATE_USER, SysConstant.LMYXLF);
        metaObject.setValue(UPDATE_TIME, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        metaObject.setValue(UPDATE_USER, SysConstant.LMYXLF);
        metaObject.setValue(UPDATE_TIME, DateUtil.now());
    }
}