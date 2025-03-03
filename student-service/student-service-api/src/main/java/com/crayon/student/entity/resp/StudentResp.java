package com.crayon.student.entity.resp;

import com.crayon.student.entity.model.Student;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author Mengdl
 * @date 2025/2/28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class StudentResp extends Student {

    @Schema(description = "老师信息")
    private TeacherResp teacherResp;

}
