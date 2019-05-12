package com.nowcoder.toutiaosec.async.handler;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nowcoder.toutiaosec.async.EventHandler;
import com.nowcoder.toutiaosec.async.EventModel;
import com.nowcoder.toutiaosec.async.EventType;
import com.nowcoder.toutiaosec.domain.Message;
import com.nowcoder.toutiaosec.service.MessageService;
import com.nowcoder.toutiaosec.util.MailSender;
@Component
public class LoginExceptionHandler implements EventHandler {
	@Autowired
	private MessageService messageService;
	@Autowired
	MailSender mailSender;

	@Override
	public void doHandle(EventModel eventModel) {
		// TODO Auto-generated method stub
        Message message=new Message();
        message.setToId(eventModel.getActorId());//通过站内信向当前用户发送登录异常信息，谁登录就发给谁
        message.setFromId(3);//可以理解为管理员ID
        message.setContent("你上次登录的ip异常");
        message.setCreatedDate(new Date());
        messageService.addMessage(message);
        //邮件发送通知
        Map<String, Object> map = new HashMap<>();
        map.put("username", eventModel.get("username"));
        mailSender.sendWithHTMLTemplate(eventModel.get("email"),"登录异常","mails/welcome.html", map);
	}

	@Override
	public List<EventType> getSupportsEventTypes() {
		// TODO Auto-generated method stub
		return Arrays.asList(EventType.LOGIN);
	}

}
