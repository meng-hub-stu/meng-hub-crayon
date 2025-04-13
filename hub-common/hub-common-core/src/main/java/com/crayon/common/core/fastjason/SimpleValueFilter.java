package com.crayon.common.core.fastjason;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.crayon.common.core.util.WebUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/4/13 9:57
 **/
@RequiredArgsConstructor
public class SimpleValueFilter implements ValueFilter {

    private final CommonCoreProperties commonCoreProperties;

    @Override
    public Object apply(Object object, String name, Object value) {
        if (value instanceof LocalDateTime) {
            if (commonCoreProperties.isEnable()) {
                return localDateTimeToString(value);
            }
        } else if (value instanceof LocalDate) {
            if (commonCoreProperties.isEnable()) {
                return localDatToString(value);
            }
        } else if (value instanceof LocalTime) {
            return ((LocalTime) value).format(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
        }
        return value;
    }

    /**
     * DEFAULT_ZONE = "UTC";
     * 时区转换, 系统时区->用户所在时区
     */
    private LocalDateTime localDateTimeToString(Object value) {
        String headerZone = WebUtils.getRequest().getHeader(commonCoreProperties.getZone());
        try {
            if (ObjUtil.isNotEmpty(value)) {
                LocalDateTime date = (LocalDateTime) value;
                String zone = Optional.ofNullable(headerZone).orElse(commonCoreProperties.getDefaultZone());
                return date.atZone(ZoneId.of(commonCoreProperties.getDefaultZone())).withZoneSameInstant(ZoneId.of(zone)).toLocalDateTime();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("[localDateTimeToString] [valueToString] Unknown headerZone" + headerZone);
        }
    }

    private LocalDate localDatToString(Object value) {
        String headerZone = WebUtils.getRequest().getHeader(commonCoreProperties.getZone());
        try {
            if (ObjUtil.isNotEmpty(value)) {
                // 原始时区
                ZoneId originalZone = ZoneId.of(commonCoreProperties.getDefaultZone());
                // 目标时区
                ZoneId targetZone = ZoneId.of(Optional.ofNullable(headerZone).orElse(commonCoreProperties.getDefaultZone()));
                LocalDate date = (LocalDate) value;
                return date.atStartOfDay(originalZone)
                        .withZoneSameInstant(targetZone)
                        .toLocalDate();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("[localDatToString] [valueToString] Unknown headerZone" + headerZone);
        }
    }

}
