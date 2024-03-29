package com.nowcoder.toutiaosec.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nowcoder.toutiaosec.dao.NewsDAO;
import com.nowcoder.toutiaosec.domain.News;
import com.nowcoder.toutiaosec.util.ToutiaoUtil;

@Service
public class NewsService {

	@Autowired
	private NewsDAO newsDAO;
	
	public List<News> getLatestNews(int userId,int offset,int limit){
		return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
	}
	
	public int addNews(News news) {
		newsDAO.addNews(news);
		return news.getId();
	}
	
	public News getById(int newsId) {
		return newsDAO.getById(newsId);
	}
	
	public int updateCommentCount(int id,int count) {
		return newsDAO.updateCommentCount(id, count);
	}
	
	public int updateLikeCount(int id,int count) {
		return newsDAO.updateLikeCount(id, count);
	}
	
	//上传图片
	public String saveImage(MultipartFile file) throws IOException{
		int dotPos=file.getOriginalFilename().lastIndexOf('.');
		if(dotPos<0) return null;
		String fileExt=file.getOriginalFilename().substring(dotPos+1).toLowerCase();
		if(!ToutiaoUtil.isFileAllowed(fileExt)) return null;
		String newName=UUID.randomUUID().toString().replaceAll("-", "")+"."+fileExt;
		Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR+newName).toPath(), 
				StandardCopyOption.REPLACE_EXISTING);
		return ToutiaoUtil.TOUTIAO_DOMAIN+"image?name="+newName;
	}
}
