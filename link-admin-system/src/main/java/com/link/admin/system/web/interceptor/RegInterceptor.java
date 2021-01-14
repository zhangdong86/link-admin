package com.link.admin.system.web.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * 
* @ClassName: RegInterceptor 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 252956
* @date 2019年10月21日 下午4:53:09 
*
 */
@Configuration
public class RegInterceptor implements WebMvcConfigurer {
	@Autowired
	private AppContextInterceptor appContextInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(appContextInterceptor);
	}
}
