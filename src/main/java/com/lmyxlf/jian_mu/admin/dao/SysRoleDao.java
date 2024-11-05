package com.lmyxlf.jian_mu.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lmyxlf.jian_mu.admin.annotation.DataScope;
import com.lmyxlf.jian_mu.admin.model.entity.SysRole;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysRole;
import com.lmyxlf.jian_mu.admin.model.resp.RespSysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:47
 * @description
 * @since 17
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRole> {

    /**
     * 根据条件分页查询角色数据
     *
     * @param reqSysRole 角色信息
     * @return 角色数据集合信息
     */
    @DataScope(deptAlias = "dept")
    IPage<RespSysRole> selectRoleList(@Param("reqSysRole") ReqSysRole reqSysRole, @Param("page") Page<SysRole> page);
}