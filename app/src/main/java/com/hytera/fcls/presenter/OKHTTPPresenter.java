package com.hytera.fcls.presenter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Tim on 17/3/2.
 */

public class OKHTTPPresenter {

    private static OkHttpClient _instance;

    public static OkHttpClient getHttpClient(){
        if (_instance == null){
            synchronized (OKHTTPPresenter.class){
                if (_instance == null){
                    _instance = new OkHttpClient.Builder()
                                    .readTimeout(10, TimeUnit.SECONDS)
                                    .build();
                }
            }
        }
        return _instance;
    }
}
