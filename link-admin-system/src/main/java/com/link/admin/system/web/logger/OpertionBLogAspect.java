package com.link.admin.system.web.logger;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.link.admin.common.AppContext;
import com.link.admin.common.utils.HttpUtils;
import com.link.admin.common.utils.IPUtils;
import com.link.admin.core.web.mvc.ResponseResult;
import com.link.admin.system.dao.ILogDao;
import com.link.admin.system.domain.BLog;
import com.link.admin.system.domain.UserInfo;
import com.link.admin.system.web.GlobalUser;

/**
 * 日志记录aop,目前已经优化为线程池加异步写入业务日志
 * 
 * @ClassName: OpertionBLogAspect
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:54:37
 *
 */
@Order(7)
@Aspect
@Component
public class OpertionBLogAspect {
	private static ExecutorService pool = Executors.newFixedThreadPool(5);

	@Autowired
	private ILogDao logDao;

	@Around("execution(* com.link.admin..*(..)) "
			+ "and @annotation(com.link.admin.system.web.logger.OpertionBLog)")
	public Object method(ProceedingJoinPoint pjp) throws Throwable {

		BLog log = new BLog();
		log.setTimestamps(System.currentTimeMillis());
		log.setCreatetime(new Date());
		log.setState(1);
		// 先通过注解判断
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod(); // 获取被拦截的方法
		OpertionBLog opertionBLog = method.getAnnotation(OpertionBLog.class);
		// 方法执行后记录日志
		Object result = pjp.proceed();

		if (opertionBLog == null) {
			return result;
		}
		try {
			crateBLog(log, pjp);
			log.setTitle(opertionBLog.title());
			log.setDuration(System.currentTimeMillis() - log.getTimestamps());
			log.setResult(JSONObject.toJSONString(result));
			if (result == null
					|| ((ResponseResult) result).getCode() != AppContext.CODE_20000) {
				log.setState(0);
			}
			// 将有OpertionBLog标记的日志记录到数据库
			pool.execute(new WBLog(log));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private void crateBLog(BLog log, ProceedingJoinPoint pjp) {
		HttpServletRequest request = HttpUtils.getRequest();
		log.setUrl(request.getRequestURL().toString());
		log.setIp(IPUtils.getIpAddr(request));
		log.setRequestmethod(request.getMethod());
		log.setContentType(request.getContentType());
		log.setRequestparams(getRequestParams(request, pjp.getArgs()));
		UserInfo user = GlobalUser.getUserInfo();
		if (user != null) {
			log.setLoginuser(user.getName());
			log.setVsername(user.getVserName());
		}

	}

	private Object getRequestParams(HttpServletRequest request, Object[] args) {
		StringBuilder sb = new StringBuilder();
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			if (request.getContentType() != null) {
				if (request.getContentType().indexOf("json") > -1) {
					for (Object object : args) {
						sb.append(JSONObject.toJSONString(object));
					}
				} else {
					for (Object object : args) {
						sb.append(object);
					}
				}
			}
		} else {
			sb.append(request.getQueryString());
		}
		return sb.toString();
	}

	class WBLog implements Runnable {
		private BLog log;

		public WBLog(BLog log) {
			this.log = log;
		}

		@Override
		public void run() {
			if (log != null) {
				try {
					logDao.insert(log);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
