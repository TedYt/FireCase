package com.hytera.fcls.presenter;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.hytera.fcls.ILogin;
import com.hytera.fcls.activity.LoginActivity;

/**
 * Created by Tim on 17/2/25.
 */

public class LoginPresenter {

    private static final String TAG = "y20650" + LoginPresenter.class.getSimpleName();

    private static final String LOGIN_XML = "Login";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CHECKED = "checked_remember";

    private static final int LOGIN_STATE_SUCCESS = 0;
    private static final int LOGIN_STATE_FAILED = 1;

    private ILogin iLogin;

    private LoginActivity context;

    private final String URL =
            "http://192.168.72.37:8080/fcls/system/login/doLogin?";
    // userCode=303798&password=123456
    private final String URL_FOR_GET =
            "http://192.168.72.37:8080/fcls/system/login/doLogin?userCode=303798&password=123456";

    public LoginPresenter(ILogin iLogin, LoginActivity context) {
        this.context = context;
        this.iLogin = iLogin;
    }

    public void Login(String password, String username){
        String content = "userCode=" + username + "&" + "password=" + password;
        HTTPPresenter.post(URL, content, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response is : " + response);
                Gson gson = new Gson();
                LoginResponseBean bean = gson.fromJson(response, LoginResponseBean.class);
                if (bean.getUser() == null){
                    context.runOnUiThread(new LoginFailureRunnable());
                }else {
                    context.runOnUiThread(new LoginSuccessRunnable());
                }
            }
        });

        /*HTTPPresenter.get(URL_FOR_GET, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                if (response == null || response.isEmpty()){
                    context.runOnUiThread(new LoginFailureRunnable());
                    return;
                }
                Log.i(TAG, "response is : " + response);

                context.runOnUiThread(new LoginSuccessRunnable());
            }
        });*/

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



    private class LoginFailureRunnable implements Runnable{

        @Override
        public void run() {
            iLogin.LoginFailed();
        }
    }

    private class LoginSuccessRunnable implements Runnable{

        @Override
        public void run() {
            iLogin.LoginSuccess();
        }
    }
}
