package com.softeem.mvct.entity;

/**
 * 统一返回结果
 */
public class Result {

    /**
     * 执行业务操作之后的返回消息
     */
    private String msg;
    /**
     * 执行结果 true-成功  false-失败
     */
    private boolean success;
    /**
     * 附加数据
     */
    private Object data;

    /**
     * 统一成功返回结果（用于链式编程）
     * @return
     */
    public static Result ok(){
        Result r = new Result();
        r.msg = "操作成功";
        r.success = true;
        return r;
    }

    public static Result fail(){
        Result r = new Result();
        r.msg = "操作失败";
        r.success = false;
        return r;
    }

    public Result msg(String msg){
        this.msg = msg;
        return this;
    }

    public Result success(boolean success){
        this.success = success;
        return this;
    }

    public Result data(Object data){
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }
}
