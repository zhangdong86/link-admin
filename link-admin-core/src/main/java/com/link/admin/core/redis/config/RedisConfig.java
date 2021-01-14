package com.link.admin.core.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//@Configuration
//public class RedisConfig {
//
//	@Bean
//	public RedisConnectionFactory factory() {
//		return new LettuceConnectionFactory();
//	}
//
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//		template.setConnectionFactory(factory);
//		template.setKeySerializer(new StringRedisSerializer());
//		template.setValueSerializer(new RedisObjectSerializer());
//		return template;
//	}
//
//}
