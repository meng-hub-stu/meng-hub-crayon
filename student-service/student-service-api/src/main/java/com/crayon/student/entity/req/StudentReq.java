package com.crayon.student.entity.req;

import com.alibaba.fastjson2.annotation.JSONField;
import com.crayon.student.field.LongDeserializer;
import com.crayon.student.field.LongSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author Mengdl
 * @date 2025/04/11
 */
@Data
public class StudentReq {

    @Schema(description = "id")
    @JSONField(deserializeUsing = LongDeserializer.class, serializeUsing = LongSerializer.class)
    private Long id;

    @Schema(description = "name")
    private String name;

    @Schema(description = "sex")
    private String sex;

}
