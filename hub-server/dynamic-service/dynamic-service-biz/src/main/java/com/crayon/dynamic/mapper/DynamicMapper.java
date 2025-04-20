package com.crayon.dynamic.mapper;

import com.crayon.dynamic.entity.ManDynamic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
@Mapper
public interface DynamicMapper {

    /**
     * 批量动态插入
     * @param tableName 表名
     * @param dataList 数据内容
     * @return 结果
     */
    int insertBatchDynamic(@Param("tableName") String tableName, @Param("dataList") List<ManDynamic> dataList);

    /**
     * 动态插入
     * @param tableName 表名
     * @param man 数据内容
     * @return 结果
     */
    int insertDynamic(@Param("man") ManDynamic man);


    /**
     * 检查表存不存在
     * @param tableName 表名
     * @return 结果
     */
    int checkTableIsExists(@Param("tableName") String tableName);

}
