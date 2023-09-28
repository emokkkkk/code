package com.softeem.mvct.service.mv;

import com.softeem.mvct.dao.mv.TicketDAO;
import com.softeem.mvct.entity.HistoryTicket;
import com.softeem.mvct.entity.Result;
import com.softeem.mvct.entity.mv.Ticket;

import java.math.BigDecimal;
import java.util.List;

public class TicketService {

    private TicketDAO ticketDAO = new TicketDAO();


    public Result queryListByFilmmangeId(Integer id){
        //根据id查询是否存在该用户
        List<Ticket> list = ticketDAO.queryListByFilmmangeId(id);
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }


    public Result queryList(int filmmangeid,String sjhoruserid ,BigDecimal money){
        //根据id查询是否存在该用户
        List<Ticket> list = ticketDAO.queryList(filmmangeid,sjhoruserid,money);
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }

    public Result addTicket(List<Ticket> t){
        boolean b = false;
        for (Ticket s:t){
             b = ticketDAO.insert(s);
        }
        if(b){
            return Result.ok().msg("添加成功");
        }
        return Result.fail().msg("添加失败");
    }


    public Result historyBuy(int userid){

        List<HistoryTicket> list = ticketDAO.historyBuy(userid);
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }

    public Result updateStatus(String id){

        Ticket ticket = ticketDAO.selectById(id);
        if(ticket == null){
            return Result.fail().msg("没有该编号");
        }
        if(1==ticket.getState()){
            return Result.fail().msg("票已被取，不能重复取票");
        }
        boolean b = ticketDAO.updateState(ticket.getTicketid());
        if(b){
            return Result.ok().msg("成功出票!");
        }
        return Result.fail().msg("出票失败");
    }

}
