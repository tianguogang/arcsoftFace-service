package com.tgg.arcsoftfaceservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public final class RedisUtil {

	/**
	 * 字符串类型存储
	 */
	private static StringRedisTemplate stringRedisTemplate;
	/**
	 * 对象类型存储
	 */
	private static RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	public RedisUtil(StringRedisTemplate stringRedisTemplate, RedisTemplate<Object, Object> redisTemplate) {
		RedisUtil.stringRedisTemplate = stringRedisTemplate;
		RedisUtil.redisTemplate = redisTemplate;
	}

	public static void staticsetObject(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public static Object getObject(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public static void setString(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 存储字符串数据，过期时间单位秒
	 */
	public static void setString(String key, String value, int seconds) {
		stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
	}

	/**
	 * 存储字符串数据，过期时间单位小时
	 */
	public static void setString(String key, String value, Long time) {
		stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.HOURS);
	}

	/**
	 * 存储复杂的对象类型，过期时间单位小时
	 */
	public static void setObject(String key, Object value, Long hour) {
		redisTemplate.opsForValue().set(key, value, hour, TimeUnit.HOURS);
	}

	/**
	 * 判断是否存在缓存key
	 */
	public static boolean hasKey(String code) {
		return stringRedisTemplate.hasKey(code);
	}

	/**
	 * 根据key获取缓存值，类型String
	 */
	public static String getString(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * 设置key的过期时间，单位秒
	 */
	public static boolean expire(String key, int seconds) {
		stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
		return true;
	}

	/**
	 * 根据key删除缓存
	 */
	public static void clean(String key) {
		stringRedisTemplate.delete(key);
	}

	/**
	 * 分布式获取自增ID
	 */
	public static Long incr(String key) {
		RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		return entityIdCounter.incrementAndGet();
	}

	/**
	 * 根据key删除缓存
	 */
	public static boolean del(String key) {
		return stringRedisTemplate.delete(key);
	}

	/**
	 * 分布式获取自减ID值
	 */
	public static Long decr(String key) {
		RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		Long decrement = entityIdCounter.decrementAndGet();
		return decrement;
	}

	/**
	 * 获取key所对应的剩余过期时间,返回秒
	 */
	public static Long getExpireDate(String key) {
		Long rtn = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
		if (null == rtn) {
			return 0L;
		}
		return rtn;
	}
}
