package com.lmyxlf.jian_mu.business.batch_invitation.model.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2025/6/23 0:42
 * @description 星流 AI 返回结果
 * @since 17
 */
@Data
@Accessors(chain = true)
public class RespXingLiu {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private Data data;

    @lombok.Data
    @Accessors(chain = true)
    public static class Data {

        private String id;
        private String uuid;
        private String mobile;
        private String email;
        private String nickname;
        private String avatar;
        private Integer status;
        private Integer createBy;
        private LocalDateTime createTime;
        private String deleteFlag;
        private Integer updateBy;
        private LocalDateTime updateTime;
        private List<Object> homepages;
        private List<Object> permissions;
        private PersonalSpaceInformation personalSpaceInformation;
        private String enterpriseId;
        private String authorDetail;
        private String activeAccounts;
        private String aboutMe;
        private String token;
        private String source;
        private String newQQUser;
        private String newWeChatUser;
        private String hasBindPhone;
        private String forbiddenType;
        private String ocpcSourceInfo;
    }

    @lombok.Data
    @Accessors(chain = true)
    static class PersonalSpaceInformation {

        private String briefIntroduction;
        private String coverImage;
        private String xiaohongshuLink;
        private String xiaohongshuIcon;
        private String tiktokLink;
        private String tiktokIcon;
        private String bliLink;
        private String bliIcon;
        private String webLink;
        private String webIcon;
        private String huggingFaceLink;
        private String huggingFaceIcon;
        private String twitterLink;
        private String twitterIcon;
        private String facebookLink;
        private String facebookIcon;
        private String instagramLink;
        private String instagramIcon;
        private String discordLink;
        private String discordIcon;
        private String otherLink;
        private String otherIcon;
        private String openBusiness;
        private String openLike;
        private String openRecommend;
    }
}