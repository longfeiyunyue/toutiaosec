package com.nowcoder.toutiaosec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.nowcoder.toutiaosec.domain.Comment;
@Mapper
public interface CommentDAO {
	 String TABLE_NAME = " comment ";
	 String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status";
	 String SELECT_FIELDS = " id, " + INSERT_FIELDS;
	 
	 @Insert({"Insert into ",TABLE_NAME,"(",INSERT_FIELDS,")values(#{userId},"
	 		+ "#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
	 int addComment(Comment comment);
	 
	 //根据entity_id, entity_type查询出某条新闻的所有评论，把最新显示在前边（降序排列）
	 @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where "
	 		+ "entity_id=#{entityId} and entity_type=#{entityType} order by id desc"})
	 List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);
	 
	 //获得某条新闻的所有评论数
	 @Select({"select count(id) from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType}"})
	 int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
