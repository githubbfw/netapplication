package com.example.netapplication.net;

/**
 * Created by Administrator on 2021/5/31
 * Describe ：  解析实体基类
 */

public class BaseResult<T> {
    private int code; // 返回的code
    private String msg; // message 可用来返回接口的说明
    private T data;  // 具体的数据结果

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
