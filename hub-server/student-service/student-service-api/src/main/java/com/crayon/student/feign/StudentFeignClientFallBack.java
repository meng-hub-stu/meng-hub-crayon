package com.crayon.student.feign;

import com.crayon.common.core.util.R;
import com.crayon.student.entity.model.Student;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2024/4/16 14:07
 **/
@Component
public class StudentFeignClientFallBack implements StudentFeignClient {

    /**
     * 返回失败
     * @param id 老师id
     * @return
     */
    @Override
    public R<Student> getInfo(Long id) {
        return R.failed();
    }

}
