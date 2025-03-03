package com.crayon.common.data.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.crayon.common.data.deptscope.DeptScopeInterceptor;
import com.crayon.common.data.resolver.SqlFilterArgumentResolver;
import com.crayon.common.data.tenant.TenantConfigProperties;
import com.crayon.common.data.tenant.TokenTenantHandler;
import com.crayon.student.feign.StudentFeignClient;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

/**
 * @author hytech
 * @date 2020-02-08
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DruidSqlLogProperties.class, TenantConfigProperties.class})
public class MybatisPlusConfiguration implements WebMvcConfigurer {

    /**
     * mybatis plus 拦截器配置
     *
     * @return PigxDefaultDatascopeHandle
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantLineInnerInterceptor tenantLineInnerInterceptor,
                                                         DeptScopeInterceptor deptScopeInterceptor) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 注入多租户支持
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        // 部门权限
        interceptor.addInnerInterceptor(deptScopeInterceptor);
        // 支持乐观锁
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页支持
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    /**
     * 创建租户维护处理器对象
     *
     * @return 处理后的租户维护处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantConfigProperties tenantConfigProperties) {
        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
        tenantLineInnerInterceptor.setTenantLineHandler(new TokenTenantHandler(tenantConfigProperties));
        return tenantLineInnerInterceptor;
    }

    /**
     * 部门权限拦截器
     *
     * @return DataScopeInterceptor
     */
    @Bean
    @Primary
//    @ConditionalOnMissingBean
    public DeptScopeInterceptor deptScopeInterceptor(StudentFeignClient systemFeignClient) {
        DeptScopeInterceptor deptScopeInterceptor = new DeptScopeInterceptor();
        deptScopeInterceptor.setStudentFeignClient(systemFeignClient);
        return deptScopeInterceptor;
    }

    /**
     * 增加请求参数解析器，对请求中的参数注入SQL 检查
     *
     * @param resolverList
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolverList) {
        resolverList.add(new SqlFilterArgumentResolver());
    }

    /**
     * SQL 日志格式化
     *
     * @return DruidSqlLogFilter
     */
    @Bean
    public DruidSqlLogFilter sqlLogFilter(DruidSqlLogProperties properties) {
        return new DruidSqlLogFilter(properties);
    }

    /**
     * 审计字段自动填充
     *
     * @return {@link MetaObjectHandler}
     */
    @Bean
    public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
        return new MybatisPlusMetaObjectHandler();
    }

    /**
     * 数据库方言配置
     *
     * @return
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MYSQL", "mysql");
        properties.setProperty("REDSHIFT", "redshift");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

}
