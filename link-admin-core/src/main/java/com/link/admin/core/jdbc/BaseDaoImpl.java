package com.link.admin.core.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.link.admin.core.jdbc.page.ISQLPageHandle;
import com.link.admin.core.jdbc.support.AbstractJdbcSupport;


/**
 * 
 * @ClassName: BaseDaoImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:55:09
 *
 */
public class BaseDaoImpl extends AbstractJdbcSupport {
	@Autowired
	@Qualifier("baseJdbcTemplate")
	protected JdbcTemplate jdbcTemplate;

	/**
	 * 分页处理
	 */
	@Autowired
	@Qualifier("mysqlSQLPageHandle")
	protected ISQLPageHandle mysqlSQLPageHandle;

	@Override
	protected JdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}

	protected ISQLPageHandle getSqlPageHandle() {
		return mysqlSQLPageHandle;
	}

}
