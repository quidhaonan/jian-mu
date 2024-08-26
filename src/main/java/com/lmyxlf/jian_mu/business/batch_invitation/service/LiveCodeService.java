package com.lmyxlf.jian_mu.business.batch_invitation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCode;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeDelete;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeList;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeList;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/8/24 1:05
 * @description
 * @since 17
 */
public interface LiveCodeService extends IService<LiveCode> {

    /**
     * 获得多个活码
     *
     * @param reqLiveCodeList
     * @return
     */
    List<RespLiveCodeList> list(ReqLiveCodeList reqLiveCodeList);

    /**
     * 添加活码
     *
     * @param file
     * @param remark
     * @return
     */
    Boolean add(MultipartFile file, String remark);

    /**
     * 获得单个活码
     *
     * @param id
     * @param response
     */
    void get(Integer id, HttpServletResponse response);

    /**
     * 修改活码
     *
     * @param file
     * @param id
     * @param remark
     * @return
     */
    Boolean update(MultipartFile file, Integer id, String remark);

    /**
     * 删除活码
     *
     * @param reqLiveCodeDelete
     * @return
     */
    Boolean delete(ReqLiveCodeDelete reqLiveCodeDelete);
}