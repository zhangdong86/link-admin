package com.link.admin.system.service;

import java.util.List;

import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.domain.Job;


/**
 * 岗位业务层接口
 * 
 * @ClassName: IJobService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2021年1月7日 上午9:46:59
 *
 */
public interface IJobService {

	JqGridPage<Job> queryPage(Job job);

	List<Job> queryAll();

	Job find(Integer id);

	Job find(Job job);

	boolean add(Job job);

	boolean update(Job job);

	boolean delete(Integer id);

	boolean updateState(Job job);

}
