package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.dao.SysRoleDao;
import com.lmyxlf.jian_mu.admin.model.dto.LoginUserDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.*;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysDept;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysRole;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysUser;
import com.lmyxlf.jian_mu.admin.model.resp.*;
import com.lmyxlf.jian_mu.admin.service.*;
import com.lmyxlf.jian_mu.admin.util.ExcelUtil;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import com.lmyxlf.jian_mu.global.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/10/1 0:48
 * @description
 * @since 17
 */
@Slf4j
@Service("sysRoleService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    private final SysRoleMenuService sysRoleMenuService;
    private final SysRoleDao sysRoleDao;
    private final SysPermissionService sysPermissionService;
    private final TokenService tokenService;
    private final SysRoleDeptService sysRoleDeptService;
    private final SysUserRoleService sysUserRoleService;
    private final SysDeptService sysDeptService;

    @Override
    public Page<RespSysRole> list(ReqSysRole reqSysRole) {

        Integer current = reqSysRole.getPage();
        Integer size = reqSysRole.getSize();

        Page<SysRole> page = new Page<>(current, size);
        List<SysRole> sysRoleList = sysRoleDao.selectRoleList(reqSysRole, page);


        // 仅为将返回对象转为 Resp
        List<RespSysRole> respSysRoleList = sysRoleList.stream()
                .map(item -> {
                    RespSysRole respSysRole = new RespSysRole();
                    BeanUtils.copyProperties(item, respSysRole);
                    return respSysRole;
                }).toList();

        Page<RespSysRole> result = new Page<>();
        result
                .setRecords(respSysRoleList)
                .setCurrent(page.getCurrent())
                .setSize(page.getSize())
                .setTotal(page.getTotal());

        return result;
    }

    @Override
    public void export(ReqSysRole reqSysRole, HttpServletResponse response) {

        List<SysRole> list = sysRoleDao.selectRoleList(reqSysRole, null);
        ExcelUtil<SysRole> util = new ExcelUtil<>(SysRole.class);
        util.exportExcel(response, list, "角色数据");
    }

    @Override
    public RespSysRole getInfo(Integer id) {

        checkRoleDataScope(id);
        SysRole sysRole = this.lambdaQuery()
                .eq(SysRole::getId, id)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();

        RespSysRole respSysRole = new RespSysRole();
        BeanUtils.copyProperties(sysRole, respSysRole);

        return respSysRole;
    }

    @Override
    public Boolean add(ReqSysRole reqSysRole) {

        List<Integer> menuIds = reqSysRole.getMenuIds();

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(reqSysRole, sysRole);
        this.save(sysRole);

        if (CollUtil.isNotEmpty(menuIds)) {

            List<SysRoleMenu> sysRoleMenuList = menuIds.stream().map(item -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(sysRole.getId());
                sysRoleMenu.setMenuId(item);
                return sysRoleMenu;
            }).toList();
            sysRoleMenuService.saveBatch(sysRoleMenuList);
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean edit(ReqSysRole reqSysRole) {

        Integer id = reqSysRole.getId();

        checkRoleAllowed(reqSysRole);
        checkRoleDataScope(id);

        if (updateRole(reqSysRole)) {

            // 更新缓存用户权限
            LoginUserDTO loginUserDTO = SecurityUtil.getLoginUser();
            if (ObjUtil.isNotNull(loginUserDTO.getSysUserDTO()) && !loginUserDTO.getSysUserDTO().isAdmin()) {

                loginUserDTO.setPermissions(sysPermissionService.getMenuPermission(loginUserDTO.getSysUserDTO()));

                SysUser sysUser = SpringContextHolder.getBean(SysUserService.class).lambdaQuery()
                        .eq(SysUser::getUserName, loginUserDTO.getSysUserDTO().getUserName())
                        .eq(SysUser::getStatus, UserConstant.NORMAL)
                        .eq(SysUser::getDeleteTime, DBConstant.INITIAL_TIME)
                        .one();
                SysUserDTO sysUserDTO = new SysUserDTO();
                BeanUtils.copyProperties(sysUser, sysUserDTO);
                loginUserDTO.setSysUserDTO(sysUserDTO);

                tokenService.setLoginUser(loginUserDTO);
            }

            return Boolean.TRUE;
        }

        log.warn("修改角色失败，ReqSysRole：{}", reqSysRole);
        throw new LmyXlfException("修改角色失败，请联系管理员");
    }

    @Override
    public Boolean dataScope(ReqSysRole reqSysRole) {

        Integer id = reqSysRole.getId();
        Integer dataScope = reqSysRole.getDataScope();
        List<Integer> deptIds = reqSysRole.getDeptIds();

        checkRoleAllowed(reqSysRole);
        checkRoleDataScope(id);

        // 修改角色信息
        this.lambdaUpdate()
                .set(SysRole::getDataScope, dataScope)
                .set(SysRole::getId, id)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();

        // 删除角色与部门关联
        sysRoleDeptService.lambdaUpdate()
                .set(SysRoleDept::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysRoleDept::getRoleId, id)
                .eq(SysRoleDept::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();

        if (CollUtil.isNotEmpty(deptIds)) {

            List<SysRoleDept> sysRoleMenuList = deptIds.stream().map(item -> {
                SysRoleDept sysRoleDept = new SysRoleDept();
                sysRoleDept
                        .setRoleId(id)
                        .setDeptId(item);
                return sysRoleDept;
            }).toList();
            sysRoleDeptService.saveBatch(sysRoleMenuList);
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean changeStatus(ReqSysRole reqSysRole) {

        Integer id = reqSysRole.getId();
        Integer status = reqSysRole.getStatus();

        checkRoleAllowed(reqSysRole);
        checkRoleDataScope(id);

        this.lambdaUpdate()
                .set(SysRole::getStatus, status)
                .set(SysRole::getId, id)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();

        return Boolean.TRUE;
    }

    @Override
    public Boolean remove(ReqSysRole reqSysRole) {

        List<Integer> ids = reqSysRole.getIds();

        ids.forEach(item -> {
            checkRoleAllowed(new ReqSysRole().setId(item));
            checkRoleDataScope(item);

            Integer count = sysUserRoleService.lambdaQuery()
                    .eq(SysUserRole::getRoleId, item)
                    .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                    .count();
            if (count > 0) {

                log.warn("该角色已分配,不能删除，reqSysRole：{}", reqSysRole);
                throw new LmyXlfException("该角色已分配,不能删除");
            }
        });

        // 删除角色与菜单关联
        sysRoleMenuService.lambdaUpdate()
                .set(SysRoleMenu::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysRoleMenu::getRoleId, ids)
                .eq(SysRoleMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
        // 删除角色与部门关联
        sysRoleDeptService.lambdaUpdate()
                .set(SysRoleDept::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysRoleDept::getRoleId, ids)
                .eq(SysRoleDept::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();

        return this.lambdaUpdate()
                .set(SysRole::getDeleteTime, LocalDateTimeUtil.now())
                .in(SysRole::getId, ids)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }

    @Override
    public List<RespSysRole> optionselect() {

        List<SysRole> sysRoleList = sysRoleDao.selectRoleList(new ReqSysRole(), null);

        // 仅为将返回对象转为 Resp
        return sysRoleList.stream()
                .map(item -> {
                    RespSysRole respSysRole = new RespSysRole();
                    BeanUtils.copyProperties(item, respSysRole);
                    return respSysRole;
                }).toList();
    }

    @Override
    public Page<RespSysUser> allocatedList(ReqSysUser reqSysUser) {

        Page<SysUser> sysUserPage = SpringContextHolder.getBean(SysUserService.class).selectAllocatedList(reqSysUser);
        long current = sysUserPage.getCurrent();
        long size = sysUserPage.getSize();
        long total = sysUserPage.getTotal();

        List<RespSysUser> respSysUserList = sysUserPage.getRecords().stream().map(item -> {
            RespSysUser respSysUser = new RespSysUser();
            BeanUtils.copyProperties(item, respSysUser);
            return respSysUser;
        }).toList();

        return new Page<RespSysUser>(current, size, total).setRecords(respSysUserList);
    }

    @Override
    public Page<RespSysUser> unallocatedList(ReqSysUser reqSysUser) {

        Page<SysUser> sysUserPage = SpringContextHolder.getBean(SysUserService.class).selectUnallocatedList(reqSysUser);
        long current = sysUserPage.getCurrent();
        long size = sysUserPage.getSize();
        long total = sysUserPage.getTotal();

        List<RespSysUser> respSysUserList = sysUserPage.getRecords().stream().map(item -> {
            RespSysUser respSysUser = new RespSysUser();
            BeanUtils.copyProperties(item, respSysUser);
            return respSysUser;
        }).toList();

        return new Page<RespSysUser>(current, size, total).setRecords(respSysUserList);
    }

    @Override
    public Boolean cancelAuthUser(ReqSysRole reqSysRole) {

        Integer roleId = reqSysRole.getId();
        Integer userId = reqSysRole.getUserId();

        return sysUserRoleService.lambdaUpdate()
                .set(SysUserRole::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getRoleId, roleId)
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }

    @Override
    public Boolean cancelAuthUserAll(ReqSysRole reqSysRole) {

        Integer roleId = reqSysRole.getId();
        List<Integer> userIds = reqSysRole.getUserIds();

        return sysUserRoleService.lambdaUpdate()
                .set(SysUserRole::getDeleteTime, LocalDateTimeUtil.now())
                .in(SysUserRole::getUserId, userIds)
                .eq(SysUserRole::getRoleId, roleId)
                .eq(SysUserRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }

    @Override
    public Boolean selectAuthUserAll(ReqSysRole reqSysRole) {

        Integer roleId = reqSysRole.getId();
        List<Integer> userIds = reqSysRole.getUserIds();

        checkRoleDataScope(roleId);
        List<SysUserRole> sysUserRoleList = userIds.stream().map(item -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole
                    .setRoleId(roleId)
                    .setUserId(item);
            return sysUserRole;
        }).toList();

        return sysUserRoleService.saveBatch(sysUserRoleList);
    }

    @Override
    public RespDeptTree deptTree(Integer id) {

        List<Integer> checkedKeys = sysDeptService.selectDeptListByRoleId(id);
        List<RespTreeSelect> depts = sysDeptService.selectDeptTreeList(new ReqSysDept());

        RespDeptTree respDeptTree = new RespDeptTree();
        respDeptTree
                .setCheckedKeys(checkedKeys)
                .setDepts(depts);

        return respDeptTree;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param reqSysRole 角色信息
     */
    public void checkRoleAllowed(ReqSysRole reqSysRole) {

        Optional.ofNullable(reqSysRole)
                .filter(ObjUtil::isNotNull)
                .map(ReqSysRole::getId)
                .filter(ObjUtil::isNotNull)
                .filter(id -> reqSysRole.isAdmin())
                .ifPresent(role -> {
                    throw new LmyXlfException("不允许操作超级管理员角色");
                });
    }

    @Override
    public void checkRoleDataScope(Integer... roleIds) {

        if (!SysUserDTO.isAdmin(SecurityUtil.getUserId())) {

            for (Integer roleId : roleIds) {

                ReqSysRole reqSysRole = new ReqSysRole();
                reqSysRole.setId(roleId);
                List<SysRole> roles = sysRoleDao.selectRoleList(reqSysRole, null);
                if (CollUtil.isEmpty(roles)) {

                    throw new LmyXlfException("没有权限访问角色数据");
                }
            }
        }
    }

    @Override
    public List<SysRole> selectRoleList(ReqSysRole reqSysRole, Page<SysRole> page) {

        return sysRoleDao.selectRoleList(reqSysRole, page);
    }

    /**
     * 修改保存角色信息
     *
     * @param reqSysRole 角色信息
     * @return 结果
     */
    @Transactional
    public Boolean updateRole(ReqSysRole reqSysRole) {

        Integer id = reqSysRole.getId();
        List<Integer> menuIds = reqSysRole.getMenuIds();

        // 修改角色信息
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(reqSysRole, sysRole);
        this.updateById(sysRole);
        // 删除角色与菜单关联
        sysRoleMenuService.lambdaUpdate()
                .set(SysRoleMenu::getDeleteTime, LocalDateTimeUtil.now())
                .eq(SysRoleMenu::getRoleId, id)
                .eq(SysRoleMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();

        // 新增角色和部门信息（数据权限）
        if (CollUtil.isNotEmpty(menuIds)) {

            List<SysRoleMenu> sysRoleMenuList = menuIds.stream().map(item -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu
                        .setRoleId(id)
                        .setMenuId(item);
                return sysRoleMenu;
            }).toList();
            sysRoleMenuService.saveBatch(sysRoleMenuList);
        }

        return Boolean.TRUE;
    }
}