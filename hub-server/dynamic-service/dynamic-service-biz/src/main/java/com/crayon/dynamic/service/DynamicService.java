package com.crayon.dynamic.service;

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

}
