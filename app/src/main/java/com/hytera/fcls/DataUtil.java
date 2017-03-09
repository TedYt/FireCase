package com.hytera.fcls;

import android.util.Log;

import com.google.gson.Gson;
import com.hytera.fcls.bean.CaseStateBean;
import com.hytera.fcls.bean.FireCaseBean;
import com.hytera.fcls.bean.LoginResponseBean;

/**
 * Created by Tim on 17/3/4.
 */

public class DataUtil {
    public static final String LOGIN_XML = "Login";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CHECKED = "checked_remember";
    public static final String KEY_LOGINED = "login_or_not";// 标识是否已登录

    public static String FIRE_CASE_STATE_URL =
            "http://192.168.123.64:8080/icc_fcls/alarmStatus/reportStatus";

    public static String FIRE_CASE_IMG_URL =
            "http://192.168.123.64:8080/icc_fcls/media/save?";

    public static String LOGIN_URL =
            "http://192.168.123.64:8080/icc_fcls/system/login/doLogin?";
    // userCode=303798&password=123456

    /** 传递输入的key */
    public static final String EXTRA_FIRE_LEVERL = "fire_level";
    public static final String EXTRA_FIRE_DESC = "fire_desc";
    public static final String EXTRA_FIRE_DEPR = "fire_dept";

    /** 初始状态 */
    public static final int CASE_STATE_INIT = 0;
    /** 接收警情 */
    public static final int CASE_STATE_ACCEPT = 1;
    /** 出发 */
    public static final int CASE_STATE_DEPART = 2;
    /** 确认到达 */
    public static final int CASE_STATE_ARRIVE = 3;
    /** 结束警情 */
    public static final int CASE_STATE_FINISH= 4;
    /** 不接收警情 */
    public static final int CASE_STATE_REJECT= 5;

    /** 记录警情状态 */
    public static int fireCaseState = CASE_STATE_INIT;

    /** 是否接收了 */
    public static boolean acceptCase = false;

    public static boolean isAcceptCase() {
        return acceptCase;
    }

    public static void setAcceptCase(boolean acceptCase) {
        DataUtil.acceptCase = acceptCase;
    }

    /**
     * 记录登录用户的信息
     * 供上报服务器用
     */
    private static LoginResponseBean loginResponseBean = null;

    /**
     * 记录警情的信息
     * 供上报服务器用
     */
    private static FireCaseBean fireCaseBean = null;

    public static void saveLoginResponseBean(LoginResponseBean bean){
        loginResponseBean = bean;
    }
    public static void saveFireCaseBean(FireCaseBean bean){
        fireCaseBean = bean;
    }

    public static LoginResponseBean.UserBean getLoginUserBean(){
        if (loginResponseBean == null) return null;

        return loginResponseBean.getUser();
    }

    public static FireCaseBean getFireCaseBean(){
        if (fireCaseBean == null) return null;

        return fireCaseBean;
    }

    /**
     * 结束火情时，清除火情信息
     */
    public static void clearFireCase(){
        fireCaseBean = null;
    }

    /**
     * 是否已在处理一个警情
     * @return
     */
    public static boolean haveOneCase() {
        if (fireCaseBean != null ){
            return true;
        }
        return false;
    }
    /**
     * 设置上报警情状态改变的地址
     * @param ip
     */
    public static void setFireCaseStateURL(String ip){
        FIRE_CASE_STATE_URL = "http://"+ ip +":8080/icc_fcls/alarmStatus/reportStatus";
    }

    /**
     * 设置图片上传的地址
     * @param ip
     */
    public static void setImgPostURL(String ip){
        FIRE_CASE_IMG_URL = "http://"+ ip +":8080/fcls/media/save?";
    }

    /**
     * 测试方法 设置服务器端口
     * @param ip
     * @param port
     */
    public static void setServerIP(String ip, String port){
        Log.i("y20650", "setServerIP : " + ip + ", port : " + port);
        FIRE_CASE_IMG_URL = "http://" + ip + ":" + port + "/fcls/media/save?";
        FIRE_CASE_STATE_URL = "http://" + ip + ":" + port + "/icc_fcls/alarmStatus/reportStatus";
        LOGIN_URL = "http://" + ip + ":" + port + "/icc_fcls/system/login/doLogin?";
        Log.i("y20650", FIRE_CASE_IMG_URL + "\n"
                    + FIRE_CASE_STATE_URL + "\n"
                    + LOGIN_URL);
    }

    /**
     * 获取警情状态的json字符串
     * @param state
     * @return
     */
    public static String getStateInfo(int state) {
        CaseStateBean caseStateBean = new CaseStateBean();
        caseStateBean.setState(state);
        caseStateBean.setUpdatetime(System.currentTimeMillis());
        caseStateBean.setCaseID(DataUtil.getFireCaseBean().getGuid());

        LoginResponseBean.UserBean loginUserBean;
        CaseStateBean.UserBean userBean;
        try {
            loginUserBean = getLoginUserBean();
            userBean = new CaseStateBean.UserBean();
            userBean.setUserCode(loginUserBean.getUserCode());
            userBean.setToken(loginUserBean.getToken());
            userBean.setStaffName(loginUserBean.getStaffName());
            userBean.setOrgName(loginUserBean.getOrgName() == null ? "保安大队" : loginUserBean.getOrgName());
            userBean.setOrgGuid(loginUserBean.getOrgGuid() == null ? "1231212321" : loginUserBean.getOrgGuid());
        }catch (NullPointerException e){
            userBean = new CaseStateBean.UserBean();
            userBean.setUserCode("303798");
            userBean.setToken("1E5CA34FC811430FBD401CF2187C81C1");
            userBean.setStaffName("张大安");
            userBean.setOrgName("新安大队");
            userBean.setOrgGuid("1234");
        }

        caseStateBean.setUserBean(userBean);
        Gson gson = new Gson();
        return gson.toJson(caseStateBean);
    }

    /**
     * 获取URL对应的state参数
     * @param state
     * @return
     */
    public static String getStateURLContent(int state){
        String json = getStateInfo(state);
        String token = getLoginUserBean().getToken();
        return "jsonStr=" + json + "&" + "token=" + token;
    }

    /**
     * 火警级别的文字描述
     * @param s
     * @return
     */
    public static String getLevelDesc(String s) {
        int level = Integer.valueOf(s);
        switch (level){
            case 1:
                return "一级火警";
            case 2:
                return "二级火警";
            case 3:
                return "三级火警";
            case 4:
                return "四级火警";
            case 5:
                return "五级火警";
        }
        return "无效级别";
    }
}
