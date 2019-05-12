package com.nowcoder.toutiaosec.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.toutiaosec.util.JedisAdapter;
import com.nowcoder.toutiaosec.util.RedisKeyUtil;


@Service
public class EventProducer {//事件生产者，负责把事件放进消息队列
	private static final Logger logger = LoggerFactory.getLogger(EventProducer.class);
    @Autowired
	private JedisAdapter jedisAdapter;
	
	public boolean fireEvent(EventModel em) {//事件接过来并放进队列
		try {
			String eventModel=JSONObject.toJSONString(em);//把事件序列化成字符串
			String list=RedisKeyUtil.getEventQueueKey();//存入队列
			jedisAdapter.lpush(list, eventModel);
			return true;
		}catch(Exception e) {
			logger.error("事件放入队列失败："+e.getMessage());
			return false;
		}
	}
}
