package com.nowcoder.toutiaosec.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.toutiaosec.dao.CommentDAO;
import com.nowcoder.toutiaosec.domain.Comment;

@Service
public class CommentService {
    @Autowired
	CommentDAO commentDAO;
	
	public int addComment(Comment comment) {
		return commentDAO.addComment(comment);
	}
	
	public List<Comment> getCommentsByEntity(int entityId,int entityType){
		return commentDAO.selectByEntity(entityId, entityType);
	}
	
	public int getCommentCount(int entityId,int entityType) {
		return commentDAO.getCommentCount(entityId, entityType);
	}
}
