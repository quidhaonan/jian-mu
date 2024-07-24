package com.lmyxlf.jian_mu.business.raising_numbers.model.req;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/24 12:45
 * @description 丽宝乐园请求体_签到
 * @since 17
 */
@Data
@Accessors(chain = true)
public class ReqLibaoLand {

    private String MallID = "11192";

    private Header Header = new Header();

    public ReqLibaoLand(String token) {
        this.Header.Token = token;
    }

    @Data
    @Accessors(chain = true)
    public static class Header {
        private String Token;

        private SystemInfo systemInfo = new SystemInfo();
    }

    @Data
    @Accessors(chain = true)
    public static class SystemInfo {
        private String model = "microsoft";

        private String SDKVersion = "3.9.0";

        private String system = "Windows 10 x64";

        private String version = "3.7.1";

        private String miniVersion = "DZ.6.7.6.byn.1";
    }
}