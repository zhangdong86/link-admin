package com.link.admin.system.service;

import java.util.List;

import com.link.admin.system.domain.Permission;
import com.link.admin.system.exception.AuthException;


/**
 * 权限业务层接口
 * 
 * @ClassName: IPermissionService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2021年1月7日 上午9:46:03
 *
 */
public interface IPermissionService {

	List<Permission> queryAll();

	List<Permission> queryByRole(Integer[] roleIds);

	Permission query(Integer code) ;

	boolean add(Permission right) ;

	boolean update(Permission right) ;

	boolean delete(Integer code) ;

}
