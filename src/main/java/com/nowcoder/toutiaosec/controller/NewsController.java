package com.nowcoder.toutiaosec.controller;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nowcoder.toutiaosec.domain.Comment;
import com.nowcoder.toutiaosec.domain.EntityType;
import com.nowcoder.toutiaosec.domain.HostHolder;
import com.nowcoder.toutiaosec.domain.News;
import com.nowcoder.toutiaosec.domain.ViewObject;
import com.nowcoder.toutiaosec.service.AliService;
import com.nowcoder.toutiaosec.service.CommentService;
import com.nowcoder.toutiaosec.service.LikeService;
import com.nowcoder.toutiaosec.service.NewsService;
import com.nowcoder.toutiaosec.service.QiniuService;
import com.nowcoder.toutiaosec.service.UserService;
import com.nowcoder.toutiaosec.util.ToutiaoUtil;

@Controller
public class NewsController {//添加新闻、添加新闻评论、查看新闻
	
	private static final Logger logger =LoggerFactory.getLogger(NewsController.class);
	@Autowired
	private NewsService newsService;
	@Autowired
    private QiniuService qiniuService;	
	@Autowired
    private AliService aliService;
	@Autowired
	private HostHolder holder;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private LikeService likeService;
	
	@RequestMapping(value="/uploadImage/")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile image) {
		try {
			String fileUrl=aliService.saveImage(image);
			if(fileUrl==null) return ToutiaoUtil.getJSONString(1, "上传图片失败");
			else return ToutiaoUtil.getJSONString(0, fileUrl);
		}catch(Exception e) {
			logger.error("上传图片异常:"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "上传图片失败");
		}
	}
			
	
	@RequestMapping(value="/image/",method= {RequestMethod.GET})
	@ResponseBody
	public void getImage(@RequestParam("name") String imageName,HttpServletResponse response) {
		try {
			response.setContentType("image/jpg");
			StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR+
					imageName)), response.getOutputStream());
		}catch(Exception e) {
			logger.error("读取图片失败："+imageName+e.getMessage());
		}
	}
	
	@RequestMapping(value="/user/addNews/",method= {RequestMethod.POST})
	@ResponseBody
	public String addNews(@RequestParam("image") String image,
			@RequestParam("title") String title,
			@RequestParam("link") String link) {
		try {
			News news=new News();
			news.setCreatedDate(new Date());
			news.setImage(image);
			news.setTitle(title);
			news.setLink(link);
			if(holder.getUser()!=null) {
				news.setUserId(holder.getUser().getId());
			}else {//如果是未登录用户，分配为匿名用户，即3
				news.setUserId(3);
			}
			newsService.addNews(news);
			return ToutiaoUtil.getJSONString(0);
		}catch(Exception e) {
			logger.error("添加资讯失败："+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "发布失败！");
		}
	}
	
	@RequestMapping(value="/news/{newsId}",method= {RequestMethod.GET})
	public String newsDatail(@PathVariable("newsId") int newsId,Model model) {
		News news=newsService.getById(newsId);
		if(news!=null) {
			int localUserId=holder.getUser()!=null?holder.getUser().getId():0;
			if(localUserId!=0) model.addAttribute("like", 
					likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
			else model.addAttribute("like", 0);
			//获得该条新闻所有的评论
			List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
			for(Comment comment:comments) {
				ViewObject vo=new ViewObject();//其实就是个map
				//一个评论信息实体分两大块：1.评论内容  2.哪个用户的评论
				//评论实体按块存入键值对集合，返前端渲染展示
				 vo.set("comment", comment);
	             vo.set("user", userService.getUser(comment.getUserId()));
				commentVOs.add(vo);
			}
			model.addAttribute("comments", commentVOs);
		}
		model.addAttribute("news", news);
		model.addAttribute("owner", userService.getUser(news.getUserId()));
		return "detail";
	}
	
	@RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
	public String addComment(@RequestParam("newsId") int newsId,
            @RequestParam("content") String content) {
		try {
			Comment comment=new Comment();
			comment.setContent(content);
			comment.setUserId(holder.getUser().getId());
			comment.setCreatedDate(new Date());
			comment.setEntityId(newsId);
			comment.setEntityType(EntityType.ENTITY_NEWS);
			comment.setStatus(0);
			commentService.addComment(comment);
			
			int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
			newsService.updateCommentCount(comment.getEntityId(), count);
		}catch(Exception e) {
			logger.error("提交评论错误" + e.getMessage());
		}
		return "redirect:/news/"+String.valueOf(newsId);
	}
	
	
	/*@RequestMapping(value="/uploadImage/")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile image) {
		try {
			String fileUrl=qiniuService.saveImage(image);
			if(fileUrl==null) return ToutiaoUtil.getJSONString(1, "上传图片失败");
			else return ToutiaoUtil.getJSONString(0, fileUrl);
		}catch(Exception e) {
			logger.error("上传图片异常:"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "上传图片失败");
		}
	}*/
	
	/*@RequestMapping(value="/uploadImage/")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile image) {
		try {
			String fileUrl=newsService.saveImage(image);
			if(fileUrl==null) return ToutiaoUtil.getJSONString(1, "上传图片失败");
			else return ToutiaoUtil.getJSONString(0, fileUrl);
		}catch(Exception e) {
			logger.error("上传图片异常:"+e.getMessage());
			return ToutiaoUtil.getJSONString(1, "上传图片失败");
		}
	}*/
}
