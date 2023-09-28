package com.softeem.mvct.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserDesVo {
    private int id;
    private String username;
    private Integer level;
    private BigDecimal total;
    private Long con;
    private String phone;
    private String email;
    private LocalDateTime crtm;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getCon() {
        return con;
    }

    public void setCon(Long con) {
        this.con = con;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCrtm() {
        return crtm;
    }

    public void setCrtm(LocalDateTime crtm) {
        this.crtm = crtm;
    }

    @Override
    public String toString() {


        return id+"\t"+username+"\t"+getLv(level)+"\t"+crtm+"\t"+total+"\t"+con+"\t"+phone+"\t"+email;
    }

    private String getLv(int level){
        if(1==level){
            return "金牌";
        }else if(2==level){
            return "银牌";
        }
        return "铜牌";
    }
}
