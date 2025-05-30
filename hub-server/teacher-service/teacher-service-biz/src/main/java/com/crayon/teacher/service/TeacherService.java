package com.crayon.teacher.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crayon.base.test.yu.ed.service.BaseService;
import com.crayon.teacher.entity.Teacher;

import java.util.List;

/**
 * @author Mengdl
 * @date 2024/11/28
 */
public interface TeacherService extends IService<Teacher>, BaseService {

    /**
     * 瀑布流加载
     * @param offset 已加载数量
     * @param limit 加载数量
     * @return 加载数据
     */
    List<Teacher> scroll(Integer offset, Integer limit);

    /**
     * 详情
     * @param id 主键
     * @return 详情
     */
    Teacher detail(Long id);

}
