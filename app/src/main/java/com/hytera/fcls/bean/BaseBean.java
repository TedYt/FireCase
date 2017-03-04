package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/3.
 */

public class BaseBean {

    /**
     * 警情ID : 标识唯一警情
     */
    public String caseID;

    /**
     * 警员身份
     */
    public UserBean userBean;

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public static class UserBean {
        /**
         * userCode : 303798
         * staffName : 李世兴
         * orgGuid : null
         * orgName : null
         * token：1E5CA34FC811430FBD401CF2187C81C1
         */

        private String userCode;
        private String staffName;
        private String orgGuid;
        private String orgName;
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

        public Object getOrgGuid() {
            return orgGuid;
        }

        public void setOrgGuid(String orgGuid) {
            this.orgGuid = orgGuid;
        }

        public Object getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }
    }
}
