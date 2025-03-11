package com.crayon.student.entity.enums;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.crayon.common.core.util.IStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author Mengdl
 * @date 2025/03/11
 */
@AllArgsConstructor
@Getter
public enum GradeType implements IStatus {

    /**
     * 年级类型
     */
    ONE(1, "一年级"),
    TWO(2, "二年级"),
    THREE(3, "三年级"),
    FOUR(4, "四年级"),
    FIVE(5, "五年级"),
    SIX(6, "六年级"),
    SEVEN(7, "七年级"),
    EIGHT(8, "八年级"),
    NINE(9, "九年级"),
    TEN(10, "十年级"),
    ELEVEN(11, "十一年级"),
    TWELVE(12, "十二年级");


    @EnumValue
    @JSONField(value = true)
    private final Integer value;
    private final String desc;

    public static GradeType valueOf(Integer value) {
//        return Arrays.stream(values()).filter(v -> v.getValue().equals(value)).findFirst().orElse(null);
        return IStatus.find(value, GradeType.class).orElse(null);
    }

    public static GradeType of(String desc) {
        return Arrays.stream(values()).filter(v -> v.getDesc().equals(desc)).findFirst().orElse(null);
//        return IStatus.of(desc, GradeType.class).orElse(null);
    }

}
