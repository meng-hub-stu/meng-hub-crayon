package com.crayon.common.data.fastjason;
import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/4/12 21:08
 **/
@Configuration
public class Fastjson2Configuration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建FastJson消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        // 创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 自定义过滤器,数据脱敏过滤器
//        fastJsonConfig.setWriterFilters(simpleValueFilter, sensitiveSerialize);
        fastJsonConfig.setWriterFeatures(
                // 格式化输出
                JSONWriter.Feature.PrettyFormat,
                // 输出值为null的字段
                JSONWriter.Feature.WriteNulls,
//                JSONWriter.Feature.FieldBased,
                // 超过javaScript支持的整数，输出为字符串
                JSONWriter.Feature.BrowserCompatible,
                // BigDecimal过大丢失精度
                JSONWriter.Feature.WriteBigDecimalAsPlain,
                // List字段如果为null，输出为[]，而不是null
                JSONWriter.Feature.WriteNullListAsEmpty,
                // 枚举用toString方法
                JSONWriter.Feature.WriteEnumUsingToString,
                // 保留Map空的字段
                JSONWriter.Feature.WriteMapNullValue
        );
        fastJsonConfig.setReaderFeatures(JSONReader.Feature.SupportSmartMatch, JSONReader.Feature.FieldBased);
        // 设置字符集
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        // fastJsonConfig.setJSONB(true);
        // 设置FastJson配置
        converter.setFastJsonConfig(fastJsonConfig);
        converters.add(converter);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
        registrar.registerFormatters(registry);
    }

}
