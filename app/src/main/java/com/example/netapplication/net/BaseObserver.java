package com.example.netapplication.net;

import android.util.Log;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2021/6/2
 * Describe ：
 * https://blog.csdn.net/fugang1230/article/details/81207202?utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.control
 */

public class BaseObserver<T> implements Observer<BaseResult<T>> {

    private ResponseCallBack<T> responseCallBack;
    private ProgressListener progressListener;
    private Disposable disposable;

    public BaseObserver(ResponseCallBack<T> responseCallBack, ProgressListener progressListener){
        this.responseCallBack = responseCallBack;
        this.progressListener = progressListener;
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {
               this.disposable = d;
        if (progressListener != null){
            progressListener.startProgress();
        }
    }


    @Override
    public void onNext(@NonNull BaseResult<T> tBaseResult) {

        if (tBaseResult.getCode()== 200){
            responseCallBack.onSuccess(tBaseResult.getData());
        }else {
            responseCallBack.onFault(tBaseResult.getMessage());
        }

    }





    @Override
    public void onError(@NonNull Throwable e) {
           Log.d("tag555",e.getMessage());
        try {

            if (e instanceof SocketTimeoutException) {//请求超时
                responseCallBack.onFault("请求超时,请稍后再试");
            } else if (e instanceof ConnectException) {//网络连接超时
                responseCallBack.onFault("网络连接超时,请检查网络状态");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                responseCallBack.onFault("安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    responseCallBack.onFault("网络异常，请检查您的网络状态");
                } else if (code == 404) {
                    responseCallBack.onFault("请求的地址不存在");
                } else {
                    responseCallBack.onFault("请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                responseCallBack.onFault("域名解析失败");
            } else {
                responseCallBack.onFault("error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            Log.e("OnSuccessAndFaultSub", "error:" + e.getMessage());
            if (disposable !=null && !disposable.isDisposed()){ //事件完成取消订阅
                disposable.dispose();
            }
            if (progressListener!=null){
                progressListener.cancelProgress();
            }
        }
    }

    @Override
    public void onComplete() {
        if (disposable !=null && !disposable.isDisposed()){ //事件完成取消订阅
            disposable.dispose();
        }
        if (progressListener!=null){
            progressListener.cancelProgress();
        }
    }
}
