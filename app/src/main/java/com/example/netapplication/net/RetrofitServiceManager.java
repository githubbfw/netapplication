package com.example.netapplication.net;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2021/5/31
 * Describe ： 统一的接口实例管理类
 */

public class RetrofitServiceManager {
    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;

    private Retrofit mRetrofit;

    private RetrofitServiceManager() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
        //  builder.writeTimeout(DEFAULT_READ_TIME_OUT,TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间

        // 添加公共参数拦截器
//        HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
//                //当前版本号，获取当前版本
//                .addHeaderParams("version","123")
//                //当前设备
//                .addHeaderParams("device", "android")
//                //时间戳+随机数
//                .addHeaderParams("timestamp", "45454545")
//                //包名,获取包名
//                .addHeaderParams("identifier", "124545")
//                //用户id
//                .addHeaderParams("customerUID","1231")
//                //用户token
//                .addHeaderParams("token","")
//                //商户ID
//                .addHeaderParams("merid","1231")
//                .build();
//        builder.addInterceptor(commonInterceptor);

        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();
    }

    private static class SingletonHolder {
        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    /**
     *  RetrofitServiceManager
     * @return
     */
    public  static  RetrofitServiceManager getInstance(){
        return  SingletonHolder.INSTANCE;
    }

    /**
     *  调用范例
     * service = RetrofitServiceManager.getInstance().create(xxxService.class);
     * @param service
     * @param <T>
     * @return
     */


    public <T> T create(Class<T> service){
        return  mRetrofit.create(service);
    }





}
