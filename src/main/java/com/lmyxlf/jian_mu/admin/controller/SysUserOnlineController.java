package com.lmyxlf.jian_mu.admin.controller;

import com.lmyxlf.jian_mu.admin.annotation.Log;
import com.lmyxlf.jian_mu.admin.model.dto.SysUserOnlineDTO;
import com.lmyxlf.jian_mu.admin.model.enums.BusinessTypeEnum;
import com.lmyxlf.jian_mu.admin.service.SysUserOnlineService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.model.PageData;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/14 2:08
 * @description
 * @since 17
 */
@RestController
@Api(tags = "在线用户监控")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.admin.urlPrefix}/monitor/online")
public class SysUserOnlineController {

    private SysUserOnlineService sysUserOnlineService;

    @PreAuthorize("@permissionService.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public LmyXlfResult<PageData<SysUserOnlineDTO>> list(String ipaddr, String userName) {

        List<SysUserOnlineDTO> sysUserOnlineDTOList = sysUserOnlineService.list(ipaddr, userName);
        return LmyXlfResult.ok(new PageData<SysUserOnlineDTO>()
                .setRecords(sysUserOnlineDTOList)
                .setTotal(sysUserOnlineDTOList.size()));
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@permissionService.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessTypeEnum.FORCE)
    @DeleteMapping("/{tokenId}")
    public LmyXlfResult<Boolean> forceLogout(@PathVariable String tokenId) {

        return LmyXlfResult.ok(sysUserOnlineService.forceLogout(tokenId));
    }
}