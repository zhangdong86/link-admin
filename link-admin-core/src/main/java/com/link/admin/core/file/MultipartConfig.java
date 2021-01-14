package com.link.admin.core.file;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

@Configuration
public class MultipartConfig {

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(DataSize.of(100, DataUnit.MEGABYTES)); // 100MB
		// / 设置总上传数据总大小
		factory.setMaxRequestSize(DataSize.of(100, DataUnit.MEGABYTES));// 100MB
		return factory.createMultipartConfig();
	}
}