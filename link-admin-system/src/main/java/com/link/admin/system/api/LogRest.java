package com.link.admin.system.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.link.admin.core.logger.LoggerUtil;
import com.link.admin.core.web.mvc.BaseRest;
import com.link.admin.core.web.mvc.ResponseResult;
import com.link.admin.system.domain.BLogVO;
import com.link.admin.system.exception.AuthException;
import com.link.admin.system.service.ILogService;
import com.link.admin.system.web.authorize.Requestauthorize;


/**
 * 日志接口
 * 
 * @ClassName: LogRest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:56:10
 *
 */
@RestController
@RequestMapping(value = "/rest/logs")
public class LogRest extends BaseRest {
	@Autowired
	private ILogService logService;

	@Requestauthorize("blog:list")
	@RequestMapping(value = "/blog/list", method = RequestMethod.POST)
	public ResponseResult list(@RequestBody BLogVO vo) {
		ResponseResult rep = new ResponseResult();
		try {
			rep.setResult(logService.queryPage(vo));
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
