package com.nowcoder.toutiaosec.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.toutiaosec.domain.User;

@Mapper
public interface UserDAO {
	String TABLE_NAME="user";
	String INSERT_FIELDS=" name, password, salt, head_url ";
    String SELECT_FIELDS=" id, name, password, salt, head_url";
    @Insert({"INSERT INTO", TABLE_NAME,"(",INSERT_FIELDS,")" + 
    		"VALUES(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);
    
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
    User selectById(int id);
    
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where name=#{name}"})
    User selectByName(String name);
    
    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePasword(User user);
    
    @Delete({"delete from ",TABLE_NAME," where id=#{id}"})
    void deleteById(int id);
}
