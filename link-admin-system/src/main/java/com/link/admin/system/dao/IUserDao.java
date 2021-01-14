package com.link.admin.system.dao;

import java.util.List;

import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.domain.UserInfo;
import com.link.admin.system.domain.UserRole;

/**
 * 用户接口
* @ClassName: IUserDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 252956 
* @date 2021年1月7日 上午9:36:22 
*
 */
public interface IUserDao {

	JqGridPage<UserInfo> selectPage(UserInfo user);

	UserInfo find(UserInfo user);

	List<UserInfo> findList(UserInfo user);

	UserInfo select(String id);

	int insert(UserInfo user);

	int[] insert(List<UserRole> list);

	int update(UserInfo user);

	int updateState(String uid,int state);

	int delete(UserInfo user);

	int delete(UserRole userRelationRole);

}
