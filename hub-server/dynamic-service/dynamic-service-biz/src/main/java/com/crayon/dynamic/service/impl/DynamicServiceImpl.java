package com.crayon.dynamic.service.impl;

import com.crayon.dynamic.database.ManualDataSource;
import com.crayon.dynamic.entity.ManDynamic;
import com.crayon.dynamic.mapper.DynamicMapper;
import com.crayon.dynamic.service.DynamicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.crayon.dynamic.database.DynamicTableNameUtils.getCreateTable;
import static com.crayon.dynamic.database.DynamicTableNameUtils.getCurrentTable;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DynamicServiceImpl implements DynamicService {

    private final DynamicMapper dynamicMapper;
    private final ManualDataSource manualDataSource;

    @Override
    public Boolean save(ManDynamic manDynamic) {
        Boolean isExists = this.checkTableIsExists(manDynamic.getTableName());
        if (!isExists) {
            manualDataSource.createTable(manDynamic.getTableName());
        }
        return this.insertDynamic(manDynamic.getTableName(), manDynamic);
    }

    @Override
    public Boolean saveBatch(List<ManDynamic> manDynamics) {
        String tableName = manDynamics.getFirst().getTableName();
        Boolean isExists = this.checkTableIsExists(tableName);
        if (!isExists) {
            manualDataSource.createTable(tableName);
        }
        return this.insertBatchDynamic(tableName, manDynamics);
    }

    @Override
    public Boolean checkTableIsExists(String tableName) {
        return dynamicMapper.checkTableIsExists(getCreateTable(tableName)) > 0;
    }

    @Override
    public Boolean insertBatchDynamic(String tableName, List<ManDynamic> manDynamics) {
        return dynamicMapper.insertBatchDynamic(getCurrentTable(tableName), manDynamics) > 0;
    }

    @Override
    public Boolean insertDynamic(String tableName, ManDynamic manDynamic) {
        return dynamicMapper.insertDynamic(getCurrentTable(tableName), manDynamic) > 0;
    }

}
