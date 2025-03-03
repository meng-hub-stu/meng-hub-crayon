package com.crayon.common.core.constant;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author robin
 * @date 2024/5/21
 */
public interface BaseEnum {
    /**
     * 获取值
     *
     * @return 值
     */
    String getValue();

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
    default boolean equals(String value) {
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
    static <E extends Enum<E> & BaseEnum> Optional<E> find(String value, final Class<E> enumClass) {
        if (value != null) {
            return Arrays.stream(enumClass.getEnumConstants())
                    .filter(v -> v.getValue().equals(value))
                    .findFirst();
        }
        return Optional.empty();
    }

}
