package com.softeem.mvct.utils;

import com.mysql.cj.util.StringUtils;
import com.softeem.mvct.entity.mv.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MvcUtils {

    //构造影厅
    public static void room(int h,int l){
        // 行数
        for (int i = 0; i <h ; i++) {
            for (int j = 0; j <l ; j++) {
                if(i==0){
                    if(j== 0){
                        System.out.print("\t"+(j+1)+"\t");
                    }else {
                        System.out.print("\t"+(j+1)+"\t");
                    }
                }else {
                    continue;
                }
            }
            System.out.println();
            System.out.print(i+1);
            for (int j = 0; j <l ; j++) {
                System.out.print("\t"+"O"+"\t");
            }
            System.out.println();
        }
    }

    public static void rooms(int h, int l, List<String> list){
        // 行数
        for (int i = 0; i <h ; i++) {
            for (int j = 0; j <l ; j++) {
                if(i==0){
                    if(j== 0){
                        System.out.print("\t"+(j+1)+"\t");
                    }else {
                        System.out.print("\t"+(j+1)+"\t");
                    }
                }else {
                    continue;
                }
            }
            System.out.println();
            System.out.print(i+1);
            //列数
            for (int j = 0; j <l ; j++) {
                String rw = (i+1)+","+(j+1);
                if(list.contains(rw)){
                    System.out.print("\t" + "X" + "\t");
                }else {
                    System.out.print("\t" + "O" + "\t");
                }
            }
            System.out.println();
        }
    }

    /**
     *  09241330-09241530 分割成两个时间
     * @param tmsp
     * @return
     */
    public static Map<String,LocalDateTime> getTm(String tmsp){

        if(!StringUtils.isNullOrEmpty(tmsp)){
            String[] tms = tmsp.split("-");
            Date t = new Date();
            Integer y =t.getYear()+1900;
            // yyyymmddhhmm
            String start = y+tms[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            // 将 LocalDateTime 对象转换为 Date 对象
            LocalDateTime t1 = LocalDateTime.parse(start, formatter);
            String end = y+tms[1];
            LocalDateTime t2 =  LocalDateTime.parse(end, formatter);

            Map<String,LocalDateTime> map = new HashMap<>();
            map.put("starttm",t1);
            map.put("endtm",t2);
            return map;
        }

        return null;
    }

    /**
     *
     * @param tm MMddHHmm 解析成 date
     * @return
     */
    public static Date getStotm(String tm){

        if(!StringUtils.isNullOrEmpty(tm)){

            Date t = new Date();
            Integer y =t.getYear()+1900;
            // yyyymmddhhmm
            String start = y+tm;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            // 将 LocalDateTime 对象转换为 Date 对象
            Date t1 = Date.from(LocalDateTime.parse(start, formatter).atZone(ZoneId.systemDefault()).toInstant());
            return t1;
        }

        return null;
    }


    /**
     *
     * @param tm
     * @param form  yyyy-MM-dd HH:mm:ss / MM-dd HH:mm
     * @return
     */
    public static String getTmToString(LocalDateTime tm,String form){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(form);
        formatter.format(tm);

        return formatter.format(tm);
    }

    /**
     *  返回座位号
     * @param s
     * @return
     */
    public static String seats(String s){
        return s.replace(",","排")+"号";
    }

    /**
     *  根据用户级别 计算金额
     * @param user
     * @param p
     * @return
     */
    public static BigDecimal getMony(User user,BigDecimal p){

        if(1== user.getLevel()){
            return new BigDecimal(0.88).multiply(p).setScale(2,BigDecimal.ROUND_UP);
        }else if(2== user.getLevel()){
            return new BigDecimal(0.95).multiply(p).setScale(2,BigDecimal.ROUND_UP);
        }
        return p;
    }

    /**
     *  随机生成一个 由数字组成的 号码
     * @param length
     * @return
     */
    public static String generateRandomCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // 生成一个0-9的随机数，并添加到验证码中
        }
        return code.toString();
    }

    /**
     * 密码校验
     * @param s
     * @return
     */
    public static boolean chakpwd(String s){

        String regex = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";

        if(s.matches(regex)){
            return true;
        }
        return false;
    }

    /**
     * 手机号校验
     * @param s
     * @return
     */
    public static boolean chaksjh(String s){
        String regex = "^1[3456789]\\d{9}$";
        if(s.matches(regex)){
            return true;
        }
        return false;
    }

    /**
     * 邮箱校验
     * @param s
     * @return
     */
    public static boolean chakemail(String s){
        String regex = "^[\\w-]+@[\\w-]+\\.[\\w]+$";
        if(s.matches(regex)){
            return true;
        }
        return false;
    }




    public static void main(String[] args) {

        //room(7,12);
        //getTm("09241330-09241530");
//        String tm=getTmToString(new Date(),"MM-dd HH:mm");
//        System.out.println(tm);

//        List<String> list = new ArrayList<>();
//        list.add("5,6");
//        list.add("3,10");
//        rooms(7,12,list);

//        String s =generateRandomCode(8);
//        System.out.println(s);

        System.out.println(chakpwd("YourPassword123!"));

    }

}
