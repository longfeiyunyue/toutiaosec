package com.nowcoder.toutiaosec.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nowcoder.toutiaosec.domain.LoginTicket;
@Mapper
public interface LoginTicketDAO {

	String TABLE_NAME = " login_ticket ";
    String INSET_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id, user_id, ticket, expired, status ";
    
    @Insert({"insert into ",TABLE_NAME,"(",INSET_FIELDS,")values(#{userId},"
    		+ "#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket ticket);
    
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);
    
    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket,@Param("status") int status);
}
