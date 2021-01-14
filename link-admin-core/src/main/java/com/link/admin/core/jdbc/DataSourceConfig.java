package com.link.admin.core.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 数据源配置
 * 
 * @ClassName: DataSourceConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:54:55
 *
 */
@Configuration
public class DataSourceConfig {
	@Bean(name = "baseDataSource")
	@Qualifier("baseDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.base")
	@Primary
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "baseJdbcTemplate")
	public JdbcTemplate primaryJdbcTemplate(
			@Qualifier("baseDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	/****** 配置事务管理 ********/

	@Bean
	public PlatformTransactionManager baseTransactionManager(
			@Qualifier("baseDataSource") DataSource prodDataSource) {
		return new DataSourceTransactionManager(prodDataSource);
	}

	// ---------------------------------------------------------

	@Bean(name = "ydhisDataSource")
	@Qualifier("ydhisDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.ydhis")
	public DataSource ydhisDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "ydhisJdbcTemplate")
	public JdbcTemplate ydhisJdbcTemplate(
			@Qualifier("ydhisDataSource") DataSource ydhisSource) {
		return new JdbcTemplate(ydhisSource);
	}

	@Bean
	public PlatformTransactionManager ydhisTransactionManager(
			@Qualifier("ydhisDataSource") DataSource ydhisDataSource) {
		return new DataSourceTransactionManager(ydhisDataSource);
	}
}