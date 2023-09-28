package com.softeem.mvct;

import com.softeem.mvct.entity.HistoryTicket;
import com.softeem.mvct.entity.Result;

import com.softeem.mvct.entity.UserDesVo;
import com.softeem.mvct.entity.mv.Film;
import com.softeem.mvct.entity.mv.Filmmange;
import com.softeem.mvct.entity.mv.Ticket;
import com.softeem.mvct.entity.mv.User;
import com.softeem.mvct.service.mv.FilmMangeService;
import com.softeem.mvct.service.mv.FilmService;
import com.softeem.mvct.service.mv.TicketService;
import com.softeem.mvct.service.mv.UserService;
import com.softeem.mvct.utils.MvcUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class StartMvct {


    private static Scanner sc = new Scanner(System.in);
    private static UserService userService = new UserService();
    private static FilmService filmService = new FilmService();
    private static FilmMangeService filmMangeService = new FilmMangeService();
    private static TicketService ticketService = new TicketService();


    /**
     * 用于记录当前登录的用户
     */
    private static User user;
    // 记录登录次数 超过 5次账号锁定
    private static int dlcs = 0;
    // 排
    private static int row = 7;
    private static int column = 12;

    private static Filmmange fmg ;

    public static void main(String[] args) {
        // 初始化 管理员账号
        // 初始化 电影场次 （一周的数据）
        // 登录
        login();

    }

    //登录页
    public static void login(){
        System.out.println("==========================");
        System.out.println("===欢迎来到云南电影城===");
        System.out.println("【1】登录");
        System.out.println("【2】注册");
        System.out.println("请输入操作指令:");
        String s = sc.nextLine();
        switch (s){
            case "1":
                dl();
               break;
            case "2":
                zc();
               break;
            case "0":
                //退出系统
                System.out.println("谢谢使用，再见！");
                System.exit(0);
            default:
                System.out.println("指令错误，请重新输入");
                login();
        }
    }

    //登录
    public static void dl() {
        System.out.println("【用户登录】，请输入账号密码（例如：账号/密码）:");
        //读取一行用户输入的文本
        String line = sc.nextLine();
        if(!line.matches(".+?/.+?")){
            //使用正则表达式匹配输入的内容是否符合特定规则
            System.out.println("输入格式不正确，请检查!");
            //递归调用
            dl();
            return;
        }
        //使用“/”截取账号密码
        String[] userInfo = line.split("/");
        //分别获取数组中的账号和密码
        String username = userInfo[0];
        String password = userInfo[1];
        //执行用户登录逻辑
        Result r = userService.login(username, password);
        if(!r.isSuccess()){
            if(dlcs < 5){
                System.out.println(r.getMsg());
                //登录失败，递归调用，重新输入
                dl();
                return;
            }else {
                System.out.println("你的输入错误超过 5次，账号已锁定");
                //todo 修改账号状态

            }

        }
        System.out.println(r.getMsg());
        //将当前成功登录的用户记录到全局静态变量中
        user = (User) r.getData();
        //按角色进入首页
         sy();


    }

    //注册
    public static void zc() {
        System.out.println("请输入你的用户名称");
        String name = sc.nextLine();
        if(name.length() <5){
            System.out.println("你输入的用户名长度小于 5");
            zc();
            return;
        }
        System.out.println("请输入你的密码");
        String pwd = sc.nextLine();

        if(!MvcUtils.chakpwd(pwd)){
            System.out.println("密码长度大于8个字符，且必须包含大小写字母、数字和标点符号");
            zc();
            return;
        }

        System.out.println("请输入你的手机号");
        String sjh = sc.nextLine();
        if(!MvcUtils.chaksjh(sjh)){
            System.out.println("你输入的手机号码格式错误");
            zc();
            return;
        }

        System.out.println("请输入你的邮箱");
        String emal = sc.nextLine();
        // 校验
        if(!MvcUtils.chakemail(emal)){
            System.out.println("你输入的邮箱格式错误");
            zc();
            return;
        }
        User us = new User();
        us.setUsername(name);
        us.setPassword(pwd);
        us.setPhone(sjh);
        us.setEmail(emal);
        Result r = userService.add(us);
        System.out.println(r.getMsg());
        if(r.isSuccess()){
            dl();
        }else {
            zc();
        }

    }

    // 首页菜单 （根据不通的角色展示不同的操作页面）
    public static void sy(){
        //管理员 1 、 经理 2 、前台 3 、4 普通用户
        System.out.println("==========================");
        System.out.println("===欢迎来到云南电影城===");
        Integer rol = user.getRole();
        if(1==rol){
            admin();
        } else if (2 == rol) {
            manage();
        } else if (3 == rol) {
            reception();
        }else if (4 == rol) {
            userView();
        }
        sy();

    }

    // 超级管理员角色
    private static void admin(){
        System.out.println("【1】查询所有用户信息");
        System.out.println("【2】查询指定用户信息");
        System.out.println("【3】删除用户信息");
        System.out.println("【4】修改密码");
        System.out.println("【5】重置用户密码");
        System.out.println("【6】修改用户角色");
        System.out.println("【7】修改用户级别");
        System.out.println("【0】退出");
        System.out.println("请输入操作指令:");
        String s = sc.nextLine();
        switch (s){
            case "1":
                queryUser();
                break;
            case "2":
                queryUserId();
                break;
            case "3":
                delUser();
                break;
            case "4":
                modifyPwd();
                break;
            case "5":
                resetPwd();
                break;
            case "6":
                updateRole();
                break;
            case "7":
                updateLv();
                break;
            case "0":
                //退出系统
                System.out.println("谢谢使用，再见！");
                System.exit(0);
            default:
                System.out.println("指令错误，请重新输入");
                admin();
        }
    }

    // 查询用户所有信息
    private static void queryUser(){

        Result r = userService.queryList();
        if(!r.isSuccess()){
            System.out.println(r.getMsg());
            return;
        }
        System.out.println("ID\t用户名\t用户级别\t用户注册时间\t用户累计消费总金额\t用户累计消费次数\t用户手机号\t用户邮箱");
        System.out.println("----------------------------------------------------------");
        List<UserDesVo> list = (List<UserDesVo>)r.getData();
        list.forEach(System.out::println);

    }

    private static void queryUserId(){
        System.out.println("请输入你要查询的ID或者用户名称");
        String s = sc.nextLine();
        Result r = userService.queryListByIdOrName(s);
        if(!r.isSuccess()){
            System.out.println(r.getMsg());
            return;
        }
        System.out.println("ID\t用户名\t用户级别\t用户注册时间\t用户累计消费总金额\t用户累计消费次数\t用户手机号\t用户邮箱");
        System.out.println("----------------------------------------------------------");
        List<UserDesVo> list = (List<UserDesVo>)r.getData();
        list.forEach(System.out::println);

    }

    private static void delUser(){
        System.out.println("请输入你要删除的用户ID");
        String s = sc.nextLine();
        try {
            Result r = userService.queryUserById(Integer.valueOf(s));
            if(!r.isSuccess()){
                System.out.println(r.getMsg());
                return;
            }
            User user = (User) r.getData();
            System.out.println("你确定要删除"+user.getUsername()+"用户吗？（Y/N）");
            String y = sc.nextLine();
            if("Y".equals(y.toUpperCase())){
               Result r1 = userService.delUser(Integer.valueOf(s));
                if(!r.isSuccess()){
                    System.out.println(r1.getMsg());
                    return;
                }
            }
        }catch (Exception e){
            System.out.println("你输入的格式有误");
            delUser();
        }
    }

    private static void modifyPwd(){
        System.out.println("请输入新密码:");
        String pwd = sc.nextLine();
        if(!MvcUtils.chakpwd(pwd)){
            System.out.println("你输入的密码格式不对");
            modifyPwd();
            return;
        }
        System.out.println("请输入重复密码:");
        String repwd = sc.nextLine();
        if(!pwd.equals(repwd)){
            System.out.println("两次密码不一致,请重新输入!");
            modifyPwd();
            return;
        }
        //设置新密码到用户对象中
        user.setPassword(pwd);
        //执行修改密码
        Result r = userService.updatePwd(user);
        //输出执行结果
        System.out.println(r.getMsg());
    }

    private static void resetPwd(){
        System.out.println("请输入你重置用户ID");
        String s = sc.nextLine();
        try {
            Result r =userService.restePwd(Integer.valueOf(s));
            if(!r.isSuccess()){
                System.out.println(r.getMsg());
                return;
            }
            System.out.println(r.getMsg());
        }catch (Exception e){
            System.out.println("你输入的Id格式错误");
        }

    }

    private static void updateRole(){
        System.out.println("请输入用户ID");
        String s = sc.nextLine();
        try {
            Result r =userService.queryUserById(Integer.valueOf(s));
            if(!r.isSuccess()){
                System.out.println(r.getMsg());
                return;
            }
            System.out.println("请输入你要修改的值：1：管理员 2：经理 3：前台 4：用户");
            String s2 = sc.nextLine();
            Result r1= userService.updateStatus(Integer.valueOf(s),Integer.valueOf(s2));
            if(!r1.isSuccess()){
                System.out.println(r1.getMsg());
                return;
            }
            System.out.println(r1.getMsg());
        }catch (Exception e){
            System.out.println("你输入的Id格式错误");
        }
    }

    private static void updateLv(){
        System.out.println("请输入用户ID");
        String s = sc.nextLine();
        try {
            Result r =userService.queryUserById(Integer.valueOf(s));
            if(!r.isSuccess()){
                System.out.println(r.getMsg());
                return;
            }
            System.out.println("请输入你要修改的值：1：金牌 2：银牌 3：铜牌");
            String s2 = sc.nextLine();
            Result r1= userService.updatelvl(Integer.valueOf(s),Integer.valueOf(s2));
            if(!r1.isSuccess()){
                System.out.println(r1.getMsg());
                return;
            }
            System.out.println(r1.getMsg());
        }catch (Exception e){
            System.out.println("你输入的Id格式错误");
        }
    }

    private static void manage(){
        System.out.println("【1】影片管理");
        System.out.println("【2】排片管理");
        System.out.println("【0】退出");
        System.out.println("请输入操作指令:");
        String s = sc.nextLine();
        switch (s){
            case "1":
                filmManage();
                break;
            case "2":
                fimlLayout();
                break;
            case "0":
                //退出系统
                System.out.println("谢谢使用，再见！");
                System.exit(0);
            default:
                System.out.println("指令错误，请重新输入");
                manage();
        }
    }

    private static void filmManage(){
        System.out.println("【1】查询所有影片信息");
        System.out.println("【2】添加影片信息");
        System.out.println("【3】修改电影的信息");
        System.out.println("【4】删除影片的信息");
        System.out.println("【5】查询影片的信息");
        System.out.println("请输入操作指令:");
        String s = sc.nextLine();
        switch (s){
            case "1":
                queryFilmList();
                break;
            case "2":
                addFilm();
                break;
            case "3":
                updateFilm();
                break;
            case "4":
                delFilm();
                break;
            case "5":
                queryByName();
                break;
            default:
                System.out.println("指令错误，请重新输入");
                filmManage();
        }

    }

    private static void queryFilmList(){
        Result r = filmService.queryList();
        if(!r.isSuccess()){
            System.out.println(r.getMsg());
            return;
        }
        System.out.println("ID\t片名\t导演\t主演\t剧情简介\t时长");
        System.out.println("----------------------------------------------------------");
        List<Film> list = (List<Film>)r.getData();
        list.forEach(System.out::println);

    }

    private static void addFilm() {
        System.out.println("请输入电影信息（格式：片名/导演/主演/剧情简介/时长）");
        String line = sc.nextLine();
        //使用“/”将字符串分割为字符串数组
        String[] fs = line.split("/");
        try {
            Film f = new Film();
            f.setName(fs[0]);
            f.setDirector(fs[1]);
            f.setLeads(fs[2]);
            f.setSynopsis(fs[3]);
            f.setDuration(Integer.valueOf(fs[4]));

            Result r = filmService.addFilm(f);
            System.out.println(r.getMsg());
            if(!r.isSuccess()){
                addFilm();
            }
        }catch (Exception e){
            System.out.println("输入格式有误");
            addFilm();
        }

    }

    private static void updateFilm() {
        System.out.println("请输入电影编号和名称(格式：编号/名称)");
        String line = sc.nextLine();
        try {
            //使用"/"做分割，将输入内容转换为字符串数组
            String[] s = line.split("/");
            Integer id = Integer.valueOf(s[0]);
            String name = s[1];
            //调用业务代码实现更新
            Result r = filmService.modifyFilm(id, name);
            System.out.println(r.getMsg());
            if(!r.isSuccess()){
                updateFilm();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private static void delFilm() {
        System.out.println("请输入电影编号");
        String s = sc.nextLine();
        try {
            Result r = filmService.delById(Integer.valueOf(s));
            System.out.println(r.getMsg());
            if(!r.isSuccess()){
                delFilm();
            }
        }catch (Exception e){
            System.out.println("输入格式有误");
        }

    }

    private static void queryByName(){
        System.out.println("请输入电影名称、或主演、或导演");
        String s = sc.nextLine();
        Result r = filmService.queryByName(s);
        if(!r.isSuccess()){
            System.out.println(r.getMsg());
            return;
        }
        System.out.println("ID\t片名\t导演\t主演\t剧情简介\t时长");
        System.out.println("----------------------------------------------------------");
        List<Film> list = (List<Film>)r.getData();
        list.forEach(System.out::println);


    }

    private static void fimlLayout(){
        System.out.println("【1】增加场次");
        System.out.println("【2】修改场次");
        System.out.println("【3】删除场次");
        System.out.println("【4】查询所有场次信息");
        System.out.println("请输入操作指令:");
        String s = sc.nextLine();
        switch (s){
            case "1":
                addFilmMg();
                break;
            case "2":
                updateFilmlayout();
                break;
            case "3":
                delFilmmange();
                break;
            case "4":
                queryList();
                break;
            default:
                System.out.println("指令错误，请重新输入");
                fimlLayout();
        }
    }

    private static void addFilmMg() {
        System.out.println("请输入电影名称");
        String s = sc.nextLine();
        try {
            Result r =filmService.queryByFilmName(s);
            System.out.println(r.getMsg());
            if(!r.isSuccess()){
                addFilmMg();
            }
            Film f = (Film)r.getData();
            System.out.println("请指定放映厅：（1-5）");
            String room = sc.nextLine();
            System.out.println("请指定放映时间段（09241330-09241530 等价于 09-24 13:30-09-24 15:30）");
            String tm = sc.nextLine();
            //校验此时放映厅是否安排
            Map<String,LocalDateTime> map =MvcUtils.getTm(tm);
            Filmmange filmmange = new Filmmange();
            filmmange.setRoom(Integer.valueOf(room));
            filmmange.setStarttm(map.get("starttm"));
            filmmange.setEndtm(map.get("endtm"));
            Result r1 =filmMangeService.queryRoomandTm(filmmange);
            System.out.println(r1.getMsg());
            if(!r1.isSuccess()){
                addFilmMg();
            }
            System.out.println("请输入价格");
            String m = sc.nextLine();
            filmmange.setPrice(new BigDecimal(m));
            filmmange.setFilmid(f.getId());
            filmmange.setFilmname(f.getName());
            filmMangeService.addFilm(filmmange);
            if(!r1.isSuccess()){
                addFilmMg();
            }
        }catch (Exception e){
            System.out.println("输入格式有误");
            addFilmMg();
        }


    }

    private static void updateFilmlayout() {
        System.out.println("请输入场次id和价格(格式：编号/价格)");
        String line = sc.nextLine();
        try {
            //使用"/"做分割，将输入内容转换为字符串数组
            String[] s = line.split("/");
            Integer id = Integer.valueOf(s[0]);
            String price = s[1];
            //调用业务代码实现更新
            Result r = filmMangeService.modifyFilm(id, new BigDecimal(price));
            System.out.println(r.getMsg());
            if(!r.isSuccess()){
                updateFilm();
            }
        }catch (Exception e){
            System.out.println("输入格式有误");
            updateFilmlayout();
        }

    }

    private static void delFilmmange() {
        try {
            System.out.println("请输入场次编号");
            String s = sc.nextLine();
            Result r = filmMangeService.delById(Integer.valueOf(s));
            System.out.println(r.getMsg());
            if(!r.isSuccess()){
                delFilm();
            }
        }catch (Exception e){
            System.out.println("输入格式有误");
            delFilmmange();
        }

    }

    private static void queryList(){
        Result r = filmMangeService.queryList();
        if(!r.isSuccess()){
            System.out.println(r.getMsg());
            return;
        }
        System.out.println("ID\t片名\t影厅\t时间段\t票价");
        System.out.println("----------------------------------------------------------");
        List<Filmmange> list = (List<Filmmange>)r.getData();
        list.forEach(System.out::println);

    }

    private static void reception(){
        System.out.println("【1】查询正在上映的影片信息");
        System.out.println("【2】查询所有场次信息");
        System.out.println("【3】查询指定电影和场次的信息");
        System.out.println("【4】售票");
        System.out.println("【0】退出");
        System.out.println("请输入操作指令:");
        String s = sc.nextLine();
        switch (s){
            case "1":
                queryFilmList();
                break;
            case "2":
                queryList();
                break;
            case "3":
                queryListByName();
                break;
            case "4":
                queryticketing();
                break;
            case "0":
                //退出系统
                System.out.println("谢谢使用，再见！");
                System.exit(0);
            default:
                System.out.println("指令错误，请重新输入");
                reception();
        }
    }

    // 查询指定电影的名称和场次
    private static void queryListByName(){
        try {
            System.out.println("请输入电影名称和场次（杀破狼/09241330）");
            String line = sc.nextLine();
            String[] s = line.split("/");
            String name = s[0];
            Date tm = MvcUtils.getStotm(s[1]);
            Result r = filmMangeService.selectByNameandTm(name,tm);
            if(!r.isSuccess()){
                System.out.println(r.getMsg());
                return;
            }
            System.out.println("ID\t片名\t影厅\t时间段\t票价");
            System.out.println("----------------------------------------------------------");
            Filmmange filmmange = (Filmmange)r.getData();
            System.out.println(filmmange.toString());
            //记录当前的场次
            fmg = filmmange;
            System.out.println("座位信息如下：");
            //查询该场次的售票信息
            Result r1 =ticketService.queryListByFilmmangeId(filmmange.getId());
            if(!r1.isSuccess()){
                //没有购票记录
                MvcUtils.room(row,column);
                return;
            }

            List<Ticket> list = (List<Ticket>) r1.getData();
            //获得座位信息
            List<String> lr = list.stream().map(e->e.getSeat()).collect(Collectors.toList());
            MvcUtils.rooms(row,column,lr);
        }catch (Exception e){
            System.out.println(e.getMessage());
            queryListByName();
        }


    }

    /**
     * 查询询售票信息
     */
    private static void queryticketing(){
        try {
            System.out.println("请输入电影名称/场次/(手机号或者用户名)/支付金额：（杀破狼/09241330/15333333333/90）");
            String line = sc.nextLine();
            String[] s = line.split("/");
            String name = s[0];
            Date tm = MvcUtils.getStotm(s[1]);
            Result r = filmMangeService.selectByNameandTm(name,tm);
            if(!r.isSuccess()){
                System.out.println(r.getMsg());
                return;
            }
            String sjhandid = s[2];
            BigDecimal je =new BigDecimal(s[3]);
            Filmmange filmmange = (Filmmange)r.getData();
            Result r1 = ticketService.queryList(filmmange.getId(),sjhandid,je);
            if(!r1.isSuccess()){
                System.out.println(r1.getMsg());
                return;
            }
            List<Ticket> list = (List<Ticket>)r1.getData();
            System.out.println("片名\t影厅\t时间段\t票价\t支付金额\t座位号\t电影票号");
            list.forEach(e->{
                System.out.println(filmmange.getFilmname()+"\t"+filmmange.getRoom()+
                        "\t"+MvcUtils.getTmToString(filmmange.getStarttm(),"MM-dd HH:mm")+"-"+MvcUtils.getTmToString(filmmange.getEndtm(),"MM-dd HH:mm")+"\t"+filmmange.getPrice()
                        +"\t"+e.getMoney()+"\t"+MvcUtils.seats(e.getSeat())+"\t"+e.getTicketid());
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
            queryticketing();
        }


    }

    //普通用户
    private static void userView(){
        System.out.println("【1】查询正在上映的影片信息");
        System.out.println("【2】查询所有场次信息");
        System.out.println("【3】查询指定电影和场次的信息");
        System.out.println("【4】购票");
        System.out.println("【5】取票");
        System.out.println("【6】查看历史购票信息");
        System.out.println("【0】退出");
        String s = sc.nextLine();
        switch (s){
            case "1":
                queryFilmList();
                break;
            case "2":
                queryList();
                break;
            case "3":
                queryListByName();
                break;
            case "4":
                buyTicket();
                break;
            case "5":
                collectionTck();
                break;
            case "6":
                historyBuy();
                break;
            case "0":
                //退出系统
                System.out.println("谢谢使用，再见！");
                System.exit(0);
            default:
                System.out.println("指令错误，请重新输入");
                userView();
        }

    }

    // 购票
    private static void buyTicket(){
        try {
            queryListByName();
            System.out.println("请输入你要购买的座位（座位号：5排12号输入 5,12 多个多个座位 / 分割）exp: (5,6/5,7) ");
            String line = sc.nextLine();
            String[] s= line.split("/");
            BigDecimal jr = new BigDecimal(0);
            List<Ticket> list = new ArrayList<>();
            String zwh = "";
            String bh = "";

            for (String s1 : s) {
                BigDecimal m = MvcUtils.getMony(user,fmg.getPrice());
                jr = jr.add(m);
                Ticket ticket = new Ticket();
                String ids = MvcUtils.generateRandomCode(8);
                ticket.setTicketid(ids);
                ticket.setUserid(user.getId());
                ticket.setMoney(MvcUtils.getMony(user,fmg.getPrice()));
                ticket.setFilmmangeid(fmg.getId());
                ticket.setSeat(s1);

                list.add(ticket);
                if("".equals(zwh)){
                    zwh = MvcUtils.seats(s1);
                    bh = ids;
                }else {
                    zwh =zwh+","+MvcUtils.seats(s1);
                    bh = bh+","+ids;
                }
            }

            System.out.println("你选择购买的票信息如下：");
            System.out.println("电影名称："+fmg.getFilmname());
            System.out.println("电影场次："+MvcUtils.getTmToString(fmg.getStarttm(),"MM-dd HH:mm")+"-"+MvcUtils.getTmToString(fmg.getEndtm(),"MM-dd HH:mm"));
            System.out.println("影厅："+fmg.getRoom()+"号");
            System.out.println("座位号："+zwh);
            System.out.println("电影票编号："+bh);
            System.out.println("支付金额："+jr);
            System.out.println("=======================================================");
            System.out.println("是否确认购买（Y/N）");
            String y = sc.nextLine();
            if("Y".equals(y.toUpperCase())){
                Result r =ticketService.addTicket(list);
                if(!r.isSuccess()){
                    System.out.println(r.getMsg());
                    return;
                }
            }else {
                userView();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            userView();
        }

    }

    //历史记录
    private static void historyBuy(){
        Result r =ticketService.historyBuy(user.getId());
        if(!r.isSuccess()){
            System.out.println(r.getMsg());
            return;
        }
        System.out.println("用户名\t片名\t场次\t支付金额\t座位号\t购买时间\t电影票编号\t是否取票");
        System.out.println("----------------------------------------------------------");
        List<HistoryTicket> list = (List<HistoryTicket>)r.getData();
        list.forEach(e->{
            System.out.println(e.getUsername()+"\t"+e.getFilmname()+"\t"+MvcUtils.getTmToString(e.getStarttm(),"MM-dd HH:mm")+"-"+MvcUtils.getTmToString(e.getEndtm(),"MM-dd HH:mm")+
            "\t"+e.getMoney()+"\t"+MvcUtils.seats(e.getSeat())+"\t"+MvcUtils.getTmToString(e.getCrtm(),"yyyy-MM-dd HH:mm")+"\t"+e.getTicketid()+"\t"+(e.getState() == 0 ? "否" : "取"));
        });
    }

    /**
     *
     */
    private static void collectionTck(){
        System.out.println("请输入电影票编号");
        String y = sc.nextLine();
        Result r =ticketService.updateStatus(y);
        if(!r.isSuccess()){
            System.out.println(r.getMsg());
            return;
        }
        System.out.println(r.getMsg());
    }


}
