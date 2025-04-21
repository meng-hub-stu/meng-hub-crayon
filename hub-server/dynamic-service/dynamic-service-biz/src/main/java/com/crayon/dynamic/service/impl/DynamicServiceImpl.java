package com.crayon.dynamic.service.impl;

import com.crayon.dynamic.database.ManualDataSource;
import com.crayon.dynamic.entity.dto.ManDynamicDto;
import com.crayon.dynamic.entity.model.ManDynamic;
import com.crayon.dynamic.mapper.DynamicMapper;
import com.crayon.dynamic.service.DynamicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.crayon.dynamic.database.DynamicTableNameUtils.getDdlTable;
import static com.crayon.dynamic.database.DynamicTableNameUtils.getDmlTable;

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
        String tableName = manDynamic.getTableName();
        manualDataSource.checkOrCreateTable(getDmlTable(tableName));
        return dynamicMapper.insertDynamic(getDdlTable(tableName), manDynamic) > 0;
    }

    @Override
    public Boolean saveBatch(List<ManDynamic> manDynamics) {
        String tableName = manDynamics.getFirst().getTableName();
        manualDataSource.checkOrCreateTable(getDmlTable(tableName));
        return dynamicMapper.insertBatchDynamic(getDdlTable(tableName), manDynamics) > 0;
    }

    @Override
    public Boolean insertBatchDynamic(String tableName, List<ManDynamic> manDynamics) {
        return dynamicMapper.insertBatchDynamic(getDdlTable(tableName), manDynamics) > 0;
    }

    @Override
    public Boolean insertDynamic(String tableName, ManDynamic manDynamic) {
        return dynamicMapper.insertDynamic(getDdlTable(tableName), manDynamic) > 0;
    }


    //TODO 以后多学习函数变成
    public BiConsumer<ManDynamic, List<ManDynamicDto>> convert() {
        Function<ManDynamic, ManDynamicDto> function = manDynamic -> {
            if (manDynamic == null) {
                return null;
            }
            return ManDynamicDto.builder()
                    .sex(manDynamic.getName())
                    .build();
        };
        Consumer<ManDynamic> consumer = manDynamic -> {
            manDynamic.setName("123");
        };

        Predicate<ManDynamic> predicate = manDynamic -> {
            return "123".equals(manDynamic.getName());
        };

        return (manDynamic, list) -> {
            consumer.accept(manDynamic);
            if (predicate.test(manDynamic)) {
                list.add(function.apply(manDynamic));
            }
        };
    }

    public void test() {
        List<ManDynamicDto> result = new ArrayList<>();
        this.convert().accept(new ManDynamic(), result);
    }

}
