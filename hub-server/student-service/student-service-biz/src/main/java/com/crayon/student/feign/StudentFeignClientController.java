package com.crayon.student.feign;

//import com.crayon.common.core.util.R;
import com.crayon.common.core.util.R;
import com.crayon.student.entity.model.Student;
import com.crayon.student.service.StudentService;
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
@RequestMapping("/student" + "/inner")
public class StudentFeignClientController implements StudentFeignClient {

    private final StudentService studentService;

    @Override
    @GetMapping("/detail")
    public R<Student> getInfo(@RequestParam(value = "id") Long id) {
        return R.ok(studentService.getById(id));
    }

}
