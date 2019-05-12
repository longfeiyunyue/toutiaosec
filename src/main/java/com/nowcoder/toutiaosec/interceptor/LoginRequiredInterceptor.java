package com.nowcoder.toutiaosec.interceptor;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.nowcoder.toutiaosec.dao.LoginTicketDAO;
import com.nowcoder.toutiaosec.dao.UserDAO;
import com.nowcoder.toutiaosec.domain.HostHolder;
import com.nowcoder.toutiaosec.domain.LoginTicket;
import com.nowcoder.toutiaosec.domain.User;
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
	
	@Autowired
	private HostHolder holder;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse res, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
        
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView mv)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {
		// TODO Auto-generated method stub
		if(holder.getUser()==null) {
			res.sendRedirect("/?pop=1");
			return false;
		}
		return true;
	}

}
