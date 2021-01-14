package com.link.admin.system.web;

import com.link.admin.common.utils.HttpUtils;
import com.link.admin.common.utils.IPUtils;
import com.link.admin.common.utils.MD5Utils;
import com.link.admin.common.utils.UUIDUtils;

/**
 * 
 * @ClassName: AppToken
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:52:24
 *
 */
public class AppToken {

	/**
	 * thread security of generate token
	 * 
	 * @param request
	 * @return
	 */
	public static synchronized String generateToken() {

		StringBuilder token = new StringBuilder();
		token.append(HttpUtils.getSession().getId());
		token.append("_");
		token.append(UUIDUtils.generateUUID());
		token.append("_");
		token.append(IPUtils.getIpAddr(HttpUtils.getRequest()));
		return MD5Utils.getMD5AndBase64(token.toString());

	}

}
