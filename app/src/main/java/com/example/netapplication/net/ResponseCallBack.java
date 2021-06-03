package com.example.netapplication.net;

/**
 * Created by Administrator on 2021/6/2
 * Describe ：
 */

public interface ResponseCallBack<T> {
    void  onSuccess(T t);
    void  onFault(String errorMsg);
//      void  onFailue(Throwable e,String errorMsg);
}
