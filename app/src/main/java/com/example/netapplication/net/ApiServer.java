package com.example.netapplication.net;

import android.database.Observable;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * 请求APi
 */
public interface ApiServer {

    // 示例代码---
    @Headers({"Content_Type:application/json", "charset:UTF-8"})
    @GET("")
    Observable<BaseResult<String >> getlizi();


}
