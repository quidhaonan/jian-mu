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
    public final static String KEY_ATTR_BIND_INFO = "lmyXlfBindInfo";

    /**
     * 客户端远程 IP 的 Http 头
     */
    public final static String KEY_REMOTE_ADDR = "REMOTE_ADDR";
    /**
     * 客户端真实 IP 的 Http 头
     */
    public final static String KEY_X_REAL_IP = "X-Real-IP";
    /**
     * 客户端路由 IP 序列的 Http 头
     */
    public final static String KEY_X_FORWARDED_FOR = "X-Forwarded-For";
    /**
     * 请求时间戳
     */
    public final static String KEY_TIMESTAMP = "_rt";

    /**
     * 电话号码，后台默认 +86
     */
    public final static String KEY_PHONE = "phone";

    /**
     * 客户端 ip
     */
    public final static String KEY_CLIENT_IP = "client_ip";

    /**
     * 随机字符串
     */
    public final static String KEY_NONCE = "nonce";

    /**
     * 签名
     */
    public final static String KEY_SIGN = "sign";
}