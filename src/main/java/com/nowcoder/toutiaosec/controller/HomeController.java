package com.nowcoder.toutiaosec.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.nowcoder.toutiaosec.domain.EntityType;
import com.nowcoder.toutiaosec.domain.HostHolder;
import com.nowcoder.toutiaosec.domain.News;
import com.nowcoder.toutiaosec.domain.ViewObject;
import com.nowcoder.toutiaosec.service.LikeService;
import com.nowcoder.toutiaosec.service.NewsService;
import com.nowcoder.toutiaosec.service.UserService;

@Controller
public class HomeController {//首页展示

	@Autowired
	UserService userService;
	@Autowired
	NewsService newsService;
	@Autowired
	LikeService likeService;
	@Autowired
	HostHolder holder;
	
	private List<ViewObject> getNews(int userId,int offset,int limit){
		List<News> newsList=newsService.getLatestNews(userId, offset, limit);
		int localUserId=holder.getUser()!=null?holder.getUser().getId():0;
		List<ViewObject> vos=new ArrayList<ViewObject>();
		for(News news:newsList) {
			ViewObject vo=new ViewObject();
			vo.set("news", news);
			vo.set("user", userService.getUser(news.getUserId()));
			//返回当前用户对于该条资讯的点赞状态
			if(localUserId!=0) vo.set("like", 
					likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
			else vo.set("like", 0);
			vos.add(vo);
		}
		return vos;
	}
	
	@RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(0, 0, 10));
        model.addAttribute("pop", pop);
        return "home";
    }
	
	@RequestMapping(value="/user/{userId}",method= {RequestMethod.GET,RequestMethod.POST})
	public String userIndex(Model model,@PathVariable("userId") int userId) {
		model.addAttribute("vos", getNews(userId,0,10));
		return "home";
	}
}
