package com.lmyxlf.jian_mu.business.batch_invitation.controller;

import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeDelete;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeList;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeList;
import com.lmyxlf.jian_mu.business.batch_invitation.service.LiveCodeService;
import com.lmyxlf.jian_mu.global.model.LmyXlfResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 0:58
 * @description 活码
 * @since 17
 */
@RestController
@Api(tags = "活码")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("${jian_mu.batch_invitation.urlPrefix}/liveCode")
public class LiveCodeController {

    private final LiveCodeService liveCodeService;

    @PostMapping("/list")
    @ApiOperation(value = "获得多个活码")
    public LmyXlfResult<List<RespLiveCodeList>> list(@RequestBody ReqLiveCodeList reqLiveCodeList) {

        return LmyXlfResult.ok(liveCodeService.list(reqLiveCodeList));
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加活码")
    public LmyXlfResult<Boolean> add(@RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "remark", required = false) String remark) {

        return LmyXlfResult.ok(liveCodeService.add(file, remark));
    }

    @GetMapping("/get/{id}")
    @ApiOperation(value = "获得单个活码")
    public void get(@PathVariable Integer id, HttpServletResponse response) {

        liveCodeService.get(id, response);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改活码")
    public LmyXlfResult<Boolean> update(@RequestParam("file") MultipartFile file,
                                        @RequestParam("id") Integer id,
                                        @RequestParam(value = "remark", required = false) String remark) {

        return LmyXlfResult.ok(liveCodeService.update(file, id, remark));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除活码")
    public LmyXlfResult<Boolean> delete(@RequestBody @Validated ReqLiveCodeDelete reqLiveCodeDelete) {

        return LmyXlfResult.ok(liveCodeService.delete(reqLiveCodeDelete));
    }
}