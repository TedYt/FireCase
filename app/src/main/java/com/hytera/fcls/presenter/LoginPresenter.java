package com.hytera.fcls.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.hytera.fcls.mqtt.bean.FireCaseBean;
import com.hytera.fcls.ILogin;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Tim on 17/2/25.
 */

public class LoginPresenter {

    public static final String TAG = "y20650" + LoginPresenter.class.getSimpleName();

    private ILogin iLogin;

    private static OkHttpClient client;

    private final String URL = "http://192.168.72.37:8080/HyteraBS/flow/gooFlow/query";

    public LoginPresenter(ILogin iLogin) {
        this.iLogin = iLogin;
        client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public static OkHttpClient getHttpClient(){
        if (client != null){
            return client;
        }
        client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        return client;
    }

    public void Login(){
        Observable.just(URL)
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(String url) throws Exception {
                        Request request = new Request.Builder().url(url).build();
                        return client.newCall(request).execute();
                    }
                })
                .map(new Function<Response, String>() {
                    @Override
                    public String apply(Response response) throws Exception {
                        if (response.isSuccessful()){
                            return response.body().string();
                        }else {
                            return "";
                        }
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String body) throws Exception {
                        // (!body.isEmpty()){
                            iLogin.LoginSuccess();
                            //response.body().string();
                            Log.i(TAG, "Response Message is :" + ", body is : " + body);
                            FireCaseBean bean = new FireCaseBean();
                            Gson gson = new Gson();
                            //gson.fromJson()
                        //}else {
                        //    iLogin.LoginFailed();
                        //}
                    }
                });

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                //String url = "http://www.baidu.com";
                try {
                    Request request = new Request.Builder().url(URL).build();
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()){
                        iLogin.LoginSuccess();
                        //response.body().string();
                        Log.i(TAG, "Response Message is :" + response.message()
                                + ", body is : " + response.body().string());
                    }else {
                        iLogin.LoginFailed();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */
    }

}
