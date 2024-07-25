package com.lmyxlf.jian_mu.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.lmyxlf.jian_mu.global.constant.TraceConstant;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 15:27
 * @description
 * @since 17
 */
public class JsonLogConverter extends ClassicConverter {


    @Override
    public String convert(ILoggingEvent event) {
        // 解析podname
        String hostName = "";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var3) {
            hostName = "not_find";
        }
        // 解析appName
        Map<String, String> propertyMap = event.getLoggerContextVO().getPropertyMap();
        String appName = propertyMap.get("APP_NAME");
        // 构造日志对象
        HashMap<Object, Object> map = new HashMap<>();
        map.put("level", event.getLevel().levelStr);
        map.put("msg", event.getFormattedMessage());
        Date date = new Date(event.getTimeStamp()); // 将时间戳转换为Date对象
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // 指定日期格式
        map.put("time", sdf.format(date));
        map.put("srv", appName);
        map.put("pod", hostName);
        map.put("trace_id", MDC.get(TraceConstant.TRACE_ID));
        map.put("uid", MDC.get("uid"));
        map.put("session_id", MDC.get("sessionId"));
        map.put("socket_id", MDC.get("socketId"));
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(sra)) {
           HttpServletRequest request = sra.getRequest();
            map.put("req_id", request.getHeader("X-Request-ID"));
        }
        // 截取前后符号
        String jsonString = LogJacksonUtils.toJsonString(map);
        return jsonString.substring(1, jsonString.length() - 1);
    }
}