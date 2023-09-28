package com.softeem.mvct.service.mv;

import com.softeem.mvct.dao.mv.FilmDAO;
import com.softeem.mvct.entity.Result;
import com.softeem.mvct.entity.mv.Film;

import java.util.List;

public class FilmService {

    private FilmDAO filmDAO = new FilmDAO();

    public Result queryList(){
        //根据id查询是否存在该用户
        List<Film> list = filmDAO.query();
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }


    public Result addFilm(Film film){
        boolean b = filmDAO.insert(film);
        if(b){
            return Result.ok().msg("添加成功");
        }
        return Result.fail().msg("添加失败");
    }

    public Result modifyFilm(Integer  id,String name){
        Film film = filmDAO.selectById(id);
        if(film == null){
            return Result.fail().msg("该电影不存在");
        }
        film.setId(id);
        film.setName(name);
        boolean b = filmDAO.update(film);
        if(b){
            return Result.ok().msg("修改成功");
        }
        return Result.fail().msg("修改失败");
    }

    public Result delById(int id){
        Film film = filmDAO.selectById(id);
        if(film == null){
            return Result.fail().msg("该电影不存在");
        }

        boolean b = filmDAO.deleteById(id);
        if(b){
            return Result.ok().msg("删除成功");
        }
        return Result.fail().msg("删除失败");
    }

    public Result queryByName(String name){
        //根据id查询是否存在该用户
        List<Film> list = filmDAO.queryByName(name);
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }

    public Result queryByFilmName(String name){
        //根据id查询是否存在该用户
        Film film = filmDAO.queryByFilmName(name);
        if(film == null){
            return Result.fail().msg("该电影不存在");
        }
        return Result.ok().data(film);
    }


}
