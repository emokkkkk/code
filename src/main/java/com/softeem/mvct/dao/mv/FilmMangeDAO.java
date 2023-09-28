package com.softeem.mvct.dao.mv;

import com.softeem.mvct.entity.mv.Filmmange;
import com.softeem.mvct.utils.DBUtils;

import java.util.Date;
import java.util.List;

public class FilmMangeDAO {


    public List<Filmmange> queryRoomandTm(Filmmange filmmange){
        String sql = "select * from filmmange where room =? and (endtm <= ? and  starttm >= ?)";
        return DBUtils.queryList(Filmmange.class,sql,filmmange.getRoom(),filmmange.getStarttm(),filmmange.getEndtm());
    }

    public boolean insert(Filmmange film) {
        String sql = "insert into filmmange(filmid,filmname,room,starttm,endtm,price) values(?,?,?,?,?,?)";
        return DBUtils.insert(sql,
                film.getFilmid(),
                film.getFilmname(),
                film.getRoom(),
                film.getStarttm(),
                film.getEndtm(),
                film.getPrice());
    }

    public Filmmange selectById(Integer eno) {
        String sql = "select * from filmmange where id=?";
        return DBUtils.queryOne(Filmmange.class, sql, eno);
    }

    public boolean update(Filmmange film) {
        String sql = "update filmmange set price=? where id=?";
        return DBUtils.update(sql,film.getPrice(),film.getId());
    }


    public boolean deleteById(int id) {
        String sql = "delete from filmmange where id=?";
        return DBUtils.delete(sql, id);
    }

    public List<Filmmange> query(){
        String sql = "select * from filmmange";
        return DBUtils.queryList(Filmmange.class,sql);
    }

    public Filmmange selectByNameandTm(String name, Date tm) {
        String sql = "select * from filmmange where filmname like ? and starttm =?";
        return DBUtils.queryOne(Filmmange.class, sql, "%"+name+"%",tm);
    }

}
