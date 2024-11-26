package com.lmyxlf.jian_mu.global.constant;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 请求参数常量
 * @since 17
 */
public class LmyXlfReqParamConstant {
    /**
     * 存储在 HttpServletRequest 的属性 Key
     */
    public static final String KEY_ATTR_BIND_INFO = "lmyXlfBindInfo";

    /**
     * 客户端远程 IP 的 Http 头
     */
    public static final String KEY_REMOTE_ADDR = "REMOTE_ADDR";

    /**
     * 客户端真实 ip 地址
     */
    public static final String REMOTE_IP = "remoteIp";

    /**
     * 客户端真实 IP 的 Http 头，nginx 代理
     */
    public static final String KEY_X_REAL_IP = "X-Real-IP";

    /**
     * 客户端路由 IP 序列的 Http 头
     */
    public static final String KEY_X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * apache 服务代理
     */
    public static final String KEY_PROXY_CLIENT_IP = "Proxy-Client-IP";

    /**
     * weblogic 服务代理
     */
    public static final String KEY_WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

    /**
     * 其他服务代理
     */
    public static final String KEY_HTTP_CLIENT_IP = "HTTP_CLIENT_IP";

    /**
     * 请求时间戳
     */
    public static final String KEY_TIMESTAMP = "_rt";

    /**
     * 电话号码，后台默认 +86
     */
    public static final String KEY_PHONE = "phone";

    /**
     * 客户端 ip
     */
    public static final String KEY_CLIENT_IP = "client_ip";

    /**
     * 随机字符串
     */
    public static final String KEY_NONCE = "nonce";

    /**
     * 签名
     */
    public static final String KEY_SIGN = "sign";

    public static final String AUTHORIZATION = "Authorization";

    /**
     * token
     */
    public static final String TOKEN = "token";

    /**
     * 来源
     */
    public static final String USER_AGENT = "User-Agent";
}