package com.crayon.teacher.feign;

import com.crayon.common.core.util.R;
import com.crayon.teacher.entity.Teacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Mengdl
 * @date 2024/11/28
 */
@FeignClient(
        value = "teacher-service-biz",
        contextId = "teacherFeignClient",
        fallback = TeacherFeignClientFallBack.class
)
public interface TeacherFeignClient {

    String PREFIX = "/teacher" + "/inner";

    /**
     * 获取老师信息
     *
     * @param id 老师id
     * @return
     */
    @GetMapping(value = PREFIX + "/detail")
    R<Teacher> getInfo(@RequestParam(value = "id") Long id);

}
