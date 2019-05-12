package com.nowcoder.toutiaosec.domain;

import java.util.Date;

public class LoginTicket {//存储用户登录信息
    private int id;
    private int userId;
    private Date expired;//登录用户的过期时间
    private int status;// 0有效登录，1无效登录，默认不删除数据，设置为1即为注销
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
