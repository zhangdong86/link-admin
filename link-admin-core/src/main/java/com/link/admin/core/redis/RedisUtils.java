package com.link.admin.core.redis;

import org.springframework.stereotype.Component;

import com.link.admin.core.web.SpringUtils;


/**
 * 
 * @ClassName: RedisUtils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 252956
 * @date 2019年10月21日 下午4:54:25
 *
 */
@Component
public class RedisUtils {
	// cache
	private static volatile IRedis redis = null;

	public static IRedis getRedis() {
		if (redis == null) {
			redis = SpringUtils.getBean(IRedis.class);
			;
		}
		return redis;
	}

}
