package com.nowcoder.toutiaosec.domain;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {//保存当前访问的用户自己的信息，通过拦截器注入
    //线程本地存储，用户存储自己的信息
	private static ThreadLocal<User> users=new ThreadLocal<User>();
	
	public User getUser() {
		return users.get();
	}
	
	public void setUser(User user) {
		users.set(user);
	}
	
	public void clear() {
		users.remove();
	}
}
