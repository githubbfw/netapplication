package com.example.netapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.netapplication.net.ApiServer;
import com.example.netapplication.net.BaseObserver;
import com.example.netapplication.net.ProgressListener;
import com.example.netapplication.net.ResponseCallBack;
import com.example.netapplication.net.RetrofitServiceManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2021/6/3
 * Describe ：
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      // 例子；
        RetrofitServiceManager.getInstance().create(ApiServer.class)
                .getlizi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>(new ResponseCallBack<String>() {
                    @Override
                    public void onSuccess(String string) {
                        //登录成功，保存用户信息
                        Log.i("tag",string.toString());
                    }

                    @Override
                    public void onFault(String errorMsg) {

                    }
                }, new ProgressListener() {
                    @Override
                    public void startProgress() {

                    }

                    @Override
                    public void cancelProgress() {

                    }
                }));


    }
}
