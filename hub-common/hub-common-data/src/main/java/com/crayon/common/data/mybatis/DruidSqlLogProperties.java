package com.crayon.common.data.mybatis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis 配置打印日志配置
 *
 * @author hytech
 * @date 2021/6/3
 */
@Data
@RefreshScope
@ConfigurationProperties("hy.mybatis")
public class DruidSqlLogProperties {

	/**
	 * 是否打印可执行 sql
	 */
	private boolean showSql = true;

	/**
	 * 跳过表
	 */
	private List<String> skipTable = new ArrayList<>();

}
