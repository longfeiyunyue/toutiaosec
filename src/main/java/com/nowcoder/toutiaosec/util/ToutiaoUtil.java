package com.nowcoder.toutiaosec.util;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;


public class ToutiaoUtil {
    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);
    public static final String IMAGE_DIR = "D:/upload/";
    public static final String[] IMAGE_FILE_EXT = {"png", "jpg", "jpeg", "bmp"};
    public static String TOUTIAO_DOMAIN = "http://127.0.0.1:8080/";
    public static String QINIU_DOMAIN_PREFIX="http://7xsetu.com1.z0.glb.clouddn.com/";
    
    public static String getJSONString(int code) {
    	JSONObject json=new JSONObject();
    	json.put("code", code);
    	return json.toJSONString();
    }

    public static String getJSONString(int code,String msg) {
    	JSONObject json=new JSONObject();
    	json.put("code", code);
    	json.put("msg", msg);
    	return json.toJSONString();
    	}
    
    public static String getJSONString(int code,Map<String,Object> map) {
    	JSONObject json = new JSONObject();
        json.put("code", code);
        for(Map.Entry<String, Object> set:map.entrySet()) {
        	json.put(set.getKey(), set.getValue());
        }
        return json.toJSONString();
    }

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

    public static boolean isFileAllowed(String fileExt) {
        for (String ext : IMAGE_FILE_EXT) {
            if (fileExt.equals(ext)) {
                return true;
            }
        }
        return false;
    }
}
