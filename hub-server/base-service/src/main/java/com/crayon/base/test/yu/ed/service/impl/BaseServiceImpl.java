package com.crayon.base.test.yu.ed.service.impl;

import com.crayon.base.test.yu.ed.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2025/04/28
 */
@Component
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {

    @Override
    public String selectStudent(Long id) {
        return "你是最棒的学生";
    }

}
