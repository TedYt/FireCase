package com.hytera.fcls.presenter;

import android.content.SharedPreferences;
import com.hytera.fcls.comutil.Log;

import com.google.gson.Gson;
import com.hytera.fcls.DataUtil;
import com.hytera.fcls.ILogin;
import com.hytera.fcls.TestClass;
import com.hytera.fcls.activity.LoginActivity;
import com.hytera.fcls.bean.LoginResponseBean;

/**
 * Created by Tim on 17/2/25.
 */

public class LoginPresenter {

    private static final String TAG = DataUtil.BASE_TAG + LoginPresenter.class.getSimpleName();

    private ILogin iLogin;

    private LoginActivity context;

    public LoginPresenter(ILogin iLogin, LoginActivity context) {
        this.context = context;
        this.iLogin = iLogin;
    }

    public void Login(String password, String username) {
        String content = "userCode=" + username + "&" + "password=" + password + "&"
                + "remark=MOBILE";
        HTTPPresenter.post(DataUtil.LOGIN_URL, content, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response is : " + response);
                Gson gson = new Gson();
                LoginResponseBean bean = gson.fromJson(response, LoginResponseBean.class);
                if (null == bean) { // 测试数据
                    bean = TestClass.getTestLoginBean();
                }
                if (response == null || bean.getUser() == null) {
                    context.runOnUiThread(new LoginFailureRunnable());
                } else {
                    setLogined(true);
                    DataUtil.saveLoginResponseBean(bean);
                    saveStaffName(bean);
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

    private void saveStaffName(LoginResponseBean bean) {
        String staff_name = bean.getUser().getStaffName();
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DataUtil.KEY_STAFFNAME, staff_name);
        editor.apply();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getStaffName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        return sharedPreferences.getString(DataUtil.KEY_STAFFNAME, "amdin");
    }

    public void onCheckedChange(boolean checked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DataUtil.KEY_CHECKED, checked);
        editor.apply();
    }

    /**
     * 是否有选择 记住密码
     *
     * @return
     */
    public boolean isCheckRemPas() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        return sharedPreferences.getBoolean(DataUtil.KEY_CHECKED, false);
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        return sharedPreferences.getString(DataUtil.KEY_PASSWORD, "");
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getUserCode() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        return sharedPreferences.getString(DataUtil.KEY_USERCODE, "");
    }

    /**
     * 保存密码
     *
     * @param password
     */
    public void savePassword(String password) {
        if (password.isEmpty()) return;

        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DataUtil.KEY_PASSWORD, password);
        editor.apply();
    }

    /**
     * 保存用户名
     *
     * @param name
     */
    public void saveUserCode(String name) {
        if (name.isEmpty()) return;

        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DataUtil.KEY_USERCODE, name);
        editor.apply();
    }

    /**
     * 设置已登录
     */
    public void setLogined(boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DataUtil.KEY_LOGINED, b);
        editor.apply();
    }

    /**
     * 是否 已登录
     *
     * @return
     */
    public boolean isLogined() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
        return sharedPreferences.getBoolean(DataUtil.KEY_LOGINED, false);
    }


    /**
     * 供主线程调用
     */
    private class LoginFailureRunnable implements Runnable {

        @Override
        public void run() {
            iLogin.LoginFailed();
        }
    }

    /**
     * 供主线程调用
     */
    private class LoginSuccessRunnable implements Runnable {

        @Override
        public void run() {
            iLogin.LoginSuccess();
        }
    }


}
