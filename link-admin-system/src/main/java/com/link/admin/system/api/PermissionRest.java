package com.link.admin.system.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.link.admin.core.logger.LoggerUtil;
import com.link.admin.core.web.mvc.BaseRest;
import com.link.admin.core.web.mvc.ResponseResult;
import com.link.admin.system.algorithm.PermissionAlgorithm;
import com.link.admin.system.domain.Permission;
import com.link.admin.system.exception.AuthException;
import com.link.admin.system.service.IPermissionService;
import com.link.admin.system.web.authorize.Requestauthorize;
import com.link.admin.system.web.logger.OpertionBLog;


/**
 * 权限接口
 * 
 * @ClassName: PermissionRest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:56:06
 *
 */
@RestController
@RequestMapping(value = "rest/permission")
public class PermissionRest extends BaseRest {

	@Autowired
	private IPermissionService rightService;

	/**
	 * 获取所有权限
	 * 
	 * @return
	 */
	@Requestauthorize("permission:list")
	@RequestMapping(value = "all")
	public ResponseResult queryAll() {
		ResponseResult rep = new ResponseResult();
		try {
			rep.setResult(PermissionAlgorithm.tree(rightService.queryAll()));
		} catch (AuthException e) {
			rep.setCode(CODE_500);
			rep.setMsg(e.getMessage());
		} catch (Exception e) {
			rep.setCode(CODE_500);
			rep.setMsg("系统异常.请稍后再试");
			LoggerUtil.error(e.getMessage());
		}
		return rep;
	}

	/**
	 * 根据角色id获取对应的权限
	 *
	 * @param @param roleId
	 * @param @return 设定文件
	 * @return ResponseResult 返回类型
	 *
	 */
	@RequestMapping(value = "allByRole/{roleId}")
	public ResponseResult queryAllCheckByRole(
			@PathVariable("roleId") Integer roleId) {
		ResponseResult rep = new ResponseResult();
		try {
			rep.setResult(PermissionAlgorithm.tree(rightService
					.queryByRole(new Integer[] { roleId })));
		} catch (AuthException e) {
			rep.setCode(CODE_500);
			rep.setMsg(e.getMessage());
		} catch (Exception e) {
			rep.setCode(CODE_500);
			rep.setMsg("系统异常.请稍后再试");
			LoggerUtil.error(e.getMessage());
		}
		return rep;
	}

	@Requestauthorize("permission:add")
	@OpertionBLog(title = "新增权限")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ResponseResult add(@RequestBody Permission permission) {
		ResponseResult rep = new ResponseResult();
		try {
			rightService.add(permission);
		} catch (AuthException e) {
			rep.setCode(CODE_500);
			rep.setMsg(e.getMessage());
		} catch (Exception e) {
			rep.setCode(CODE_500);
			rep.setMsg("系统异常.请稍后再试");
			LoggerUtil.error(e.getMessage());
		}

		return rep;
	}

	@Requestauthorize("permission:edit")
	@OpertionBLog(title = "修改权限")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ResponseResult update(@RequestBody Permission right) {
		ResponseResult rep = new ResponseResult();
		try {
			rightService.update(right);
		} catch (AuthException e) {
			rep.setCode(CODE_500);
			rep.setMsg(e.getMessage());
		} catch (Exception e) {
			rep.setCode(CODE_500);
			rep.setMsg("系统异常.请稍后再试");
			LoggerUtil.error(e.getMessage());
		}
		return rep;
	}

	@Requestauthorize("permission:del")
	@OpertionBLog(title = "删除权限")
	@RequestMapping(value = "delete")
	public ResponseResult delete(@RequestParam("id") Integer id) {
		ResponseResult rep = new ResponseResult();
		try {
			rightService.delete(id);
		} catch (AuthException e) {
			rep.setCode(CODE_500);
			rep.setMsg(e.getMessage());
		} catch (Exception e) {
			rep.setCode(CODE_500);
			rep.setMsg("系统异常.请稍后再试");
			LoggerUtil.error(e.getMessage());
		}
		return rep;
	}

}
