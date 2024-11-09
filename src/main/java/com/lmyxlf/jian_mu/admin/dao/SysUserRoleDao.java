package com.lmyxlf.jian_mu.admin.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lmyxlf.jian_mu.admin.model.entity.SysUserRole;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 2:24
 * @description
 * @since 17
 */
@Mapper
@DS(DBConstant.ADMIN)
public interface SysUserRoleDao extends BaseMapper<SysUserRole> {

}