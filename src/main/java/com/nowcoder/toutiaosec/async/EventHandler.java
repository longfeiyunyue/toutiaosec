package com.nowcoder.toutiaosec.async;

import java.util.List;

import org.springframework.stereotype.Component;
@Component
public interface EventHandler {//标示处理异步事件的接口
    //处理事件的方法，不同实现类处理方式不同
	void doHandle(EventModel eventModel);
	//关注某些类型，只要是这些类型就由我这个handler处理
	List<EventType> getSupportsEventTypes();
}
