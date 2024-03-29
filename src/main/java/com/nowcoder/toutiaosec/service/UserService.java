package com.nowcoder.toutiaosec.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.toutiaosec.dao.LoginTicketDAO;
import com.nowcoder.toutiaosec.dao.UserDAO;
import com.nowcoder.toutiaosec.domain.LoginTicket;
import com.nowcoder.toutiaosec.domain.User;
import com.nowcoder.toutiaosec.util.ToutiaoUtil;

@Service
public class UserService {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private LoginTicketDAO loginTicketDAO;

	public User selectById(int id) {
	     return userDAO.selectById(id);	
	}
	
	public User getUser(int id) {
		return userDAO.selectById(id);
	}
	
	public Map<String,Object> register(String username,String password) {
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空!");
			return map;
		}			

		if(StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空!");
			return map;
		}
			
		User user=userDAO.selectByName(username);
		if(user!=null) {
			map.put("msg", "用户名已被注册!");
			return map;
		}
		user=new User();
		user.setName(username);
		user.setHeadUrl(String.format(
				"http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
		user.setSalt(UUID.randomUUID().toString().substring(0, 5));
		user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
		userDAO.addUser(user);
		//登录
		//传入ticket，也就是登录成功
		String ticket=addLoginTicket(user.getId());
		map.put("ticket", ticket);
		return map;

	}
	
	public Map<String,Object> login(String username,String password){
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空!");
			return map;
		}
		if(StringUtils.isBlank(password)) {
			map.put("msg",  "密码不能为空!");
			return map;
		}
		User user=userDAO.selectByName(username);
		if(user==null) {
			map.put("msg", "用户名不存在!");
			return map;
		}
		if(!user.getPassword().equals(ToutiaoUtil.MD5(password+user.getSalt()))) {
			map.put("msg", "密码不正确!");
			return map;
		}
		//传入ticket，也就是登录成功
		String ticket=addLoginTicket(user.getId());
		map.put("ticket", ticket);
		return map;
	}
	
	private String addLoginTicket(int userId) {
		LoginTicket lt=new LoginTicket();
		lt.setUserId(userId);
		Date date=new Date();
		date.setTime(date.getTime()+24*3600*1000);
		lt.setExpired(date);
		lt.setStatus(0);
		lt.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		loginTicketDAO.addTicket(lt);
		return lt.getTicket();
	}
	
	
	public void logout(String ticket) {
		loginTicketDAO.updateStatus(ticket, 1);
	}
}
