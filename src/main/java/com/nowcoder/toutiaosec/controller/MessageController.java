package com.nowcoder.toutiaosec.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.toutiaosec.domain.HostHolder;
import com.nowcoder.toutiaosec.domain.Message;
import com.nowcoder.toutiaosec.domain.User;
import com.nowcoder.toutiaosec.domain.ViewObject;
import com.nowcoder.toutiaosec.service.MessageService;
import com.nowcoder.toutiaosec.service.UserService;
import com.nowcoder.toutiaosec.util.ToutiaoUtil;



@Controller
public class MessageController {//消息对话
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
	@Autowired
	private HostHolder holder;
	
	@RequestMapping(value="/msg/detail",method= {RequestMethod.GET})
	public String conversationDetail(Model model,@RequestParam("conversationId") String conversationId) {
		try {
			List<Message> messageList=messageService.getConversationDetail(conversationId, 0, 10);
			List<ViewObject> messages=new ArrayList<ViewObject>();
			for(Message message:messageList) {
				ViewObject vo=new ViewObject();
				vo.set("message", message);
				User user=userService.getUser(message.getFromId());
				if(user==null) continue;
				vo.set("headUrl", user.getHeadUrl());
				vo.set("userName", user.getName());
				messages.add(vo);
			}
			model.addAttribute("messages", messages);
		}catch(Exception e) {
			logger.error("获取站内信列表失败:"+e.getMessage());
		}
		return "letterDetail";
	}
	
	@RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = holder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> messageList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", msg.getId());
                vo.set("unreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }
	
	@RequestMapping(value="/msg/addMessage",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String addMessage(@RequestParam("fromId") int fromId,
            @RequestParam("toId") int toId,
            @RequestParam("content") String content) {
		Message msg=new Message();
		msg.setContent(content);
		msg.setFromId(fromId);
		msg.setToId(toId);
		msg.setCreatedDate(new Date());
		msg.setConversationId(String.format("%d_%d", fromId,toId));
		if(toId<fromId) msg.setConversationId(String.format("%d_%d", toId,fromId));
		messageService.addMessage(msg);
		return ToutiaoUtil.getJSONString(msg.getId());
	}
	
}
