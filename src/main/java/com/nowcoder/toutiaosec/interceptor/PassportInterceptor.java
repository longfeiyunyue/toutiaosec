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
public class PassportInterceptor implements HandlerInterceptor {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private LoginTicketDAO loginTicketDAO;
	@Autowired
	private HostHolder holder;

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
        
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView mv)
			throws Exception {//渲染之前提供的后处理方法，可以添加模型数据，自动传给前端。
		// TODO Auto-generated method stub
          if(mv!=null&&holder.getUser()!=null) {
        	  mv.addObject(holder.getUser());
        	  holder.clear();
          }
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) throws Exception {
		// TODO Auto-generated method stub
		//true继续请求,false拦截请求,此处拦截器的作用是保存用户的信息到本地线程，以便后续使用
		String ticket=null;
		if(req.getCookies()!=null) {
			for(Cookie cookie:req.getCookies()) {
				if(cookie.getName().equals("ticket")) {
					ticket=cookie.getValue();
					break;
				}
			}
			if(ticket!=null) {
				LoginTicket lt=loginTicketDAO.selectByTicket(ticket);
				if(lt==null||lt.getExpired().before(new Date())||lt.getStatus()!=0)
					return true;//如果用户ticket不存在或已过期或不在登录态，不需要要往下走记录当前用户的登录信息
				User user=userDAO.selectById(lt.getUserId());
				holder.setUser(user);//记录当前用户的登录信息，以便后面controller调用，
				//在后续调用中可直接获取本地线程的用户，并根据用户特性展示不同的浏览页面
				return true;
			}
		}
		return true;
	}

}
