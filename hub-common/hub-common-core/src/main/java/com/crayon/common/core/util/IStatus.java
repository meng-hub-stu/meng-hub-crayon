package com.crayon.common.core.util;

import java.util.Arrays;
import java.util.Optional;

/**
 * 枚举基类
 * @author Mengdl
 * @date 2025/03/11
 */
public interface IStatus {

    /**
     * 获取值
     *
     * @return 值
     */
    Integer getValue();

    /**
     * 获取描述
     *
     * @return 描述
     */
    String getDesc();

    /**
     * 状态判断
     *
     * @param value 状态值
     * @return 返回结果
     */
    default boolean equals(Integer value) {
        return getValue().equals(value);
    }

    /**
     * 查询枚举
     *
     * @param value     值
     * @param enumClass 类型
     * @param <E>       枚举类实例
     * @return 返回结果
     */
    static <E extends Enum<E> & IStatus> Optional<E> find(Integer value, final Class<E> enumClass) {
        if (value != null) {
            return Arrays.stream(enumClass.getEnumConstants()).filter(v -> v.getValue().equals(value)).findFirst();
        }
        return Optional.empty();
    }

    /**
     * 查询枚举
     *
     * @param desc      描述
     * @param enumClass 类型
     * @param <E>       枚举类实例
     * @return 返回结果
     */
    static <E extends Enum<E> & IStatus> Optional<E> of(String desc, final Class<E> enumClass) {
        if (desc != null) {
            return Arrays.stream(enumClass.getEnumConstants()).filter(v -> v.getDesc().equals(desc)).findFirst();
        }
        return Optional.empty();
    }

}
