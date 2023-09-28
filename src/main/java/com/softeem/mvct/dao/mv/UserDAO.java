package com.softeem.mvct.dao.mv;

import com.softeem.mvct.entity.UserDesVo;
import com.softeem.mvct.entity.mv.User;
import com.softeem.mvct.utils.DBUtils;

import java.util.List;

public class UserDAO {

    /**
     * 根据用户名查询用户：对应登录操作
     * @param username
     * @return
     */
    public User selectByUsername(String username){
        //定义SQL语句
        String sql = "select * from user where username=?";
        //执行查询操作获取一个用户对象
        User user = DBUtils.queryOne(User.class, sql, username);
        return user;
    }

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    public User selectById(int id){
        String sql = "select * from user where id=?";
        return DBUtils.queryOne(User.class,sql,id);
    }

    /**
     * 添加用户（管理员）：对应注册操作
     * @param user
     * @return
     */
    public boolean insert(User user){
        //定义SQL语句
        String sql = "insert into user(username,password,phone,email) values(?,?,?,?)";
        //执行添加
        return DBUtils.insert(sql,user.getUsername(),user.getPassword(),user.getPhone(),user.getEmail());
    }

    /**
     * 用户修改：对应管理员信息更新（改密码）
     * @param user
     * @return
     */
    public boolean updatePwd(User user){
        String sql = "update user set password=? where id=?";
        //执行修改
        return DBUtils.update(sql,user.getPassword(),user.getId());
    }

    /**
     * 修改用户的角色（0-普通用户，1-管理员）
     * @param user
     * @return
     */
    public boolean updateRole(User user){
        String sql = "update user set role=? where id=?";
        return DBUtils.update(sql,user.getRole(),user.getId());
    }

    public List<UserDesVo> query(){
        String sql = "select a.id,a.username,a.level,a.crtm,b.total,b.con,a.phone,a.email from user a " +
                "LEFT JOIN (select sum(money) total,count(1) con ,userid from ticket GROUP BY userid) b " +
                "on a.id = b.userid";
        return DBUtils.queryList(UserDesVo.class,sql);
    }

    public List<UserDesVo> query(String idorname){
        String sql = "select a.id,a.username,a.level,a.crtm,b.total,b.con,a.phone,a.email from user a " +
                "LEFT JOIN (select sum(money) total,count(1) con ,userid from ticket GROUP BY userid) b " +
                "on a.id = b.userid where a.id=? or a.username like ? ";
        return DBUtils.queryList(UserDesVo.class,sql,idorname,"%"+idorname+"%");
    }

    public boolean deleteById(int id) {
        String sql = "delete from user where id=?";
        return DBUtils.delete(sql, id);
    }

    public boolean updateLvl(User user){
        String sql = "update user set level=? where id=?";
        return DBUtils.update(sql,user.getLevel(),user.getId());
    }

}
