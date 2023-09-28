package com.softeem.mvct.utils;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

/**
 * 通用的数据库操作工具类，封装了所有JDBC相关操作
 * 1. 获取连接
 * 2. 资源关闭
 * 3. 执行通用更新操作（添加，删除，修改）
 * 3. 执行通用查询操作
 * 实现技术：
 * 1. JDBC
 * 2. 连接池技术（Druid）
 * 3. 反射
 * 4. 集合操作
 */
public class DBUtils {

    private static String url;
    private static String user;
    private static String password;
    private static int initSize;
    private static int maxActive;
    private static long maxWait;
    private static DruidDataSource dataSource;

    static {
        try (
                //获取指定配置文件的输入流
                InputStream is = DBUtils.class.getResourceAsStream("/jdbc.properties");
        ) {
            //创建一个属性表对象（名值对结构）
            Properties prop = new Properties();
            //加载输入流对应的数据到属性对象中
            prop.load(is);
            //读取属性文件中指定的属性值
            url = prop.getProperty("jdbc.url");
            user = prop.getProperty("jdbc.user");
            password = prop.getProperty("jdbc.password");

            initSize = Integer.parseInt(prop.getProperty("pool.initSize"));
            maxActive = Integer.parseInt(prop.getProperty("pool.maxActive"));
            maxWait = Long.parseLong(prop.getProperty("pool.maxWait"));

            //创建连接池对象并设置初始信息
            dataSource = new DruidDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            dataSource.setInitialSize(initSize);
            dataSource.setMaxActive(maxActive);
            dataSource.setMaxWait(maxWait);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConn() throws SQLException {
        if(dataSource != null){
            return dataSource.getConnection();
        }
        return null;
    }


    /**
     * 关闭资源
     *
     * @param rs
     * @param stat
     * @param conn
     * @throws SQLException
     */
    public static void close(ResultSet rs, Statement stat, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stat != null) stat.close();
            if (conn != null) conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * select * from admin where id>?;
     * select * from admin limit ?,?;
     * 封装通用查询操作，对于任意的查询操作统一实现
     * @param sql    需要执行的查询语句
     * @param params 查询条件参数
     * @return
     */
    public static List<Map<String, Object>> queryList(String sql,Object... params) {
        List<Map<String, Object>> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
                conn = getConn();
                ps = conn.prepareStatement(sql);
                if(Objects.nonNull(params)){
                    //执行查询条件的赋值
                    for (int i = 0; i < params.length; i++) {
                        ps.setObject(i + 1,params[i]);
                    }
                }
                rs = ps.executeQuery();
            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取总列数
            int columnCount = rsmd.getColumnCount();
            //遍历结果集
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                //对每一列循环获取列名称
                for (int i = 1; i <= columnCount; i++) {
                    //获取列名称
                    String cname = rsmd.getColumnLabel(i);
                    //获取列值
                    Object value = rs.getObject(cname);
                    //将列名作为键，列值作为值存储到map中
                    map.put(cname, value);
                }
                list.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            //资源关闭
            close(rs,ps,conn);
        }
        return list;
    }

    /**
     * 通用更新操作的封装，对于任意增，删，改操作统一实现
     *  insert into xx(xx,xx) values(?,?)
     *  update xxx set xx=?,xx=?,... where xx=?
     *  delete from xx where xx=?
     *  exeUpdate("sql",new Object[]{xx,xxx,xx})
     * @param sql
     * @param params
     * @return
     */
    public static boolean exeUpdate(String sql,Object... params){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //获取连接
            conn = getConn();
            //对sql语句编译获取预处理对象
            ps = conn.prepareStatement(sql);
            if(Objects.nonNull(params)){
                //根据传入的参数个数，对sql语句中的占位符填充值（参数有多少，则占位符就有多少）
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1,params[i]);
                }
            }
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            close(null,ps,conn);
        }
        return false;
    }

    public static boolean exeUpdate(Connection conn,String sql,Object... params) throws SQLException {
        PreparedStatement ps = null;
        try{
            //对sql语句编译获取预处理对象
            ps = conn.prepareStatement(sql);
            if(Objects.nonNull(params)){
                //根据传入的参数个数，对sql语句中的占位符填充值（参数有多少，则占位符就有多少）
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1,params[i]);
                }
            }
            return ps.executeUpdate() > 0;
        }finally{
            close(null,ps,null);
        }
    }

    /**
     * 将一个Map对象转换为javabean
     * @param map
     * @param t
     * @param <T>
     * @return
     */
    private static <T> T mapToBean(Map<String,Object> map,Class<T> t){
        try {
            //获取Class对象的无参构造器
            Constructor<T> c = t.getDeclaredConstructor();
            //创建对象（使用无参构造器）
            T obj = c.newInstance();
            //获取Map集合键集
            Set<String> fieldsName = map.keySet();
            for (String fname : fieldsName) {
                //获取每一个键（对象的属性名）
                //根据属性名获取属性对象
                Field field = t.getDeclaredField(fname);
                //抑制访问权限检查（设置属性可访问）
                field.setAccessible(true);
                //获取当前键对应的值（属性值）
                Object val = map.get(fname);
                if(obj instanceof LocalDateTime){
                    LocalDateTime s = (LocalDateTime)val;
                    Date date = Date.from(s.atZone(ZoneId.systemDefault()).toInstant());
                    field.set((Date)obj,date);
                }else {
                    field.set(obj,val);
                }


            }
            return obj;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对queryList方法增强，将返回的结果以具体的javabean集合形式表现
     * @param t
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryList(Class<T> t,String sql,Object... params){
        //将查询结果获取为List<Map<String, Object>>
        List<Map<String, Object>> maps = queryList(sql, params);
        //声明新的集合（泛型动态确定）
        List<T> list  = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            T t1 = mapToBean(map, t);
            list.add(t1);
        }
        return list;
    }

    /**
     * 封装单个对象查询方法
     * @param t
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryOne(Class<T> t,String sql,Object... params){
        List<T> list = queryList(t, sql, params);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 执行添加操作
     * @param sql
     * @param params
     * @return
     */
    public static boolean insert(String sql,Object... params){
        return exeUpdate(sql,params);
    }

    /**
     * 执行添加操作(含事务处理)
     * @param sql
     * @param params
     * @return
     */
    public static boolean insert(Connection conn,String sql,Object... params) throws SQLException {
        return exeUpdate(conn,sql,params);
    }

    /**
     * 执行删除操作
     * @param sql
     * @param params
     * @return
     */
    public static boolean delete(String sql,Object... params){
        return exeUpdate(sql,params);
    }

    /**
     * 执行删除操作(含事务操作)
     * @param sql
     * @param params
     * @return
     */
    public static boolean delete(Connection conn,String sql,Object... params) throws SQLException {
        return exeUpdate(conn,sql,params);
    }

    /**
     * 执行修改操作
     * @param sql
     * @param params
     * @return
     */
    public static boolean update(String sql,Object... params){
        return exeUpdate(sql,params);
    }

    /**
     * 执行修改操作(含事务)
     * @param sql
     * @param params
     * @return
     */
    public static boolean update(Connection conn,String sql,Object... params) throws SQLException {
        return exeUpdate(conn,sql,params);
    }

    /**
     * 执行插入操作 并返回自动生成的键，事务手动开启提交（从外部传入连接对象）
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static int insertGetAutoKey(Connection conn,String sql,Object... params) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //对sql语预编译，并能够返回自动生成的键
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //对Sql语句预处理（将参数值设置到占位符中）
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i+1,params[i]);
            }
            //执行更新
            int i = ps.executeUpdate();
            if (i > 0) {
                //获取包含了自动生成键的结果集对象
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    //获取生成的主键值
                    int id = rs.getInt(1);
                    return id;
                }
            }
        }finally {
            close(rs,ps,null);
        }
        return -1;
    }

    /**
     * 执行插入，并返回自动生成的主键（事务自动提交关闭）
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static int insertGetAutoKey(String sql,Object... params) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = getConn();
            //对sql语预编译，并能够返回自动生成的键
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //对Sql语句预处理（将参数值设置到占位符中）
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i+1,params[i]);
            }
            //执行更新
            int i = ps.executeUpdate();
            if (i > 0) {
                //获取包含了自动生成键的结果集对象
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    //获取生成的主键值
                    int id = rs.getInt(1);
                    return id;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            close(rs,ps,conn);
        }
        return -1;
    }


    /**
     * 根据提供的SQL语句执行通用的记录行数查询
     * @param sql
     * @param param
     * @return
     */
    public static int queryCount(String sql,Object... param){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < param.length; i++) {
                ps.setObject(i+1,param[i]);
            }
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally{
            close(rs,ps,conn);
        }
        return 0;
    }

}
