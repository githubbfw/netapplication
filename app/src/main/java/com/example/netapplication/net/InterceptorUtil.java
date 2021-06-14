package com.example.netapplication.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2021/6/6
 * Describe ： 拦截器工具类
 */

public class InterceptorUtil {

    public static String TAG="----";
    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor(){
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w(TAG, "log: "+message );
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }

    public static Interceptor HeaderInterceptor(){
        return   new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request mRequest=chain.request();
                //在这里你可以做一些想做的事,比如token失效时,重新获取token
                //或者添加header等等,PS我会在下一篇文章总写拦截token方法
                Request.Builder builder = mRequest.newBuilder()
                        .addHeader("version","1111")
                        .addHeader("device","android")
                        .addHeader("timestamp","")
                        .addHeader("customerUID","123")
//                        .addHeader("token",SPUtils.getInstance().getString("token"))
                        .addHeader("merid","1231");
                Request build = builder.build();
                return chain.proceed(build);
            }
        };

    }



}
