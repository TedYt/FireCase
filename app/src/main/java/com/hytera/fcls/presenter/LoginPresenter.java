package com.hytera.fcls.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.hytera.fcls.ILogin;
import com.hytera.fcls.mqtt.bean.FireCaseBean;

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

    private static final String TAG = "y20650" + LoginPresenter.class.getSimpleName();
    private static final String LOGIN_XML = "Login";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CHECKED = "checked_remember";

    private ILogin iLogin;

    private Context context;

    private final String URL = "http://192.168.72.37:8080/HyteraBS/flow/gooFlow/query";

    public LoginPresenter(ILogin iLogin, Context context) {
        this.context = context;
        this.iLogin = iLogin;
    }

    public void Login(){
        Observable.just(URL)
                .map(new Function<String, Response>() {
                    @Override
                    public Response apply(String url) throws Exception {
                        OkHttpClient client = OKHTTPPresenter.getHttpClient();
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
                         if(!body.isEmpty()){
                            iLogin.LoginSuccess();
                            //response.body().string();
                            Log.i(TAG, "Response Message is :" + ", body is : " + body);
                            FireCaseBean bean = new FireCaseBean();
                            Gson gson = new Gson();
                            //gson.fromJson()
                        }else {
                            iLogin.LoginFailed();
                        }
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

    public void onCheckedChange(boolean checked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_XML,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_CHECKED, checked);
        editor.apply();
    }

    /**
     * 是否有选择 记住密码
     * @return
     */
    public boolean isCheckRemPas() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_XML,0);
        return sharedPreferences.getBoolean(KEY_CHECKED,false);
    }

    /**
     * 获取密码
     * @return
     */
    public String getPassword() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_XML,0);
        return sharedPreferences.getString(KEY_PASSWORD,"");
    }

    /**
     * 获取用户名
     * @return
     */
    public String getName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_XML,0);
        return sharedPreferences.getString(KEY_USERNAME,"");
    }

    /**
     * 保存密码
     * @param password
     */
    public void savePassword(String password) {
        if (password.isEmpty()) return;

        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_XML,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    /**
     * 保存用户名
     * @param name
     */
    public void saveName(String name) {
        if(name.isEmpty()) return;

        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_XML,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, name);
        editor.apply();
    }

}
