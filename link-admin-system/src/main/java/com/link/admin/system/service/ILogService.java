package com.link.admin.system.service;

import com.link.admin.core.web.mvc.JqGridPage;
import com.link.admin.system.domain.BLog;
import com.link.admin.system.domain.BLogVO;


/**
 * 日志业务层接口
* @ClassName: ILogService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 252956 
* @date 2021年1月7日 上午9:46:33 
*
 */

public interface ILogService {

	JqGridPage<BLog> queryPage(BLogVO vo);
}
