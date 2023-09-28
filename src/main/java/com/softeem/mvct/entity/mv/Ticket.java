package com.softeem.mvct.entity.mv;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class Ticket {

    private Integer id;
    private String ticketid;
    private Integer userid;
    private BigDecimal money;
    private Integer filmmangeid;
    private String seat;
    private LocalDateTime crtm;
    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getFilmmangeid() {
        return filmmangeid;
    }

    public void setFilmmangeid(Integer filmmangeid) {
        this.filmmangeid = filmmangeid;
    }

    public LocalDateTime getCrtm() {
        return crtm;
    }

    public void setCrtm(LocalDateTime crtm) {
        this.crtm = crtm;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


}
