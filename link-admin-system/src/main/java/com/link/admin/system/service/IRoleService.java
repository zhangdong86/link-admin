package com.link.admin.system.service;

import java.util.List;

import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.domain.Role;
import com.link.admin.system.exception.AuthException;

/**
 * 角色业务层接口
 * 
 * @ClassName: IRoleService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2021年1月7日 上午9:45:37
 *
 */
public interface IRoleService {

	JqGridPage<Role> queryPage(Role role);

	List<Role> queryAll();

	List<Role> queryByUser(String userId) ;

	Role query(Integer code) ;

	boolean add(Role role) ;

	boolean update(Role role) ;

	boolean delete(Integer code) ;

	boolean saveRelationDataScope(Role role);

	List<Integer> queryDataScope(Integer roleId);

	boolean updateState(Role role);

}
