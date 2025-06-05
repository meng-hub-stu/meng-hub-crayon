package com.crayon.dynamic.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.crayon.dynamic.entity.dto.ManDynamicListDto;
import com.crayon.dynamic.entity.model.ManDynamic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
@Mapper
public interface DynamicMapper {

    /**
     * 批量动态插入
     * @param dataBaseName 库名
     * @param dataList  数据内容
     * @return 结果
     */
    @DS("#dataBaseName")
    int insertBatch2Dynamic(@Param("dataBaseName") String dataBaseName, @Param("dataList") List<ManDynamicListDto> dataList);

    /**
     * 批量动态插入
     *
     * @param tableName 表名
     * @param dataList  数据内容
     * @return 结果
     */
    int insertBatchDynamic(@Param("tableName") String tableName, @Param("dataList") List<ManDynamic> dataList);

    /**
     * 动态插入
     *
     * @param tableName 表名
     * @param man       数据内容
     * @return 结果
     */
    int insertDynamic(@Param("tableName") String tableName, @Param("man") ManDynamic man);


    /**
     * 检查表存不存在
     *
     * @param dataBaseName 库名
     * @param tableName 表名
     * @return 结果
     */
    @DS("#dataBaseName")
    int checkTableIsExists(@Param("dataBaseName") String dataBaseName, @Param("tableName") String tableName);

    /**
     * 检查库存不存在
     *
     * @param dataBaseName 库名
     * @return 结果
     */
    int checkDataBaseIsExists(@Param("dataBaseName") String dataBaseName);

    /**
     * 动态查询
     * @param manDynamic 实体
     * @return  结果
     */
    @Select("select * from man_test where id = #{manDynamic.id}")
    ManDynamic selectByCondition(@Param("manDynamic") ManDynamic manDynamic);

}
