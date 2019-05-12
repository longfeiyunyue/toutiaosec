package com.nowcoder.toutiaosec.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nowcoder.toutiaosec.async.EventModel;
import com.nowcoder.toutiaosec.async.EventProducer;
import com.nowcoder.toutiaosec.async.EventType;
import com.nowcoder.toutiaosec.domain.EntityType;
import com.nowcoder.toutiaosec.domain.HostHolder;
import com.nowcoder.toutiaosec.domain.News;
import com.nowcoder.toutiaosec.service.LikeService;
import com.nowcoder.toutiaosec.service.NewsService;
import com.nowcoder.toutiaosec.util.ToutiaoUtil;

@Controller
public class LikeController {
	@Autowired
	private HostHolder holder;
	@Autowired
	private LikeService likeService;
	@Autowired
	private NewsService newsService;
	@Autowired
	private EventProducer eventProducer;
	
	@RequestMapping(value="/like",method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String like(@Param("newsId") int newsId) {
		int userId=holder.getUser().getId();
		long likeCount=likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
		newsService.updateLikeCount(newsId,(int)likeCount);//更新点赞数
		News news=newsService.getById(newsId);
		eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(userId)
				.setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS)
				.setEntityOwnerId(news.getUserId()));
		return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
	}
	
	@RequestMapping(value="/dislike",method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String disLike(@Param("newsId") int newsId) {
		int userId=holder.getUser().getId();
		long likeCount=likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
		newsService.updateLikeCount(newsId,(int)likeCount);
		return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
	}
}
