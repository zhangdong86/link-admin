package com.link.admin.system.run;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.link.admin.common.AppContext;
import com.link.admin.common.config.RsaProperties;
import com.link.admin.core.redis.RedisUtils;
import com.link.admin.system.dao.IDepartmentDao;
import com.link.admin.system.domain.Department;

/**
 * 服务启动后执行的代码
 * 
 * @ClassName: AppRunner
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:55:44
 *
 */
@Component
public class AppRunner implements ApplicationRunner {

	@Autowired
	private IDepartmentDao departmentDao;

	@Autowired
	private Environment env;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		loadConfig();
		loadAllDept();
	}

	private void loadConfig() {
		RsaProperties.privateKey = env.getProperty("linkadmin.private.key");
	}

	/**
	 * 将数据库中所有部门加载到redis中
	 *
	 * @param 设定文件
	 * @return void 返回类型
	 *
	 */
	private void loadAllDept() {
		List<Department> deptList = departmentDao.selectAll();
		if (deptList != null && !deptList.isEmpty()) {
			RedisUtils.getRedis().set(AppContext.Department_Key,
					JSON.toJSONString(deptList));
		}

	}

}
