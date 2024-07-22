package com.lmyxlf.jian_mu.global.session;

import com.lmyxlf.jian_mu.global.constant.LmyXlfReqParamConstant;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 请求会话的绑定信息
 * @since 17
 */
public class SessionBindInfo {

    private String clientIp;
    private String phone;

    /**
     * 从 HttpServletRequest 构造绑定信息并在 HttpServletRequest 记录下来
     *
     * @param httpRequest 本次 HttpServletRequest 请求对象
     */
    private SessionBindInfo(HttpServletRequest httpRequest) {
        clientIp = httpRequest.getHeader(LmyXlfReqParamConstant.KEY_REMOTE_ADDR);
        if (clientIp == null) {
            clientIp = httpRequest.getHeader(LmyXlfReqParamConstant.KEY_X_REAL_IP);
        }
        if (clientIp == null) {
            clientIp = httpRequest.getHeader(LmyXlfReqParamConstant.KEY_X_FORWARDED_FOR);
            if (clientIp != null) {
                clientIp = clientIp.split(",", 1)[0];
            }
        }
        String phone = httpRequest.getHeader(LmyXlfReqParamConstant.KEY_PHONE);
        if (phone == null) {
            phone = httpRequest.getParameter(LmyXlfReqParamConstant.KEY_PHONE);
        }
        this.phone = phone;
    }

    /**
     * @return 返回客户端IP
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @return 返回手机号码
     */
    public String getPhone() {
        return phone;
    }

    private String longToString(Long l) {
        if (l == null) {
            return "";
        }
        return l.toString();
    }

    @Override
    public String toString() {
        return String.format("[clientIp=%s", clientIp);
    }

    /**
     * 在请求的preHandler拦截器中初始化绑定信息
     */
    public static void initInfo() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        if (attributes != null) {
            HttpServletRequest httpRequest = attributes.getRequest();
            SessionBindInfo session = new SessionBindInfo(httpRequest);
            httpRequest.setAttribute(LmyXlfReqParamConstant.KEY_ATTR_BIND_INFO, session);
        }
    }

    /**
     * @return 返回当前请求会话的绑定信息
     */
    public static SessionBindInfo get() {
        SessionBindInfo sessionBindInfo = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        if (attributes != null) {
            HttpServletRequest httpRequest = attributes.getRequest();
            sessionBindInfo = (SessionBindInfo) httpRequest.getAttribute(LmyXlfReqParamConstant.KEY_ATTR_BIND_INFO);
        }
        return sessionBindInfo;
    }

    /**
     * 为fiegn的请求模板添加上下文绑定数据
     *
     * @param template fiegn请求拦截器模板
     */
    public static void fiegn(RequestTemplate template) {
        SessionBindInfo sessionBindInfo = get();
        if (sessionBindInfo.clientIp != null) {
            template.header(LmyXlfReqParamConstant.KEY_REMOTE_ADDR, sessionBindInfo.clientIp);
        }
    }
}