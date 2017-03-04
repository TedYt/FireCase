package com.hytera.fcls;

import com.hytera.fcls.bean.LoginResponseBean;

/**
 * Created by Tim on 17/3/4.
 */

public class DataUtil {
    public static final String LOGIN_XML = "Login";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CHECKED = "checked_remember";

    private static LoginResponseBean loginResponseBean;

    public static void saveLoginResponseBean(LoginResponseBean bean){
        loginResponseBean = bean;
    }

    public static LoginResponseBean.UserBean getLoginedUserBean(){
        return loginResponseBean.getUser();
    }
}
