package com.crayon.dynamic;

import com.crayon.dynamic.entity.model.ManDynamic;
import com.crayon.dynamic.service.DynamicService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

/**
 * @author Mengdl
 * @date 2025/04/19
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = DynamicApplication.class)
public class TestDynamic {

    @Autowired
    private DynamicService dynamicService;

    @Test
    public void testSave() {
        ManDynamic manDynamic = ManDynamic.builder()
                .id(3L)
                .name("Mengdl")
                .tableName("test.01")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .delFlag(0)
                .build();
        Boolean result = dynamicService.save(manDynamic);
        System.out.println("执行结果" + result);
    }

}
