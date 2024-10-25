package com.lmyxlf.jian_mu.admin.model.enums;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/8 17:51
 * @description 业务操作类型
 * @since 17
 */
public enum BusinessTypeEnum {

    /**
     * 其它
     */
    OTHER,

    /**
     * 新增
     */
    INSERT,

    /**
     * 修改
     */
    UPDATE,

    /**
     * 删除
     */
    DELETE,

    /**
     * 授权
     */
    GRANT,

    /**
     * 导出
     */
    EXPORT,

    /**
     * 导入
     */
    IMPORT,

    /**
     * 强退
     */
    FORCE,

    /**
     * 生成代码
     */
    GENCODE,

    /**
     * 清空数据
     */
    CLEAN,
}