package com.nowcoder.toutiaosec.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {//异步触发事件的信息保存,单向/优先队列队列里的内容
	
	private EventType eventType;//异步事件类型，有枚举
	private int actorId;//触发者
	
	private int entityId;//触发对象
	private int entityType;
	
    private int entityOwnerId;//触发对象拥有者，拥有者会收到通知
    //触发事件的其它参数或数据，触发当下有什么数据要保存下来，以后要用,用map保存，map真万能
    private Map<String,String> exts=new HashMap<>();
    
    
    
    public EventModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventModel(EventType eventType) {
		super();
		this.eventType = eventType;
	}

	public EventModel set(String key,String value) {
    	//使用return this可以让代码执行链路化操作，可以直接set set set
        //后面的set都改用这种方式
    	exts.put(key, value);
    	return this;
    }
    
    public String get(String key) {
    	return exts.get(key);
    }
    
	public EventType getEventType() {
		return eventType;
	}
	public EventModel setEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}
	public int getActorId() {
		return actorId;
	}
	public EventModel setActorId(int actorId) {
		this.actorId = actorId;
		return this;
	}
	public int getEntityId() {
		return entityId;
	}
	public EventModel setEntityId(int entityId) {
		this.entityId = entityId;
		return this;
	}
	public int getEntityType() {
		return entityType;
	}
	public EventModel setEntityType(int entityType) {
		this.entityType = entityType;
		return this;
	}
	public int getEntityOwnerId() {
		return entityOwnerId;
	}
	public EventModel setEntityOwnerId(int entityOwnerId) {
		this.entityOwnerId = entityOwnerId;
		return this;
	}
	public Map<String, String> getExts() {
		return exts;
	}
	public void setExts(Map<String, String> exts) {
		this.exts = exts;
	}
    
    
}
