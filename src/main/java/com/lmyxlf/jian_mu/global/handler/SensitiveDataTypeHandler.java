package com.lmyxlf.jian_mu.global.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description
 * @since 17
 */
@Slf4j
public class SensitiveDataTypeHandler extends BaseTypeHandler<String> {
    /**
     * 128 位 key
     */
    private static final String SM4_KEY = "WCrrJ#rIY)CMVAdp";

    /**
     * 非空字段加密 - 入库
     *
     * @param preparedStatement
     * @param i
     * @param parameter
     * @param jdbcType
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String parameter, JdbcType jdbcType) {
        // 不处理空字符串
        if (StrUtil.isBlank(parameter)) {
            return;
        }
        try {
            SM4 sm4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));
            String encrypt = sm4.encryptHex(parameter, StandardCharsets.UTF_8);
            log.debug("数据：{},加密{}", parameter, encrypt);
            preparedStatement.setString(i, encrypt);
        } catch (Exception e) {
            log.error("typeHandler 加密异常：" + e);
        }
    }

    /**
     * 可空字段解密 - 出库
     *
     * @param resultSet
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return decryptStr(resultSet.getString(columnName));
    }

    /**
     * 可空字段解密
     *
     * @param resultSet
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return decryptStr(resultSet.getString(columnIndex));
    }

    /**
     * 可空字段解密
     *
     * @param callableStatement
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public String getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return decryptStr(callableStatement.getString(columnIndex));
    }


    private String decryptStr(String col) {
        // 不处理空字符串
        if (StrUtil.isBlank(col)) {
            return col;
        }
        try {
            SM4 sm4 = SmUtil.sm4(SM4_KEY.getBytes(StandardCharsets.UTF_8));
            String plain = sm4.decryptStr(col, StandardCharsets.UTF_8);
            log.debug("数据：{},解密{}", col, plain);
            return plain;
        } catch (Exception e) {
            log.debug("数据非 sm4 加密");
        }
        return col;
    }
}