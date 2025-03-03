package com.crayon.common.data.deptscope;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 部门拦截器
 *
 * @author Mengdl
 * @date 2025/2/28
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@ImportAutoConfiguration(DeptScopeInterceptor.class)
public @interface EnableDeptInterceptor {
    /**
     * 所属部门数据表
     *
     * @return 所属部门数据表
     */
    String[] value();

    /**
     * 所属部门字段名称
     *
     * @return 所属部门字段名称
     */
    String column() default "dept_id";
}
