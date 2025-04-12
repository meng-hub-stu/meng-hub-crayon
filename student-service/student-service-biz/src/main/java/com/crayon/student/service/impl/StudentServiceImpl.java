package com.crayon.student.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crayon.common.core.util.IStatus;
import com.crayon.common.core.util.R;
import com.crayon.common.data.transaction.NoticeTransaction;
import com.crayon.student.entity.enums.GradeType;
import com.crayon.student.entity.model.Student;
import com.crayon.student.entity.resp.StudentResp;
import com.crayon.student.entity.resp.TeacherResp;
import com.crayon.student.mapper.StudentMapper;
import com.crayon.student.service.StudentService;
import com.crayon.teacher.entity.Teacher;
import com.crayon.teacher.feign.TeacherFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.alibaba.fastjson2.JSON.copyTo;

/**
 * @author Mengdl
 * @date 2024/11/28
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private final TeacherFeignClient teacherFeignClient;
    private final NoticeTransaction noticeTransaction;

    @Override
    public StudentResp detail(Long id) {
        Student student = this.getById(id);
        StudentResp resp = copyTo(student, StudentResp.class);
        R<Teacher> teacherR = teacherFeignClient.getInfo(id);
        if (teacherR.isOk() && teacherR.getData() != null) {
            resp.setTeacherResp(copyTo(teacherR.getData(), TeacherResp.class));
        }
        log.info(JSON.toJSONString(resp));
        //事务完成之后通知
        noticeTransaction.transMessage(consumer -> {
            System.out.println("你好");
        });
        //测试枚举
        IStatus.find(1, GradeType.class).ifPresent(e -> resp.setGradeTypeName(e.getDesc()));
        GradeType one = GradeType.valueOf("ONE");


        return resp;
    }

}
