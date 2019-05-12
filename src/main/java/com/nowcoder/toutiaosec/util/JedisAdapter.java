package com.nowcoder.toutiaosec.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisAdapter implements InitializingBean {
	/*InitializingBean接口为bean提供了初始化方法的方式，
	它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean的时候都会执行该方法。*/

	private JedisPool pool=null;
	
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);


	@Override
	public void afterPropertiesSet() throws Exception {//在类初始化时执行此方法
		// TODO Auto-generated method stub
		pool=new JedisPool("localhost",6379);//创建Jedis连接池对象
	}
	
	public Jedis getJedis() {
		return pool.getResource();
	}
	
	public void set(String key,String value) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			jedis.set(key, value);
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
		}finally {
			if(jedis!=null) jedis.close();
		}
	}
	
	public String get(String key) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.get(key);
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
			return null;
		}finally {
			if(jedis!=null) jedis.close();
		}
	}
	
	public long sadd(String set,String value) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.sadd(set, value);
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) jedis.close();
		}				
	}
	
	public long srem(String set,String value) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.srem(set, value);
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) jedis.close();
		}
	}
	
	public boolean sismember(String set,String value) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.sismember(set, value);
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
			return false;
		}finally {
			if(jedis!=null) jedis.close();
		}
	}
	
	public long scard(String set) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.scard(set);
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) jedis.close();
		}
	}
	
	public long lpush(String key,String value) {
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.lpush(key, value);//返队列长度
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
			return 0;
		}finally {
			if(jedis!=null) jedis.close();
		}
	}
	
	public List<String> brpop(int timeout,String key){//时间延迟
		Jedis jedis=null;
		try {
			jedis=pool.getResource();
			return jedis.brpop(timeout,key);
		}catch(Exception e) {
			logger.error("redis异常："+e.getMessage());
			return null;
		}finally {
			if(jedis!=null) jedis.close();
		}
	}
	
	public void setObject(String key,Object obj) {
		//对象缓存入redis的一种方式,值包装成JSON
		set(key,JSON.toJSONString(obj));//Json将对象转化为Json字符串
	}
	
	public <T> T getObject(String key,Class<T> c) {//对应的取出该对象
		String value=get(key);
		if(value!=null) return JSON.parseObject(value, c);//JSON将Json字符串转化为相应的对象
		return null;
	}
}
