package com.nowcoder.toutiaosec.controller;


import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.toutiaosec.async.EventModel;
import com.nowcoder.toutiaosec.async.EventProducer;
import com.nowcoder.toutiaosec.async.EventType;
import com.nowcoder.toutiaosec.service.NewsService;
import com.nowcoder.toutiaosec.service.UserService;
import com.nowcoder.toutiaosec.util.ToutiaoUtil;

@Controller
public class LoginController {//登录、注册、登出
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private EventProducer eventProducer;
	
	@RequestMapping(value="/reg/",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String reg(Model model,@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam(value="rember",defaultValue="0")int rememberme,
			HttpServletResponse response) {
		try {//控制层要做好异常处理
			Map<String,Object> map=userService.register(username, password);
			//自己设置状态码返回前端，0即正常，1即错误
			if(map.containsKey("ticket")) {
				Cookie cookie=new Cookie("ticket", map.get("ticket").toString());
				cookie.setPath("/");
				if(rememberme>0) cookie.setMaxAge(5*24*3600);
				response.addCookie(cookie);
				return ToutiaoUtil.getJSONString(0, "注册成功");
			}
			else return ToutiaoUtil.getJSONString(1, map);
		}catch(Exception e) {
			logger.error("注册异常：",e.getMessage());
			return ToutiaoUtil.getJSONString(1, "注册异常");
		}
	}
	
	@RequestMapping(value="/login/",method= {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String login(Model model,@RequestParam("username")String username,
			@RequestParam("password")String password,
			@RequestParam(value="rember",defaultValue="0")int rememberme,
			HttpServletResponse response) {
		try {
			Map<String, Object> map=userService.login(username, password);	
			if(map.containsKey("ticket")) {
				Cookie cookie=new Cookie("ticket", map.get("ticket").toString());
				if(rememberme>0) cookie.setMaxAge(5*24*3600);
				cookie.setPath("/");
				response.addCookie(cookie);
				eventProducer.fireEvent(new EventModel(EventType.LOGIN)
						.set("username", "username").set("email", "wanna@qq.com"));//触发异常登录信息判断
				return ToutiaoUtil.getJSONString(0, "登录成功");			
			}else return ToutiaoUtil.getJSONString(1, map);
		}catch(Exception e) {
			logger.error("登录异常:"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "登录异常");
		}		
	}
	
	@RequestMapping(value="/logout/",method= {RequestMethod.GET,RequestMethod.POST})
	public String logout(@CookieValue("ticket") String ticket) {
		userService.logout(ticket);
		return "redirect:/";
	}
}
