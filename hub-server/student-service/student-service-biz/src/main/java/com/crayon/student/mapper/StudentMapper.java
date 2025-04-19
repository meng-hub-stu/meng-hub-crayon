package com.crayon.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crayon.student.entity.model.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Mengdl
 * @date 2024/11/29
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
