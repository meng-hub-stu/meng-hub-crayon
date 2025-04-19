package com.crayon.teacher.feign;

import com.crayon.common.core.util.R;
import com.crayon.teacher.entity.Teacher;
import org.springframework.stereotype.Component;
/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2024/4/16 14:07
 **/
@Component
public class TeacherFeignClientFallBack implements TeacherFeignClient {

    /**
     * 返回失败
     * @param id 老师id
     * @return
     */
    @Override
    public R<Teacher> getInfo(Long id) {
        return R.failed();
    }

}
