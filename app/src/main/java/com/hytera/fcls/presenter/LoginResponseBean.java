package com.hytera.fcls.presenter;

/**
 * Created by Tim on 17/3/3.
 */

public class LoginResponseBean {

    /**
     * msg : success
     * user : {"userCode":"303798","staffName":"李世兴","orgGuid":null,"orgIdentifier":null,"orgName":null,"loginTime":"2017-03-03 15:37:03","ip":"192.168.123.76","token":"1E5CA34FC811430FBD401CF2187C81C1"}
     */

    private String msg;
    private UserBean user;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * userCode : 303798
         * staffName : 李世兴
         * orgGuid : null
         * orgIdentifier : null
         * orgName : null
         * loginTime : 2017-03-03 15:37:03
         * ip : 192.168.123.76
         * token : 1E5CA34FC811430FBD401CF2187C81C1
         */

        private String userCode;
        private String staffName;
        private Object orgGuid;
        private Object orgIdentifier;
        private Object orgName;
        private String loginTime;
        private String ip;
        private String token;

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public Object getOrgGuid() {
            return orgGuid;
        }

        public void setOrgGuid(Object orgGuid) {
            this.orgGuid = orgGuid;
        }

        public Object getOrgIdentifier() {
            return orgIdentifier;
        }

        public void setOrgIdentifier(Object orgIdentifier) {
            this.orgIdentifier = orgIdentifier;
        }

        public Object getOrgName() {
            return orgName;
        }

        public void setOrgName(Object orgName) {
            this.orgName = orgName;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
