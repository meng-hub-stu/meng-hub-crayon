package com.crayon.dynamic.database;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * mysql配置数据库
 *
 * @author Mengdl
 * @date 2025/04/19
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "mysql.write")
public class MysqlWriteProperties {

    @Schema(description = "mysql地址")
    private String mysqlUrl;

    @Schema(description = "mysql用户名")
    private String mysqlUser;

    @Schema(description = "mysql密码")
    private String mysqlPwd;

}
