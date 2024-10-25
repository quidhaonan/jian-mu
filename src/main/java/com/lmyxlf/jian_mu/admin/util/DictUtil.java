package com.lmyxlf.jian_mu.admin.util;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.lmyxlf.jian_mu.admin.constant.CacheConstant;
import com.lmyxlf.jian_mu.admin.model.entity.SysDictData;
import com.lmyxlf.jian_mu.global.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/9/12 21:48
 * @description 字典工具类
 * @since 17
 */
public class DictUtil {

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 设置字典缓存
     *
     * @param key       参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas) {

        RedisUtil.set(getCacheKey(key), dictDatas);
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<SysDictData> getDictCache(String key) {

        JSONArray arrayCache = RedisUtil.get(getCacheKey(key));
        if (ObjUtil.isNotNull(arrayCache)) {

            return arrayCache.toList(SysDictData.class);
        }

        return null;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue) {

        if (StrUtil.isEmpty(dictValue)) {

            return StrUtil.EMPTY;
        }

        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel) {

        if (StrUtil.isEmpty(dictLabel)) {

            return StrUtil.EMPTY;
        }

        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator) {

        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (ObjUtil.isNull(datas)) {

            return StrUtil.EMPTY;
        }

        if (StrUtil.containsAny(separator, dictValue)) {

            for (SysDictData dict : datas) {

                for (String value : dictValue.split(separator)) {

                    if (value.equals(dict.getDictValue())) {

                        propertyString.append(dict.getDictLabel()).append(separator);
                        break;
                    }
                }
            }
        } else {

            for (SysDictData dict : datas) {

                if (dictValue.equals(dict.getDictValue())) {

                    return dict.getDictLabel();
                }
            }
        }

        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator) {

        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (ObjUtil.isNull(datas)) {

            return StrUtil.EMPTY;
        }

        if (StringUtils.containsAny(separator, dictLabel)) {

            for (SysDictData dict : datas) {

                for (String label : dictLabel.split(separator)) {

                    if (label.equals(dict.getDictLabel())) {

                        propertyString.append(dict.getDictValue()).append(separator);
                        break;
                    }
                }
            }
        } else {

            for (SysDictData dict : datas) {

                if (dictLabel.equals(dict.getDictLabel())) {

                    return dict.getDictValue();
                }
            }
        }

        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型获取字典所有值
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    public static String getDictValues(String dictType) {

        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (ObjUtil.isNull(datas)) {

            return StrUtil.EMPTY;
        }

        for (SysDictData dict : datas) {

            propertyString.append(dict.getDictValue()).append(SEPARATOR);
        }

        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 根据字典类型获取字典所有标签
     *
     * @param dictType 字典类型
     * @return 字典值
     */
    public static String getDictLabels(String dictType) {

        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);
        if (ObjUtil.isNull(datas)) {

            return StrUtil.EMPTY;
        }

        for (SysDictData dict : datas) {

            propertyString.append(dict.getDictLabel()).append(SEPARATOR);
        }

        return StringUtils.stripEnd(propertyString.toString(), SEPARATOR);
    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key) {

        RedisUtil.delete(getCacheKey(key));
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {

        Collection<String> keys = RedisUtil.keys(CacheConstant.SYS_DICT_KEY + "*");
        RedisUtil.delete(keys);
    }

    /**
     * 设置 cache key
     *
     * @param configKey 参数键
     * @return 缓存键 key
     */
    public static String getCacheKey(String configKey) {

        return CacheConstant.SYS_DICT_KEY + configKey;
    }
}