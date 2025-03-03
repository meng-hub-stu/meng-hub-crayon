package com.crayon.student.feign;

import com.crayon.common.core.util.R;
import com.crayon.student.entity.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2024/4/16 13:59
 **/

@FeignClient(
        value = "student-service-biz",
        contextId = "studentFeignClient",
        fallback = StudentFeignClientFallBack.class
)
public interface StudentFeignClient {

    String PREFIX = "/student" + "/inner";

    /**
     * 获取学生信息
     *
     * @param id 学生id
     * @return
     */
    @GetMapping(value = PREFIX + "/detail")
    R<Student> getInfo(@RequestParam(value = "id") Long id);

}
