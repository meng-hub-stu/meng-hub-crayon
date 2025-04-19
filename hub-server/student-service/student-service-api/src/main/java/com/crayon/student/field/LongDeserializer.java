package com.crayon.student.field;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.reflect.Type;

/**
 * @author Mengdl
 * @date 2025/04/11
 */
@Schema(description = "Long类型反序列化器")
public class LongDeserializer implements ObjectReader<Long> {

    @Override
    public Long readObject(JSONReader jsonReader, Type type, Object fieldName, long l) throws JSONException {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        return Long.parseLong(jsonReader.readString());
    }

}
