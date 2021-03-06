package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/21.
 */

public class RtmpVideoBean {

    /**
     * url : rtmp://XXX
     * user : {"userCode":"303798","staffName":"李世兴","orgGuid":null,"orgIdentifier":null,"orgName":null,"loginTime":"2017-03-03 15:37:03","ip":"192.168.123.76","token":"1E5CA34FC811430FBD401CF2187C81C1"}
     */

    private String url;
    private UserBean user;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
         * orgGuid : null 组织结构代码，用于判断是哪个机构
         * orgIdentifier : null
         * orgName : null
         * loginTime : 2017-03-03 15:37:03
         * ip : 192.168.123.76
         * token : 1E5CA34FC811430FBD401CF2187C81C1
         * orgType : 0 大队，1 中队，2 大分队，3 小分队 4 微站
         */

        private String userCode;
        private String staffName;
        private String orgGuid;
        private String orgIdentifier;
        private String orgName;
        private String loginTime;
        private String ip;
        private String token; // 用户的访问凭证，上报服务器要带有
        private String orgType;

        public String getOrgType() {
            return orgType;
        }

        public void setOrgType(String orgType) {
            this.orgType = orgType;
        }

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

        public String getOrgGuid() {
            return orgGuid;
        }

        public void setOrgGuid(String orgGuid) {
            this.orgGuid = orgGuid;
        }

        public String getOrgIdentifier() {
            return orgIdentifier;
        }

        public void setOrgIdentifier(String orgIdentifier) {
            this.orgIdentifier = orgIdentifier;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
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
