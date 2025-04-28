package com.crayon.base.test.yu.ed.controller;

import com.crayon.base.test.yu.ed.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mengdl
 * @date 2025/04/28
 */
public abstract class AbstractTeacherController {

    @Autowired
    private BaseService baseService;

    /**
     * 测试方法
     * @param id id
     * @return String
     */
    public String test(Long id) {
        return baseService.selectStudent(id);
    }

}
