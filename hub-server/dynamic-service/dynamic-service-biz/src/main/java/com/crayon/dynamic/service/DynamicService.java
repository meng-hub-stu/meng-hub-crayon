package com.crayon.dynamic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.crayon.dynamic.entity.model.ManDynamic;

import java.util.List;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
public interface DynamicService {

    /**
     * 新增数据
     *
     * @param manDynamics 实体
     * @return 结果
     */
    Boolean saveBatch(List<ManDynamic> manDynamics);

    /**
     * 新增数据
     *
     * @param manDynamic 实体
     * @return 结果
     */
    Boolean save(ManDynamic manDynamic);

    /**
     * 新增数据
     *
     * @param tableName   表名
     * @param manDynamics 实体
     * @return 结果
     */
    Boolean insertBatchDynamic(String tableName, List<ManDynamic> manDynamics);

    /**
     * 新增数据
     *
     * @param tableName  表名
     * @param manDynamic 实体
     * @return 结果
     */
    Boolean insertDynamic(String tableName, ManDynamic manDynamic);

    /**
     * 查询数据
     *
     * @param id 主键
     * @ param name      姓名
     * @return 结果
     */
    ManDynamic detail(Long id, String name);

    /**
     * 查询数据
     *
     * @param id         主键
     * @param dataBaseId 数据库id
     * @param name       姓名
     * @return 结果
     */
    @DS(value = "#dataBaseId")
    ManDynamic detailMt4(Long id, String dataBaseId, String name);

    /**
     * 查询数据
     *
     * @param id         主键
     * @param dataBaseId 数据库id
     * @param name       姓名
     * @return 结果
     */
    @DS(value = "#dataBaseId")
    ManDynamic detailMt5(Long id, String dataBaseId, String name);

}
