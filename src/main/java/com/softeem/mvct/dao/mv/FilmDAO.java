package com.softeem.mvct.dao.mv;

import com.softeem.mvct.entity.mv.Film;
import com.softeem.mvct.utils.DBUtils;

import java.util.List;

public class FilmDAO {

    public List<Film> query(){
        String sql = "select * from film";
        return DBUtils.queryList(Film.class,sql);
    }


    public boolean insert(Film film) {
        String sql = "insert into film(name,director,leads,synopsis,duration) values(?,?,?,?,?)";
        return DBUtils.insert(sql,
                film.getName(),
                film.getDirector(),
                film.getLeads(),
                film.getSynopsis(),
                film.getDuration());
    }

    public Film selectById(Integer eno) {
        String sql = "select * from film where id=?";
        return DBUtils.queryOne(Film.class, sql, eno);
    }

    public boolean deleteById(int id) {
        String sql = "delete from film where id=?";
        return DBUtils.delete(sql, id);
    }

    public boolean update(Film film) {
        String sql = "update film set name=? where id=?";
        return DBUtils.update(sql,film.getName(),film.getId());
    }

    public List<Film> queryByName(String name){
        String sql = "select * from film where name like ? or director like ? or leads like ? ";
        String s = "%"+name+"%";
        return DBUtils.queryList(Film.class,sql,s,s,s);
    }

    public Film queryByFilmName(String name){
        String sql = "select * from film where name =? ";
        return DBUtils.queryOne(Film.class,sql,name);
    }

}
