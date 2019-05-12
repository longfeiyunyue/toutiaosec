package com.nowcoder.toutiaosec.util;

public class RedisKeyUtil {//用于生成redis的key,避免重复

	private static final String SPLIT = ":";
    private static final String BIZ_LIKE = "LIKE";
    private static final String BIZ_DISLIKE = "DISLIKE";
    private static final String BIZ_EVENT = "EVENT";//处理异步时间的队列的key名称
    
    public static String getEventQueueKey() {
    	return BIZ_EVENT;
    }
    
    public static String getLikeKey(int entityId,int entityType) {
    	//为每个资讯生成自己的集合,各自管理,避免乱掉
    	//例如喜欢资讯1和资讯2所关联集合是不一样的
    	return BIZ_LIKE+SPLIT+entityType+SPLIT+entityId;
    }
    
    public static String getDisLikeKey(int entityId,int entityType) {
    	return BIZ_DISLIKE+SPLIT+entityType+SPLIT+entityId;
    }
}
