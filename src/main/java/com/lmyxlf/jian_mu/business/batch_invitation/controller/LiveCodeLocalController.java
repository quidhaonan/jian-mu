package com.lmyxlf.jian_mu.business.batch_invitation.controller;

import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeLocal;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeLocal;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeLocalService;
import com.lmyxlf.jian_mu.global.annotation.IgnoreAuthUrlAnnotation;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import com.lmyxlf.jian_mu.global.model.PageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/10 1:53
 * @description
 * @since 17
 */
@RestController
@Api(tags = "本地 活码")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.batch_invitation.urlPrefix}/liveCode/local")
public class LiveCodeLocalController {

    private final LiveCodeLocalService liveCodeLocalService;

    @PostMapping("/list")
    @ApiOperation(value = "分页获得活码")
    public LmyXlfResult<PageData<RespLiveCodeLocal>> list(@RequestBody ReqLiveCodeLocal reqLiveCodeLocal) {

        return LmyXlfResult.ok(new PageData<>(liveCodeLocalService.list(reqLiveCodeLocal)));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "获得单个活码")
    public LmyXlfResult<RespLiveCodeLocal> getInfo(@PathVariable("id") Integer id) {

        return LmyXlfResult.ok(liveCodeLocalService.getInfo(id));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获得单个活码")
    public void getOneById(@PathVariable("id") Integer id, HttpServletResponse response) {

        liveCodeLocalService.getOneById(id, response);
    }

    @IgnoreAuthUrlAnnotation
    @GetMapping("/pub/{randomStr}")
    @ApiOperation(value = "获得单个活码")
    public void getOneByRandomStr(@PathVariable("randomStr") String randomStr, HttpServletResponse response) {

        liveCodeLocalService.getOneByRandomStr(randomStr, response);
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加活码")
    public LmyXlfResult<Boolean> add(ReqLiveCodeLocal reqLiveCodeLocal, @RequestParam("file") MultipartFile file) {

        return LmyXlfResult.ok(liveCodeLocalService.add(reqLiveCodeLocal, file));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改活码")
    public LmyXlfResult<Boolean> update(ReqLiveCodeLocal reqLiveCodeLocal, @RequestParam("file") MultipartFile file) {

        return LmyXlfResult.ok(liveCodeLocalService.update(reqLiveCodeLocal, file));
    }

    @PostMapping("/change/status")
    @ApiOperation(value = "修改活码状态")
    public LmyXlfResult<Boolean> changeStatus(@RequestBody ReqLiveCodeLocal reqLiveCodeLocal) {

        return LmyXlfResult.ok(liveCodeLocalService.changeStatus(reqLiveCodeLocal));
    }

    @PostMapping("/remove")
    @ApiOperation(value = "删除活码")
    public LmyXlfResult<Boolean> remove(@RequestBody ReqLiveCodeLocal reqLiveCodeLocal) {

        return LmyXlfResult.ok(liveCodeLocalService.remove(reqLiveCodeLocal));
    }
}