package com.crayon.teacher.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crayon.teacher.entity.Teacher;
import com.crayon.teacher.mapper.TeacherMapper;
import com.crayon.teacher.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mengdl
 * @date 2024/11/28
 */
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public List<Teacher> scroll(Integer offset, Integer limit) {
        return baseMapper.selectPage(new Page<>(offset / limit + 1, limit), null).getRecords();
    }

}
