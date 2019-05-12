package com.nowcoder.toutiaosec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.toutiaosec.dao.MessageDAO;
import com.nowcoder.toutiaosec.domain.Message;

@Service
public class MessageService {
    @Autowired
	private MessageDAO messageDAO;
	
	public int addMessage(Message message) {
		if(message.getConversationId()==null) {
			message.setConversationId(message.getFromId()+"_"+message.getToId());
		}
		return messageDAO.addMessage(message);
	}
	
	public List<Message> getConversationDetail(String conversationId, int offset, int limit){
		return messageDAO.getConversationDetail(conversationId, offset, limit);
	}
	
	public List<Message> getConversationList(int userId,int offset,int limit){
		return messageDAO.getConversationList(userId, offset, limit);
	}
	
	public int getUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnReadCount(userId, conversationId);
    }
}
