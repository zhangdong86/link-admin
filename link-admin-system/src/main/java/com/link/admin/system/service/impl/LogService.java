package com.link.admin.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.link.admin.common.utils.DateUtils;
import com.link.admin.common.utils.StringUtils;
import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.dao.ILogDao;
import com.link.admin.system.domain.BLog;
import com.link.admin.system.domain.BLogVO;
import com.link.admin.system.exception.AuthException;
import com.link.admin.system.service.ILogService;

/**
 * 日志业务层实现类
 * 
 * @ClassName: LogService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2021年1月7日 上午9:52:38
 *
 */
@Service
public class LogService implements ILogService {
	@Autowired
	private ILogDao logDao;

	@Override
	public JqGridPage<BLog> queryPage(BLogVO vo) {
		if (vo == null) {
			throw new AuthException("参数不能为空");
		}
		if (StringUtils.isBlank(vo.getStarttime())
				|| StringUtils.isBlank(vo.getEndtime())) {
			throw new AuthException("请选择时间范围");
		}
		return logDao.selectPage(vo);
	}

}
