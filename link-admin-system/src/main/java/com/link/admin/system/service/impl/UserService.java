package com.link.admin.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.link.admin.common.AppContext;
import com.link.admin.common.config.RsaProperties;
import com.link.admin.common.constant.DataScopeType;
import com.link.admin.common.utils.BeanUtils;
import com.link.admin.common.utils.MD5Utils;
import com.link.admin.common.utils.RsaUtils;
import com.link.admin.common.utils.StringUtils;
import com.link.admin.common.utils.UUIDUtils;
import com.link.admin.core.exception.SystemException;
import com.link.admin.core.redis.RedisUtils;
import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.algorithm.DepartmentAlgorithm;
import com.link.admin.system.algorithm.PermissionAlgorithm;
import com.link.admin.system.dao.IUserDao;
import com.link.admin.system.domain.Department;
import com.link.admin.system.domain.Job;
import com.link.admin.system.domain.LoginVO;
import com.link.admin.system.domain.ModifyPwdVO;
import com.link.admin.system.domain.Permission;
import com.link.admin.system.domain.Role;
import com.link.admin.system.domain.UserInfo;
import com.link.admin.system.domain.UserInfoVO;
import com.link.admin.system.domain.UserRole;
import com.link.admin.system.exception.AuthException;
import com.link.admin.system.service.IDepartmentService;
import com.link.admin.system.service.IJobService;
import com.link.admin.system.service.IPermissionService;
import com.link.admin.system.service.IRoleService;
import com.link.admin.system.service.IUserService;
import com.link.admin.system.web.AppToken;
import com.link.admin.system.web.GlobalUser;

/**
 * 用户业务层实现类
 * 
 * @ClassName: UserService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2021年1月7日 上午9:47:58
 *
 */
@Service
public class UserService implements IUserService {
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IPermissionService rightService;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IJobService jobService;

	@Override
	public String login(LoginVO vo) throws Exception {
		validateLoginCodition(vo);
		UserInfo userInfo = loginProcess(vo);
		String token = AppToken.generateToken();
		userInfo.setToken(token);
		GlobalUser.setUserInfo(userInfo);
		return token;
	}

	private void validateLoginCodition(LoginVO vo) {
		if (vo == null) {
			throw new AuthException("登录失败");
		}
		if (StringUtils.isBlank(vo.getUsername())) {
			throw new AuthException("用户名不能为空");
		}
		if (StringUtils.isBlank(vo.getPassword())) {
			throw new AuthException("密码不能为空");
		}
	}

	private UserInfo loginProcess(LoginVO vo) throws Exception {

		// 密码解密
		String password = RsaUtils.decryptByPrivateKey(
				RsaProperties.privateKey, vo.getPassword());

		UserInfo param = new UserInfo();
		param.setName(vo.getUsername());
		// 根据登陆名
		UserInfo user = userDao.find(param);
		if (user == null) {
			throw new AuthException("无效的用户名或密码");
		}
		String md5password = MD5Utils.getMD5Encoding(password);
		if (!user.getPassword().equals(md5password)) {
			throw new AuthException("无效的用户名或密码");
		}
		if (GlobalUser.user_unable.equals(user.getState())) {
			throw new AuthException("该账户已被禁用");
		}
		return user;
	}

	@Override
	public UserInfo info() {
		// 判断用户数据是否为空
		UserInfo user = GlobalUser.getUserInfo();
		if (user == null) {
			throw new AuthException("用户未登录");
		}

		// 获取当前用户的所属公司
		Department company = DepartmentAlgorithm.findCompany(user.getDeptid(),
				JSONArray.parseArray(
						RedisUtils.getRedis().get(AppContext.Department_Key),
						Department.class));
		if (company == null) {
			throw new SystemException("未查询到当前用户的所属公司");
		}
		user.setCompanyId(company.getId());
		user.setCompanyName(company.getName());

		// 部门
		Department dept = departmentService.query(user.getDeptid());
		if (dept == null) {
			throw new AuthException("未查询到当前用户的部门");
		}
		user.setDeptName(dept.getName());
		if (user.getJobid() != null) {
			// 岗位
			Job job = jobService.find(user.getJobid());
			if (job != null) {
				user.setJobName(job.getName());
			}
		}
		// 角色
		List<Role> roles = roleService.queryByUser(user.getUid());
		if (roles == null || roles.isEmpty()) {
			throw new AuthException("当前用户未分配角色");
		}

		List<Integer> roleIds = new ArrayList<Integer>();
		for (Role role : roles) {
			roleIds.add(role.getId());
		}
		// 角色对应的权限
		List<Permission> permissionList = rightService.queryByRole(roleIds
				.toArray(new Integer[roleIds.size()]));

		if (permissionList == null || permissionList.isEmpty()) {
			throw new AuthException("当前用户为的角色未分配权限");
		}
		// 用户菜单
		List<Permission> menus = new ArrayList<Permission>();
		for (Permission perm : permissionList) {
			if (perm.isMenu()) {
				menus.add(perm);
			}
		}
		menus = PermissionAlgorithm.tree(menus);

		user.setRoles(roles);
		user.setMenus(PermissionAlgorithm.buildMenu(menus));
		user.setPermissions(permissionList);
		user.setDatascope(getDataScope(user));
		user.setDatascopes(getDataScopes(user));
		GlobalUser.setUserInfo(user);
		return user;
	}

	/**
	 * 找出用户角色拥有最大的数据权限
	 *
	 * @param @param user
	 * @param @return 设定文件
	 * @return Integer 返回类型
	 *
	 */
	private Integer getDataScope(UserInfo user) {
		Integer datascope = DataScopeType.self;
		for (Role role : user.getRoles()) {
			if (role.getData_scope() < datascope) {
				datascope = role.getData_scope();
			}
		}
		return datascope;
	}

	/***
	 * 获取角色对应的数据权限
	 *
	 * @param @param roles
	 * @param @return 设定文件
	 * @return List<Integer> 返回类型
	 *
	 */
	private List<Integer> getDataScopes(UserInfo user) {
		List<Integer> dataScopeList = new ArrayList<Integer>();
		List<Department> allDeptList = JSONArray.parseArray(RedisUtils
				.getRedis().get(AppContext.Department_Key), Department.class);
		for (Role role : user.getRoles()) {
			// 1全部数据权限
			if (DataScopeType.all.equals(role.getData_scope())) {
				for (Department dept : allDeptList) {
					dataScopeList.add(dept.getId());
				}
				// 2用户自定义数据权限
			} else if (DataScopeType.customize.equals(role.getData_scope())) {
				dataScopeList.addAll(roleService.queryDataScope(role.getId()));
				// 3本部门及以下数据权限
			} else if (DataScopeType.deptAndBelow.equals(role.getData_scope())) {
				List<Department> findSelfAndAllChild = DepartmentAlgorithm
						.findSelfAndAllChild(user.getDeptid(), allDeptList);
				for (Department dept : findSelfAndAllChild) {
					dataScopeList.add(dept.getId());
				}
				// 4本部门数据权限
			} else if (DataScopeType.dept.equals(role.getData_scope())) {
				dataScopeList.add(user.getDeptid());
			}
		}
		if (dataScopeList.isEmpty()) {
			// 5仅本人数据权限
			return null;
		}
		// 去重复
		Set<Integer> ownedSet = new HashSet<Integer>();
		for (Integer id : dataScopeList) {
			ownedSet.add(id);
		}
		dataScopeList = new ArrayList<Integer>(ownedSet);

		return dataScopeList;
	}

	@Override
	public void modifyPwd(ModifyPwdVO vo) {

		if (vo == null || StringUtils.isBlank(vo.getOldPassword())
				|| StringUtils.isBlank(vo.getNewPassword())
				|| StringUtils.isBlank(vo.getConfirmNewPassword())) {
			throw new AuthException("必填项不能为空");
		}
		if (!vo.getNewPassword().equals(vo.getConfirmNewPassword())) {
			throw new AuthException("两次输入密码必须相同");
		}
		UserInfo user = GlobalUser.getUserInfo();

		if (!user.getPassword().equals(
				MD5Utils.getMD5Encoding(vo.getOldPassword()))) {
			throw new AuthException("原密码错误");
		}

		UserInfo userInfo = userDao.select(user.getUid());
		if (userInfo == null) {
			throw new AuthException("用户不存在");
		}
		userInfo.setPassword(MD5Utils.getMD5Encoding(vo.getNewPassword()));
		userDao.update(userInfo);
		// 更新内存中的密码
		GlobalUser.setUserInfo(userInfo);

	}

	@Override
	public JqGridPage<UserInfo> queryPage(UserInfo user) {
		if (user == null) {
			throw new AuthException("参数不能为空");
		}
		JqGridPage<UserInfo> page = userDao.selectPage(user);
		if (page.getRows() != null && !page.getRows().isEmpty()) {
			for (UserInfo userInfo : page.getRows()) {
				List<Role> roleList = roleService
						.queryByUser(userInfo.getUid());
				userInfo.setRoles(roleList);
			}
		}
		return page;
	}

	@Transactional(value = "baseTransactionManager")
	@Override
	public boolean add(UserInfoVO vo) throws AuthException {

		if (StringUtils.isBlank(vo.getVserName())) {
			throw new AuthException("真实姓名不能为空");
		}
		if (StringUtils.isBlank(vo.getName())) {
			throw new AuthException("登陆名不能为空");
		}
		if (StringUtils.isBlank(vo.getPassword())) {
			throw new AuthException("登陆密码不能为空");
		}
		if (vo.getDeptid() == null) {
			throw new AuthException("请选择部门");
		}
		UserInfo user = new UserInfo();
		BeanUtils.copyObject(user, vo);
		user.setCreateTime(new Date());
		String password = MD5Utils.getMD5Encoding(user.getPassword());
		user.setPassword(password);
		user.setUid(UUIDUtils.generateUUID());
		// 保存用户信息
		int result = userDao.insert(user);
		if (result < 0) {
			throw new AuthException("保存失败");
		}

		if (vo.getRoleIds() != null && vo.getRoleIds().length > 0) {
			List<UserRole> urList = new ArrayList<UserRole>();
			UserRole ur;
			for (Integer roleId : vo.getRoleIds()) {
				ur = new UserRole();
				ur.setUserId(user.getUid());
				ur.setRoleId(roleId);
				urList.add(ur);
			}
			// 保存用户分配的角色
			saveRelationRole(urList);
		}
		return true;
	}

	@Override
	public boolean update(UserInfoVO vo) throws AuthException {

		if (vo.getUid() == null) {
			throw new AuthException("用户不存在");
		}
		if (vo.getDeptid() == null) {
			throw new AuthException("请选择部门");
		}
		UserInfo user = userDao.select(vo.getUid());
		if (user == null) {
			throw new AuthException("用户不存在");
		}
		String password = "";
		if (!user.getPassword().equals(vo.getPassword())) {
			password = MD5Utils.getMD5Encoding(vo.getPassword());
		}
		BeanUtils.copyObject(user, vo);
		if (StringUtils.isNotBlank(password)) {
			user.setPassword(password);
		}
		int result = userDao.update(user);
		if (result < 0) {
			throw new AuthException("修改失败");
		}

		// 删除用户角色
		UserRole delUr = new UserRole();
		delUr.setUserId(user.getUid());
		userDao.delete(delUr);

		if (vo.getRoleIds() != null && vo.getRoleIds().length > 0) {
			List<UserRole> urList = new ArrayList<UserRole>();
			UserRole ur;
			for (Integer roleId : vo.getRoleIds()) {
				ur = new UserRole();
				ur.setUserId(user.getUid());
				ur.setRoleId(roleId);
				urList.add(ur);
			}
			// 保存用户分配的角色
			saveRelationRole(urList);
		}
		return true;

	}

	public boolean saveRelationRole(List<UserRole> urList) {
		userDao.insert(urList);
		return true;
	}

	@Override
	public boolean delete(String uid) throws AuthException {
		if (StringUtils.isBlank(uid)) {
			throw new AuthException("uid不能为空");
		}
		UserInfo user = new UserInfo();
		user.setUid(uid);

		int result = userDao.delete(user);
		if (result < 0) {
			throw new AuthException("删除失败");
		}
		return true;
	}

	@Override
	public UserInfo find(String uid) {
		if (StringUtils.isBlank(uid)) {
			return null;
		}
		UserInfo userInfo = userDao.select(uid);
		return userInfo;
	}

	@Override
	public UserInfo find(UserInfo user) {
		return userDao.find(user);
	}

	/**
	 * 更新用户状态
	 */
	@Override
	public boolean updateState(UserInfoVO vo) {
		if (StringUtils.isBlank(vo.getUid())) {
			throw new AuthException("uid不能为空");
		}
		int result = userDao.updateState(vo.getUid(), vo.getState());
		if (result < 0) {
			throw new AuthException("更新失败");
		}
		return true;
	}
}
