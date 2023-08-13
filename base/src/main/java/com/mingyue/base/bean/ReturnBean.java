package com.mingyue.base.bean;

public class ReturnBean<T> {

    private String msg;

    private T Data;

    private String version = "1.0.0";

    private Integer code;







    public static <T> ReturnBean<T> ok(String msg) {
        ReturnBean<T> returnBean = new ReturnBean<>();
        returnBean.setCode(200);
        returnBean.setMsg(msg);
        return returnBean;

    }

    public static <T> ReturnBean<T> error(String msg) {
        ReturnBean<T> returnBean = new ReturnBean<>();
        returnBean.setCode(500);
        returnBean.setMsg(msg);
        return returnBean;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return Data;
    }

    public ReturnBean<T> setData(T data) {
        Data = data;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
