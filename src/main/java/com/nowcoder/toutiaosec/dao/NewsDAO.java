package com.nowcoder.toutiaosec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.toutiaosec.domain.News;
@Mapper
public interface NewsDAO {
	String TABLE_NAME="news";
	String INSERT_FIELDS=" title, link, image, like_count, comment_count, created_date, user_id ";
	String SELECT_FIELDS="id,"+INSERT_FIELDS;
	@Insert({"insert into ",TABLE_NAME," (",INSERT_FIELDS,") values("
    		+ "#{title},#{link},#{image},#{likeCount},#{commentCount},"
    		+ "#{createdDate},#{userId})"})
    int addNews(News news);
	
	List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset") 
	int offset,@Param("limit") int limit);
	
	@Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
	News getById(@Param("id") int id);
	
	@Update({"update ",TABLE_NAME," set comment_count=#{commentCount} where id=#{id}"})
	int updateCommentCount(@Param("id") int id,@Param("commentCount") int commentCot);
	
	@Update({"Update ",TABLE_NAME," set like_count=#{likeCount} where id=#{id}"})
	int updateLikeCount(@Param("id") int id,@Param("likeCount") int likeCot);
}
