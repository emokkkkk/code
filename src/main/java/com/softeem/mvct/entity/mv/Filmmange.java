package com.softeem.mvct.entity.mv;

import com.softeem.mvct.utils.MvcUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Filmmange {

    private Integer id;
    private Integer filmid;
    private String filmname;
    private Integer room;
    private LocalDateTime starttm;
    private LocalDateTime endtm;
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFilmid() {
        return filmid;
    }

    public void setFilmid(Integer filmid) {
        this.filmid = filmid;
    }

    public String getFilmname() {
        return filmname;
    }

    public void setFilmname(String filmname) {
        this.filmname = filmname;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id+"\t"+filmname+"\t"+room+"\t"+ MvcUtils.getTmToString(starttm,"MM-dd HH:mm")+"-"+MvcUtils.getTmToString(endtm,"MM-dd HH:mm")+"\t"+price;
    }
}
