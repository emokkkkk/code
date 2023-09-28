package com.softeem.mvct.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class HistoryTicket {

    private String username;
    private String filmname;
    private LocalDateTime starttm;
    private LocalDateTime endtm;
    private String ticketid;
    private BigDecimal money;
    private String seat;
    private LocalDateTime crtm;
    private Integer state;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilmname() {
        return filmname;
    }

    public void setFilmname(String filmname) {
        this.filmname = filmname;
    }

    public LocalDateTime getStarttm() {
        return starttm;
    }

    public void setStarttm(LocalDateTime starttm) {
        this.starttm = starttm;
    }

    public LocalDateTime getEndtm() {
        return endtm;
    }

    public void setEndtm(LocalDateTime endtm) {
        this.endtm = endtm;
    }

    public LocalDateTime getCrtm() {
        return crtm;
    }

    public void setCrtm(LocalDateTime crtm) {
        this.crtm = crtm;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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

    public String getTicketid() {
        return ticketid;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }
}
