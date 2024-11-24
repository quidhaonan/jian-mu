package com.lmyxlf.jian_mu.business.batch_invitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lmyxlf.jian_mu.business.batch_invitation.model.entity.LiveCodeLocal;
import com.lmyxlf.jian_mu.business.batch_invitation.model.req.ReqLiveCodeLocal;
import com.lmyxlf.jian_mu.business.batch_invitation.model.resp.RespLiveCodeLocal;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/10 1:51
 * @description
 * @since 17
 */
public interface LiveCodeLocalService extends IService<LiveCodeLocal> {

    IPage<RespLiveCodeLocal> list(ReqLiveCodeLocal reqLiveCodeLocal);

    RespLiveCodeLocal getInfo(Integer id);

    /**
     * 获得单个活码
     *
     * @param id
     * @param response
     */
    void getOneById(Integer id, HttpServletResponse response);

    /**
     * 获得单个活码
     *
     * @param randomStr
     * @param response
     */
    void getOneByRandomStr(String randomStr, HttpServletResponse response);

    Boolean add(ReqLiveCodeLocal reqLiveCodeLocal, MultipartFile file);

    Boolean update(ReqLiveCodeLocal reqLiveCodeLocal, MultipartFile file);

    Boolean changeStatus(ReqLiveCodeLocal reqLiveCodeLocal);

    Boolean remove(ReqLiveCodeLocal reqLiveCodeLocal);
}