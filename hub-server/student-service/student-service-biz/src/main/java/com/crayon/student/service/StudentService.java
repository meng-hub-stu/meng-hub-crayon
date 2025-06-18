package com.crayon.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crayon.student.entity.model.Student;
import com.crayon.student.entity.resp.StudentResp;

/**
 * @author Mengdl
 * @date 2024/11/28
 */
public interface StudentService extends IService<Student> {

    /**
     * 查询数据
     * @param id id
     * @return StudentResp
     */
    StudentResp detail(Long id);

    /**
     * 查询数据
     * @param id id
     * @return StudentResp
     */
    StudentResp detail1(Long id);

}
