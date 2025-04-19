package com.crayon.student.field;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.reflect.Type;

/**
 * @author Mengdl
 * @date 2025/04/11
 */
@Schema(description = "Long类型序列化")
public class LongSerializer implements ObjectWriter<String> {
    @Override
    public void write(JSONWriter jsonWriter, Object o, Object o1, Type type, long l) {
        jsonWriter.writeString(o.toString());
    }

}
