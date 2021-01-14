package com.link.admin.system.service;

import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.domain.LoginVO;
import com.link.admin.system.domain.ModifyPwdVO;
import com.link.admin.system.domain.UserInfo;
import com.link.admin.system.domain.UserInfoVO;


/**
 * 用户业务层接口
 * 
 * @ClassName: IUserService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2021年1月7日 上午9:45:23
 *
 */
public interface IUserService {

	JqGridPage<UserInfo> queryPage(UserInfo user);

	String login(LoginVO vo) throws Exception;

	UserInfo info();

	UserInfo find(String uid);

	UserInfo find(UserInfo user);

	void modifyPwd(ModifyPwdVO vo);

	boolean add(UserInfoVO vo);

	boolean update(UserInfoVO vo);

	boolean delete(String uid);

	boolean updateState(UserInfoVO vo);

}
