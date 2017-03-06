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

    public static final String FIRE_CASE_URL =
            "http://192.168.123.48:8080/icc_fcls/alarmStatus/reportStatus";

    /** 接收警情 */
    public static final int CASE_STATE_COPY = 1;
    /** 出发 */
    public static final int CASE_STATE_DEPART = 2;
    /** 确认到达 */
    public static final int CASE_STATE_ARRIVE = 3;
    /** 结束警情 */
    public static final int CASE_STATE_FINISH= 4;

    /** 记录警情状态 */
    public static int fireCaseState = 0;

    /**
     * 记录登录用户的信息
     * 供上报服务器用
     */
    private static LoginResponseBean loginResponseBean;

    public static void saveLoginResponseBean(LoginResponseBean bean){
        loginResponseBean = bean;
    }

    public static LoginResponseBean.UserBean getLoginUserBean(){
        if (loginResponseBean == null) return null;

        return loginResponseBean.getUser();
    }
}
