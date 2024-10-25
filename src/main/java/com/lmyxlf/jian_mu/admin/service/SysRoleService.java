package com.lmyxlf.jian_mu.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.admin.model.entity.SysRole;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysRole;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:48
 * @description
 * @since 17
 */
public interface SysRoleService extends IService<SysRole> {

    Page<RespSysRole> list(ReqSysRole reqSysRole);

    void export(ReqSysRole reqSysRole, HttpServletResponse response);

    RespSysRole getInfo(Integer id);

    Boolean add(ReqSysRole reqSysRole);

    Boolean edit(ReqSysRole reqSysRole);

    Boolean dataScope(ReqSysRole reqSysRole);

    Boolean changeStatus(ReqSysRole reqSysRole);

    Boolean remove(ReqSysRole reqSysRole);

    List<RespSysRole> optionselect();

    Page<RespSysUser> allocatedList(ReqSysUser reqSysUser);

    Page<RespSysUser> unallocatedList(ReqSysUser reqSysUser);

    Boolean cancelAuthUser(ReqSysRole reqSysRole);

    Boolean cancelAuthUserAll(ReqSysRole reqSysRole);

    Boolean selectAuthUserAll(ReqSysRole reqSysRole);

    RespDeptTree deptTree(Integer id);

    /**
     * 校验角色是否有数据权限
     *
     * @param roleIds 角色 id
     */
    void checkRoleDataScope(Integer... roleIds);

    /**
     * 根据条件分页查询角色数据
     *
     * @param reqSysRole 角色信息
     * @return 角色数据集合信息
     */
    List<SysRole> selectRoleList(ReqSysRole reqSysRole, Page<SysRole> page);
}