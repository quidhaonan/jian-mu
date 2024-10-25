package com.lmyxlf.jian_mu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lmyxlf.jian_mu.admin.constant.AdminConstant;
import com.lmyxlf.jian_mu.admin.constant.UserConstant;
import com.lmyxlf.jian_mu.admin.dao.SysMenuDao;
import com.lmyxlf.jian_mu.admin.model.dto.MetaDTO;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserDTO;
import com.lmyxlf.jian_mu.admin.model.entity.*;
import com.lmyxlf.jian_mu.admin.model.enums.MenuTypeEnum;
import com.lmyxlf.jian_mu.admin.model.req.ReqSysMenu;
import com.lmyxlf.jian_mu.admin.model.resp.*;
import com.lmyxlf.jian_mu.admin.service.*;
import com.lmyxlf.jian_mu.admin.util.SecurityUtil;
import com.lmyxlf.jian_mu.global.constant.DBConstant;
import com.lmyxlf.jian_mu.global.exception.LmyXlfException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/29 23:23
 * @description
 * @since 17
 */
@Slf4j
@Service("sysMenuService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {

    private final SysRoleMenuService sysRoleMenuService;
    private final SysMenuDao sysMenuDao;
    private final SysRoleService sysRoleService;

    @Override
    public List<RespSysMenu> list(ReqSysMenu reqSysMenu) {

        String menuName = reqSysMenu.getMenuName();
        Integer visible = reqSysMenu.getVisible();
        Integer status = reqSysMenu.getStatus();

        Integer userId = SecurityUtil.getUserId();
        List<SysMenu> sysMenuList;
        // 管理员显示所有菜单信息
        if (SysUserDTO.isAdmin(userId)) {

            sysMenuList = this.lambdaQuery()
                    .like(StrUtil.isNotEmpty(menuName), SysMenu::getMenuName, menuName)
                    .eq(ObjUtil.isNotNull(visible), SysMenu::getVisible, visible)
                    .eq(ObjUtil.isNotNull(status), SysMenu::getStatus, status)
                    .eq(SysMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getOrderNum)
                    .list();
        } else {

            reqSysMenu.setUserId(userId);
            sysMenuList = sysMenuDao.selectMenuListByUserId(reqSysMenu);
        }

        return sysMenuList.stream().map(item -> {
            RespSysMenu respSysMenu = new RespSysMenu();
            BeanUtils.copyProperties(item, respSysMenu);
            return respSysMenu;
        }).toList();
    }

    @Override
    public RespSysMenu getInfo(Integer id) {

        SysMenu sysMenu = this.lambdaQuery()
                .eq(SysMenu::getId, id)
                .eq(SysMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();

        RespSysMenu respSysMenu = new RespSysMenu();
        BeanUtils.copyProperties(sysMenu, respSysMenu);

        return respSysMenu;
    }

    @Override
    public List<RespTreeSelect> treeselect(ReqSysMenu reqSysMenu) {

        List<RespSysMenu> respSysMenuList = list(reqSysMenu);

        return buildMenuTreeSelect(respSysMenuList);
    }

    @Override
    public RespRoleMenuTree roleMenuTreeselect(Integer roleId) {

        RespRoleMenuTree respRoleMenuTree = new RespRoleMenuTree();

        List<RespSysMenu> respSysMenuList = list(new ReqSysMenu());
        List<RespTreeSelect> menus = buildMenuTreeSelect(respSysMenuList);

        SysRole sysRole = sysRoleService.lambdaQuery()
                .eq(SysRole::getId, roleId)
                .eq(SysRole::getDeleteTime, DBConstant.INITIAL_TIME)
                .one();
        if (ObjUtil.isNull(sysRole)) {

            log.warn("角色不存在，角色菜单列表树为空，roleId：{}", roleId);
            return respRoleMenuTree;
        }
        List<Integer> menusIds = sysMenuDao.selectMenuListByRoleId(roleId, sysRole.getMenuCheckStrictly());

        respRoleMenuTree.setMenus(menus)
                .setCheckedKeys(menusIds);
        return respRoleMenuTree;
    }

    @Override
    public Boolean add(ReqSysMenu reqSysMenu) {

        Integer isFrame = reqSysMenu.getIsFrame();
        String path = reqSysMenu.getPath();

        if (UserConstant.YES_FRAME.equals(isFrame) &&
                !StringUtils.startsWithAny(path, AdminConstant.HTTP, AdminConstant.HTTPS)) {

            log.warn("新增菜单失败，地址必须以http(s)://开头，reqSysMenu：{}", reqSysMenu);
            throw new LmyXlfException("新增菜单失败，地址必须以http(s)://开头");
        }

        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(reqSysMenu, sysMenu);

        return this.save(sysMenu);
    }

    @Override
    public Boolean edit(ReqSysMenu reqSysMenu) {

        Integer id = reqSysMenu.getId();
        Integer parentId = reqSysMenu.getParentId();
        Integer isFrame = reqSysMenu.getIsFrame();
        String path = reqSysMenu.getPath();

        if (UserConstant.YES_FRAME.equals(isFrame) &&
                !StringUtils.startsWithAny(path, AdminConstant.HTTP, AdminConstant.HTTPS)) {

            log.warn("新增菜单失败，地址必须以http(s)://开头，reqSysMenu：{}", reqSysMenu);
            throw new LmyXlfException("新增菜单失败，地址必须以http(s)://开头");
        }

        if (id.equals(parentId)) {

            log.warn("修改菜单失败，上级菜单不能选择自己，reqSysMenu：{}", reqSysMenu);
            throw new LmyXlfException("修改菜单失败，上级菜单不能选择自己");
        }

        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(reqSysMenu, sysMenu);

        return this.updateById(sysMenu);
    }

    @Override
    public Boolean remove(Integer id) {

        List<SysMenu> sysMenuList = this.lambdaQuery()
                .eq(SysMenu::getParentId, id)
                .eq(SysMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        if (CollUtil.isNotEmpty(sysMenuList)) {

            log.warn("菜单存在子菜单,不允许删除，id：{}", id);
            throw new LmyXlfException("存在子菜单,不允许删除");
        }

        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuService.lambdaQuery()
                .eq(SysRoleMenu::getMenuId, id)
                .eq(SysRoleMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .list();
        if (CollUtil.isNotEmpty(sysRoleMenuList)) {

            log.warn("菜单已分配,不允许删除，id：{}", id);
            throw new LmyXlfException("菜单已分配,不允许删除");
        }

        return this.lambdaUpdate()
                .set(SysMenu::getDeleteTime, LocalDateTime.now())
                .eq(SysMenu::getId, id)
                .eq(SysMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                .update();
    }

    @Override
    public List<RespSysMenu> selectMenuTreeByUserId(Integer userId) {

        List<SysMenu> sysMenuList;
        if (SysUserDTO.isAdmin(userId)) {

            sysMenuList = this.lambdaQuery()
                    .in(SysMenu::getMenuType, Arrays.asList(MenuTypeEnum.DIRECTORY.getValue(),
                            MenuTypeEnum.MENU.getValue()))
                    .eq(SysMenu::getStatus, UserConstant.NORMAL)
                    .eq(SysMenu::getDeleteTime, DBConstant.INITIAL_TIME)
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getOrderNum)
                    .list();
        } else {

            sysMenuList = sysMenuDao.selectMenuTreeByUserId(userId);
        }

        List<RespSysMenu> respSysMenuList = sysMenuList.stream().map(item -> {
            RespSysMenu respSysMenu = new RespSysMenu();
            BeanUtils.copyProperties(item, respSysMenu);
            return respSysMenu;
        }).toList();

        return getChildPerms(respSysMenuList, 0);
    }

    @Override
    public List<RespRouter> buildMenus(List<RespSysMenu> respSysMenuList) {

        List<RespRouter> routers = new LinkedList<>();
        for (RespSysMenu respSysMenu : respSysMenuList) {

            SysMenu sysMenu = new SysMenu();
            BeanUtils.copyProperties(respSysMenu, sysMenu);

            RespRouter router = new RespRouter();
            router.setHidden(Integer.valueOf(1).equals(respSysMenu.getVisible()));
            router.setName(getRouteName(sysMenu));
            router.setPath(getRouterPath(sysMenu));
            router.setComponent(getComponent(sysMenu));
            router.setQuery(respSysMenu.getQuery());
            router.setMeta(new MetaDTO(respSysMenu.getMenuName(),
                    respSysMenu.getIcon(),
                    Integer.valueOf(1).equals(respSysMenu.getIsCache()),
                    respSysMenu.getPath()));
            List<RespSysMenu> cMenus = respSysMenu.getChildren();
            if (CollUtil.isNotEmpty(cMenus) && MenuTypeEnum.DIRECTORY.getValue().equals(respSysMenu.getMenuType())) {

                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(sysMenu)) {

                router.setMeta(null);
                List<RespRouter> childrenList = new ArrayList<>();
                RespRouter children = new RespRouter();
                children.setPath(respSysMenu.getPath());
                children.setComponent(respSysMenu.getComponent());
                children.setName(getRouteName(respSysMenu.getRouteName(), respSysMenu.getPath()));
                children.setMeta(new MetaDTO(respSysMenu.getMenuName(),
                        respSysMenu.getIcon(),
                        Integer.valueOf(1).equals(respSysMenu.getIsCache()),
                        respSysMenu.getPath()));
                children.setQuery(respSysMenu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (respSysMenu.getParentId() == 0 && isInnerLink(sysMenu)) {

                router.setMeta(new MetaDTO(respSysMenu.getMenuName(), respSysMenu.getIcon()));
                router.setPath("/");
                List<RespRouter> childrenList = new ArrayList<>();
                RespRouter children = new RespRouter();
                String routerPath = innerLinkReplaceEach(respSysMenu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstant.INNER_LINK);
                children.setName(getRouteName(respSysMenu.getRouteName(), routerPath));
                children.setMeta(new MetaDTO(respSysMenu.getMenuName(), respSysMenu.getIcon(), respSysMenu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }

            routers.add(router);
        }

        return routers;
    }

    /**
     * 根据父节点的 id 获取所有子节点
     *
     * @param respSysMenuList 分类表
     * @param parentId        传入的父节点 id
     * @return String
     */
    public List<RespSysMenu> getChildPerms(List<RespSysMenu> respSysMenuList, int parentId) {

        List<RespSysMenu> returnList = new ArrayList<>();
        for (RespSysMenu respSysMenu : respSysMenuList) {

            // 一、根据传入的某个父节点 id，遍历该父节点的所有子节点
            if (respSysMenu.getParentId() == parentId) {

                recursionFn(respSysMenuList, respSysMenu);
                returnList.add(respSysMenu);
            }
        }

        return returnList;
    }

    /**
     * 递归列表
     *
     * @param respSysMenuList 分类表
     * @param respSysMenu     子节点
     */
    private void recursionFn(List<RespSysMenu> respSysMenuList, RespSysMenu respSysMenu) {

        // 得到子节点列表
        List<RespSysMenu> childList = getChildList(respSysMenuList, respSysMenu);
        respSysMenu.setChildren(childList);
        for (RespSysMenu tChild : childList) {

            if (hasChild(respSysMenuList, tChild)) {

                recursionFn(respSysMenuList, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<RespSysMenu> getChildList(List<RespSysMenu> respSysMenuList, RespSysMenu respSysMenu) {

        List<RespSysMenu> tlist = new ArrayList<>();
        for (RespSysMenu n : respSysMenuList) {

            if (n.getParentId().intValue() == respSysMenu.getId().intValue()) {

                tlist.add(n);
            }
        }

        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<RespSysMenu> respSysMenuList, RespSysMenu respSysMenu) {

        return !getChildList(respSysMenuList, respSysMenu).isEmpty();
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    private String getRouteName(SysMenu menu) {

        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {

            return StringUtils.EMPTY;
        }

        return getRouteName(menu.getRouteName(), menu.getPath());
    }

    /**
     * 获取路由名称，如没有配置路由名称则取路由地址
     *
     * @param name 路由名称
     * @param path 路由地址
     * @return 路由名称（驼峰格式）
     */
    public String getRouteName(String name, String path) {

        String routerName = StringUtils.isNotEmpty(name) ? name : path;
        return StringUtils.capitalize(routerName);
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isMenuFrame(SysMenu menu) {

        return menu.getParentId() == 0 && MenuTypeEnum.MENU.getValue().equals(menu.getMenuType())
                && UserConstant.NO_FRAME.equals(menu.getIsFrame());
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {

        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId() != 0 && isInnerLink(menu)) {

            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId() && MenuTypeEnum.DIRECTORY.getValue().equals(menu.getMenuType())
                && UserConstant.NO_FRAME.equals(menu.getIsFrame())) {

            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {

            routerPath = "/";
        }

        return routerPath;
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {

        return UserConstant.NO_FRAME.equals(menu.getIsFrame()) &&
                StringUtils.startsWithAny(menu.getPath(), AdminConstant.HTTP, AdminConstant.HTTPS);
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return 替换后的内链域名
     */
    public String innerLinkReplaceEach(String path) {

        return StringUtils.replaceEach(path, new String[]{AdminConstant.HTTP, AdminConstant.HTTPS, AdminConstant.WWW, ".", ":"},
                new String[]{"", "", "", "/", "/"});
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {

        String component = UserConstant.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {

            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId() != 0 && isInnerLink(menu)) {

            component = UserConstant.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {

            component = UserConstant.PARENT_VIEW;
        }

        return component;
    }

    /**
     * 是否为 parent_view 组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {

        return menu.getParentId() != 0 && MenuTypeEnum.DIRECTORY.getValue().equals(menu.getMenuType());
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param respSysMenuList 菜单列表
     * @return 下拉树结构列表
     */
    private List<RespTreeSelect> buildMenuTreeSelect(List<RespSysMenu> respSysMenuList) {

        List<RespSysMenu> menuTrees = buildMenuTree(respSysMenuList);
        return menuTrees.stream().map(RespTreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param respSysMenuList 菜单列表
     * @return 树结构列表
     */
    private List<RespSysMenu> buildMenuTree(List<RespSysMenu> respSysMenuList) {

        List<RespSysMenu> returnList = new ArrayList<>();
        List<Integer> tempList = respSysMenuList.stream().map(RespSysMenu::getId).toList();
        for (RespSysMenu respSysMenu : respSysMenuList) {

            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(respSysMenu.getParentId())) {

                recursionFn(respSysMenuList, respSysMenu);
                returnList.add(respSysMenu);
            }
        }
        if (returnList.isEmpty()) {

            returnList = respSysMenuList;
        }

        return returnList;
    }
}