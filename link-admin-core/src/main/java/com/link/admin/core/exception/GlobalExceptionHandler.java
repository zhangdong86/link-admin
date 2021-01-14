package com.link.admin.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.link.admin.common.AppContext;
import com.link.admin.core.web.mvc.ResponseResult;

/**
 * 全局异常
 * 
 * @ClassName: GlobalExceptionHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年11月11日 下午1:28:23
 *
 */
@ControllerAdvice
class GlobalExceptionHandler {

	// json exceptin
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseResult jsonErrorHandler(HttpServletRequest req, Exception e)
			throws Exception {
		ResponseResult r = new ResponseResult();
		r.setCode(AppContext.CODE_50000);
		r.setMsg("系统异常");
		return r;
	}
}