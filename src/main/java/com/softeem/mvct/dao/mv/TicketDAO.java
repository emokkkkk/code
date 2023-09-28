package com.softeem.mvct.dao.mv;

import com.softeem.mvct.entity.HistoryTicket;
import com.softeem.mvct.entity.mv.Ticket;
import com.softeem.mvct.utils.DBUtils;

import java.math.BigDecimal;
import java.util.List;

public class TicketDAO {

    public List<Ticket> queryListByFilmmangeId(Integer filmmangeId){
        String sql = "select * from ticket where filmmangeid =? ";
        return DBUtils.queryList(Ticket.class,sql,filmmangeId);
    }


    public List<Ticket> queryList(int filmmangeid,String sjhoruserid ,BigDecimal money){
        String sql = "select * from ticket a LEFT JOIN user b on a.userid = b.id where a.filmmangeid =? and (b.id=? or b.phone =?) and money =? ";
        return DBUtils.queryList(Ticket.class,sql,filmmangeid,sjhoruserid,sjhoruserid,money);
    }


    public List<HistoryTicket> historyBuy(int userid){
        String sql = "select b.username,c.filmname,c.starttm,c.endtm,a.ticketid,a.money,a.seat,a.crtm,a.state from ticket a LEFT JOIN user b on a.userid = b.id left JOIN filmmange c on a.filmmangeid = c.id " +
                "where a.userid = ? ";
        return DBUtils.queryList(HistoryTicket.class,sql,userid);
    }

    public boolean insert(Ticket ticket) {
        String sql = "insert into ticket(ticketid,userid,money,filmmangeid,seat) values(?,?,?,?,?)";
        return DBUtils.insert(sql,
                ticket.getTicketid(),
                ticket.getUserid(),
                ticket.getMoney(),
                ticket.getFilmmangeid(),
                ticket.getSeat()
        );
    }

    public Ticket selectById(String id){
        String sql = "select * from ticket where ticketid=?";
        return DBUtils.queryOne(Ticket.class,sql,id);
    }

    public boolean updateState(String id){
        String sql = "update ticket set state=1 where ticketid=?";
        //执行修改
        return DBUtils.update(sql,id);
    }


}
