package com.nowcoder.toutiaosec;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nowcoder.toutiaosec.dao.CommentDAO;
import com.nowcoder.toutiaosec.dao.LoginTicketDAO;
import com.nowcoder.toutiaosec.dao.NewsDAO;
import com.nowcoder.toutiaosec.dao.UserDAO;
import com.nowcoder.toutiaosec.domain.Comment;
import com.nowcoder.toutiaosec.domain.EntityType;
import com.nowcoder.toutiaosec.domain.LoginTicket;
import com.nowcoder.toutiaosec.domain.News;
import com.nowcoder.toutiaosec.domain.User;

import junit.framework.Assert;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaosecApplication.class)
@Sql("/toutiao.sql")
public class InitDatabaseTests {
    @Autowired
	UserDAO userDAO;
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    CommentDAO commentDAO;
    @Test
    public void contextLoads() {
    	Random random=new Random();
    	for(int i=0;i<=10;i++) {
    		User user=new User();
    		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
    		user.setName(String.format("USER%d",i));
    		user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            
            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);
            
            
            for(int j=0;j<3;j++) {
            	Comment c=new Comment();
            	c.setUserId(i+1);
            	c.setCreatedDate(new Date());
            	c.setStatus(0);
            	c.setEntityType(EntityType.ENTITY_COMMENT);
            	c.setContent("评论："+String.valueOf(i));
            	c.setEntityId(news.getId());
            	commentDAO.addComment(c);
            }
            
            user.setPassword("11111111");
            userDAO.updatePasword(user);
            
            LoginTicket ticket=new LoginTicket();
            ticket.setUserId(i+1);
            ticket.setExpired(date);
            ticket.setStatus(0);
            ticket.setTicket(String.format("ticket%d", i+1));
            loginTicketDAO.addTicket(ticket);
            
            loginTicketDAO.updateStatus(ticket.getTicket(), 2);
    	}
    	Assert.assertEquals("11111111", userDAO.selectById(1).getPassword());
    	userDAO.deleteById(1);
    	System.out.println("------"+loginTicketDAO.selectByTicket("ticket1").getStatus());
    	Assert.assertEquals(loginTicketDAO.selectByTicket("ticket1").getStatus(), 2);
    	Assert.assertEquals(loginTicketDAO.selectByTicket("ticket1").getUserId(), 1);
    	Assert.assertNotNull(commentDAO.selectByEntity(1, EntityType.ENTITY_COMMENT).get(0));
    }
}
