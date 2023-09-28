package com.softeem.mvct.service.mv;

import com.softeem.mvct.dao.mv.UserDAO;
import com.softeem.mvct.entity.Result;
import com.softeem.mvct.entity.UserDesVo;
import com.softeem.mvct.entity.mv.User;

import java.util.List;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    /**
     * 用户登录
     * @param name
     * @param pwd
     * @return
     */
    public Result login(String name, String pwd){
        User user = userDAO.selectByUsername(name);
        if(user == null){
            return Result.fail().msg("用户不存在");
        }
        if(!user.getPassword().equals(pwd)){
            return Result.fail().msg("密码错误");
        }
        return Result.ok().msg("登录成功").data(user);
    }

    /**
     * 修改密码
     * @param user
     * @return
     */
    public Result updatePwd(User user){
        User u = userDAO.selectByUsername(user.getUsername());
        if(u == null){
            return Result.fail().msg("用户不存在");
        }
        boolean b = userDAO.updatePwd(user);
        if(b){
            return Result.ok().msg("修改成功!");
        }
        return Result.fail().msg("修改失败");
    }


    public Result add(User u){
        User user = userDAO.selectByUsername(u.getUsername());
        if(user != null){
            return Result.fail().msg("该用户已存在!");
        }
        //创建用户对象
        boolean b = userDAO.insert(u);
        if(b){
            return Result.ok().msg("添加成功");
        }
        return Result.fail().msg("添加失败");
    }

    /**
     * 修改用户角色（权限）
     * @param id
     * @param role
     * @return
     */
    public Result updateStatus(int id,int role){
        //根据id查询是否存在该用户
        User user = userDAO.selectById(id);
        if(user == null){
            return Result.fail().msg("该用户不存在");
        }
        user = new User();
        user.setId(id);
        user.setRole(role);
        boolean b = userDAO.updateRole(user);
        if(b){
            return Result.ok().msg("修改成功!");
        }
        return Result.fail().msg("修改失败");
    }


    public Result queryList(){
        //根据id查询是否存在该用户
        List<UserDesVo> list = userDAO.query();
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }

    public Result queryListByIdOrName(String idornm){
        //根据id查询是否存在该用户
        List<UserDesVo> list = userDAO.query(idornm);
        if(list.size()<1){
            return Result.fail().msg("无记录");
        }
        return Result.ok().data(list);
    }

    public Result queryUserById(int id){

        User user = userDAO.selectById(id);
        if(user == null){
            return Result.fail().msg("该用户不存在");
        }
        return Result.ok().data(user);
    }

    public Result delUser(int id){

        boolean b = userDAO.deleteById(id);
        if(b){
            return Result.ok().msg("删除成功");
        }
        return Result.fail().msg("删除失败");
    }

    public Result restePwd(Integer id){
        User u = userDAO.selectById(id);
        if(u == null){
            return Result.fail().msg("用户不存在");
        }
        //
        User s = new User();
        s.setId(id);
        // 默认密码 12345678
        s.setPassword("12345678");

        boolean b = userDAO.updatePwd(s);
        if(b){
            return Result.ok().msg("重置密码成功：密码为：12345678");
        }
        return Result.fail().msg("修改失败");
    }

    public Result updatelvl(int id,int lvl){
        //根据id查询是否存在该用户
        User user = userDAO.selectById(id);
        if(user == null){
            return Result.fail().msg("该用户不存在");
        }
        user = new User();
        user.setId(id);
        user.setLevel(lvl);
        boolean b = userDAO.updateLvl(user);
        if(b){
            return Result.ok().msg("修改成功!");
        }
        return Result.fail().msg("修改失败");
    }


    //======================================================================================






}
