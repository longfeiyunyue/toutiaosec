package com.nowcoder.toutiaosec.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.nowcoder.toutiaosec.interceptor.LoginRequiredInterceptor;
import com.nowcoder.toutiaosec.interceptor.PassportInterceptor;


@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
    PassportInterceptor passportInterceptor;
	@Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(passportInterceptor);//先注册先调用
		//这个拦截器可以让没有登陆的用户无法访问某些页面、
        //通过url匹配指定拦截的页面。
        //首先要让前一个拦截器来判断用户的状态，然后根据用户状态执行后续的拦截器
		registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
		super.addInterceptors(registry);
	}
	
}
