package com.crayon.teacher.service;

import com.crayon.base.test.yu.ed.service.AbstractBase;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/05/07
 */
@Component

public class TeacherStrategy extends AbstractBase<TeacherService> {

    private final TeacherService teacherService;

    public TeacherStrategy(TeacherService teacherService) {
        super(teacherService);
        this.teacherService = teacherService;
    }

    @Override
    public String test(Long id) {
        return teacherService.detail(id).toString();
    }

}
