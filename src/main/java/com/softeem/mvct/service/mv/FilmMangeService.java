package com.softeem.mvct.service.mv;

import com.softeem.mvct.dao.mv.FilmMangeDAO;
import com.softeem.mvct.entity.Result;
import com.softeem.mvct.entity.mv.Filmmange;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class FilmMangeService {

    private FilmMangeDAO filmDAO = new FilmMangeDAO();



    public Result queryRoomandTm(Filmmange filmmange){
        //根据id查询是否存在该用户
        List<Filmmange> list = filmDAO.queryRoomandTm(filmmange);
        if(list.size()<1){
            return Result.ok();
        }
        return Result.fail().msg("此时段已经安排了："+list.get(0).toString());
    }

    public Result addFilm(Filmmange film){
        boolean b = filmDAO.insert(film);
        if(b){
            return Result.ok().msg("添加成功");
        }
        return Result.fail().msg("添加失败");
    }

    public Result modifyFilm(Integer  id,BigDecimal price){
        Filmmange film = filmDAO.selectById(id);
        if(film == null){
            return Result.fail().msg("该场次不存在");
        }
        film.setId(id);
        film.setPrice(price);
        boolean b = filmDAO.update(film);
        if(b){
            return Result.ok().msg("修改成功");
        }
        return Result.fail().msg("修改失败");
    }


    public Result delById(int id){
        Filmmange film = filmDAO.selectById(id);
        if(film == null){
            return Result.fail().msg("该电影不存在");
        }

        boolean b = filmDAO.deleteById(id);
        if(b){
            return Result.ok().msg("删除成功");
        }
        return Result.fail().msg("删除失败");
    }

    public Result queryList(){
        //根据id查询是否存在该用户
        List<Filmmange> list = filmDAO.query();
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }


    public Result selectByNameandTm(String name,Date tm){
        Filmmange film = filmDAO.selectByNameandTm(name,tm);
        if(film == null){
            return Result.fail().msg("该电影不存在");
        }
        return Result.ok().data(film);
    }

}
