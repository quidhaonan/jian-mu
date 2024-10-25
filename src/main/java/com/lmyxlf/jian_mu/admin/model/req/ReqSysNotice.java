package com.lmyxlf.jian_mu.admin.model.req;

import com.lmyxlf.jian_mu.admin.annotation.Xss;
import com.lmyxlf.jian_mu.global.model.PageEntity;
import com.lmyxlf.jian_mu.global.validation.group.Delete;
import com.lmyxlf.jian_mu.global.validation.group.Insert;
import com.lmyxlf.jian_mu.global.validation.group.Update;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/30 12:32
 * @description
 * @since 17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ReqSysNotice extends PageEntity {

    /**
     * 主键 id
     */
    @Null(message = "主键 id 应为空", groups = {Insert.class})
    @NotNull(message = "主键 id 不能为空", groups = {Update.class})
    private Integer id;

    /**
     * 主键 id 集合，配置 id
     */
    @NotEmpty(message = "主键 id 集合不能为空", groups = {Delete.class})
    private List<Integer> ids;

    /**
     * 公告标题
     */
    @Xss(message = "公告标题不能包含脚本字符", groups = {Insert.class, Update.class})
    @NotBlank(message = "公告标题不能为空", groups = {Insert.class, Update.class})
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符", groups = {Insert.class, Update.class})
    private String noticeTitle;

    /**
     * 公告类型，0：通知，1：公告
     */
    private Integer noticeType;

    /**
     * 创建者
     */
    @Null(message = "创建者不能指定", groups = {Insert.class, Update.class})
    private String createUser;
}