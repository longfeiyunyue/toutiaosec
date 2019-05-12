package com.nowcoder.toutiaosec.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.nowcoder.toutiaosec.util.JedisAdapter;
import com.nowcoder.toutiaosec.util.RedisKeyUtil;


@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	
	@Autowired
	JedisAdapter jedisAdapter;
	
	private ApplicationContext applicationContext;//继承ApplicationContextAware类，用于获取所有注入的handler
    //事件要分发给指定handler处理，要一个映射表，映射处理事件的所有触发handler
	private Map<EventType,List<EventHandler>> config=new HashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		//获取EventHandler的所有实现类
		 Map<String, EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
		 if(beans!=null) {//遍历实现类，反向填充事件类型->执行handler
			 for(Map.Entry<String, EventHandler> entry:beans.entrySet()) {
				 List<EventType> eventTypes=entry.getValue().getSupportsEventTypes();
				 for(EventType eventType:eventTypes) {
					 if(!config.containsKey(eventType)) {
						 config.put(eventType, new ArrayList<EventHandler>());
					 }
					 config.get(eventType).add(entry.getValue());
				 }
			 }
		 }
		 
		Thread t= new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {//在多线程内持续监听队列等待处理事件
					String key=RedisKeyUtil.getEventQueueKey();
					List<String> events=jedisAdapter.brpop(0, key);//一直阻塞直到有事件
					for(String event:events) {
						if(event.equals(key)) continue;//redis自带消息key要过滤掉
						EventModel em=JSONObject.parseObject(event, EventModel.class);
						if(!config.containsKey(em.getEventType())) {
							logger.error("不能识别的事件");
							continue;
						}
						//有了map就知道事件由哪些handler来处理
						for(EventHandler handler:config.get(em.getEventType())) {
							handler.doHandle(em);
						}
					}
				}
			}
			 
		 });
		 t.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext=applicationContext;
	}

}
