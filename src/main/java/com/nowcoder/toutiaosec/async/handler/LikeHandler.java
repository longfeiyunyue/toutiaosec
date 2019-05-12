package com.nowcoder.toutiaosec.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nowcoder.toutiaosec.async.EventHandler;
import com.nowcoder.toutiaosec.async.EventModel;
import com.nowcoder.toutiaosec.async.EventType;
import com.nowcoder.toutiaosec.domain.Message;
import com.nowcoder.toutiaosec.domain.User;
import com.nowcoder.toutiaosec.service.MessageService;
import com.nowcoder.toutiaosec.service.UserService;
@Component
public class LikeHandler implements EventHandler {
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;

	@Override
	public void doHandle(EventModel eventModel) {
		// TODO Auto-generated method stub
        int userId=eventModel.getActorId();
        User user=userService.getUser(userId);
        Message message=new Message();
        message.setFromId(3);//可以理解为管理员通过站内信向当前用户发送的点赞信息
        message.setToId(eventModel.getEntityOwnerId());
        //message.setToId(eventModel.getActorId());
        message.setContent("用户" + user.getName() + "赞了你的资讯"
                + ",http://127.0.0.1:8088/news/" + eventModel.getEntityId());
        message.setCreatedDate(new Date());
        messageService.addMessage(message);//列举了点赞行为的其中一种产生效应，发送站内信
	}

	@Override
	public List<EventType> getSupportsEventTypes() {
		// TODO Auto-generated method stub
		return Arrays.asList(EventType.LIKE);
	}

}
