package com.crayon.teacher.feign;

import com.crayon.common.core.util.R;
import com.crayon.teacher.entity.Teacher;
import com.crayon.teacher.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description
 * @Author Administrator
 * @Date 2024/4/16 13:55
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/teacher" + "/inner")
public class TeacherFeignClientController implements TeacherFeignClient {

    private final TeacherService teacherService;

    @Override
    @GetMapping("/detail")
    public R<Teacher> getInfo(@RequestParam(value = "id") Long id) {
        throw new RuntimeException("接口主动抛出异常，用于测试 fallback");
//        return R.ok(teacherService.getById(id));
    }

}
