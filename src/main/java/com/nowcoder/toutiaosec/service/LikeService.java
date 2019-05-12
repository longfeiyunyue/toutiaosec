package com.nowcoder.toutiaosec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nowcoder.toutiaosec.util.JedisAdapter;
import com.nowcoder.toutiaosec.util.RedisKeyUtil;

@Service
public class LikeService {
    @Autowired
	private JedisAdapter jedisAdapter;
    
    public int getLikeStatus(int userId,int entityType,int entityId) {
    	//如果该用户喜欢这条资讯，返回1，不喜欢返-1，没表示返0
    	//用于标示前端用户喜欢则高亮点赞，不喜欢高亮踩，或者没表示
    	String likeSta=RedisKeyUtil.getLikeKey(entityId, entityType);
    	if(jedisAdapter.sismember(likeSta, String.valueOf(userId))) return 1;
    	
    	String disLikeSta=RedisKeyUtil.getDisLikeKey(entityId, entityType);
    	return jedisAdapter.sismember(disLikeSta, String.valueOf(userId))? -1:0;
    }
    
    public long like(int userId,int entityType,int entityId) {
    	//用户点赞某条资讯，先将用户加入该资讯点赞集合，并从踩集合中移除
    	String likeSta=RedisKeyUtil.getLikeKey(entityId, entityType);
    	jedisAdapter.sadd(likeSta, String.valueOf(userId));
    	
    	String disLikeSta=RedisKeyUtil.getDisLikeKey(entityId, entityType);
    	jedisAdapter.srem(disLikeSta, String .valueOf(userId));
    	
    	return jedisAdapter.scard(likeSta);//最后返回点赞集合后数量
    }
    
    public long disLike(int userId,int entityType,int entityId) {
    	//用户踩某条资讯，反过来
    	String disLikeSta=RedisKeyUtil.getDisLikeKey(entityId, entityType);
    	jedisAdapter.sadd(disLikeSta, String .valueOf(userId));
    	
    	String likeSta=RedisKeyUtil.getLikeKey(entityId, entityType);
    	jedisAdapter.srem(likeSta, String.valueOf(userId));
    	
    	return jedisAdapter.scard(likeSta);//最后返回点赞集合后数量
    }
}
