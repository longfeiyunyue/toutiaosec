package com.nowcoder.toutiaosec;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nowcoder.toutiaosec.domain.User;
import com.nowcoder.toutiaosec.util.JedisAdapter;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaosecApplication.class)
public class JedisTests {
	@Autowired
	JedisAdapter jedisAdapter;
	
	public static void print(int index,Object obj) {
		System.out.println(String.format("%d,%s", index,obj.toString()));
	}
	
	@Test
	public void testObj() {
		 User user = new User();
	        user.setHeadUrl("http://images.nowcoder.com/head/100t.png");
	        user.setName("user1");
	        user.setPassword("abc");
	        user.setSalt("def");
	        jedisAdapter.setObject("user1", user);
	       User u= jedisAdapter.getObject("user1", User.class);
	       System.out.print(ToStringBuilder.reflectionToString(u));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jedis j=new Jedis();
		j.flushAll();
		j.set("hello", "world");
		print(1,j.get("hello"));
		j.rename("hello", "hellonew");
		print(2,j.get("hellonew"));
		j.setex("h2", 15, "2");//设置15S的暂时存储，过期就自动没了，可用于验证码
		j.set("pv", "100");
		j.incr("pv");//PV自增1
		print(3,j.get("pv"));
		j.incrBy("pv", 12);//PV自增12
		print(3,j.get("pv"));
		
		//列表操作
		String listName="list";
		for(int i=0;i<10;i++) j.lpush(listName, "a"+i);
		print(4,j.lrange(listName, 0, 12));
		print(4,j.llen(listName));
		print(4,j.lpop(listName));
		print(4,j.lindex(listName, 3));
		print(4,j.linsert(listName, BinaryClient.LIST_POSITION.AFTER, "a4", "af"));
		print(4,j.linsert(listName, BinaryClient.LIST_POSITION.BEFORE, "a4", "bf"));
		print(4,j.lrange(listName, 0, 12));
		
		//map操作
		String map="wanna";
		j.hset(map, "name", "wanna");
		j.hset(map, "age", "18");
		j.hset(map, "phone", "88888");
		print(5,j.hget(map, "name"));
		print(5,j.hgetAll(map));
		print(5,j.hdel(map, "age"));
		print(5,j.hgetAll(map));
		print(5,j.hkeys(map));
		print(5,j.hvals(map));
		print(5,j.hexists(map, "school"));
		print(5,j.hexists(map, "name"));
		j.hsetnx(map, "school", "qh");
		j.hsetnx(map, "name", "kobe");
		print(5,j.hgetAll(map));
		
		
		String set1="set1";
		String set2="set2";
		for(int i=0;i<10;i++) {
			j.sadd(set1, i+"");
			j.sadd(set2, i*2+"");
		}
		print(6,j.smembers(set1));
		print(6,j.smembers(set2));
		print(6,j.sinter(set1, set2));//交集
		print(6,j.sunion(set1, set2));//并集
		print(6,j.sdiff(set1,set2));//set1有，set2没有的集合
		print(6,j.sismember(set1, "1"));
		j.srem(set1, "1");
		print(6,j.smembers(set1));
		print(6,j.scard(set1));
		j.smove(set2, set1, "14");
		print(6,j.scard(set1));
		print(6,j.smembers(set1));
		
		//优先队列
		String pq="pq";
		j.zadd(pq, 91, "jim");
		j.zadd(pq, 93, "wanna");
		j.zadd(pq, 44, "kenam");
		j.zadd(pq, 75,"kobe");
		j.zadd(pq, 60, "messi");
		print(7,j.zcard(pq));
		print(7,j.zcount(pq, 61, 122));
		print(7,j.zscore(pq, "kobe"));
		j.zincrby(pq, 2, "kobe");
		print(7,j.zscore(pq, "kobe"));
		print(7,j.zrange(pq, 1, 3));//从小到大
		print(7,j.zrevrange(pq, 1, 3));//从大到小
		print(7,j.zrank(pq, "wanna"));//从小到大排第几
		print(7,j.zrevrank(pq, "wanna"));//从大到小排第几
		
		JedisPool pool=new JedisPool();
		/*for(int i=0;i<100;i++) {
			//redis是单线程的，可通过建立JedisPool像实现线程池那样，但同时最多只能提供8个，需要设置close
			Jedis je=pool.getResource();
			je.get("hello");
			System.out.println("pool"+i);
			je.close();
		}*/
		System.out.println(j.lpush("s", "w"));//返队列的长度
		System.out.println(j.lpush("s", "a"));
	}

}
