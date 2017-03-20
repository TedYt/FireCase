package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/3.
 */

/**
 * 消防警情的详细信息
 */
public class FireCaseBean {

    /**
     * acceptDeptId : BFFC553C2024496D8DA981B71C86FC30
     * answeringTime : 1489044407491
     * callingTime : 1489044407491
     * caseDefine : DIC007001
     * caseDesc : 宝安区福永大洋田中粮福安机器人产业园15栋4楼被困电梯13380333115
     * caseDispatchState : 2
     * caseLevel : 1
     * caseState : DIC001002
     * caseTime : 1489044407491
     * caseType : DIC008004
     * compDeptName : 福永中队
     * contactNO :
     * createTime : 1489044407491
     * delFlag : 0
     * dispatchDeptId : BFFC553C2024496D8DA981B71C86FC30
     * dispatchRecord : {"biztype":"1","caseid":"20170309152647489","createtime":1489044407587,"createuser":"system","delflag":"0","dispatcher":"system","dispatchorgan":"system","dispatchorglevel":"1","dispatchway":"0","guid":"352f0247-c10b-452b-b831-14b31916cca0","policetype":"4","receiveno":"BFFC553C2024496D8DA981B71C86FC30","receiver":"福永中队","sourcetype":"3"}
     * guid : 20170309152647489
     * important : false
     * manualPot : false
     * mapx : 113.801231
     * mapy : 22.69247
     * orgInfo : {"busessTypeStr":"","contact":"吴晓斌","contactno":"13825269817","createTime":1488544302000,"createUser":"admin","enableFlag":1,"flag":0,"guid":"BFFC553C2024496D8DA981B71C86FC30","latitude":22.681493,"longitude":113.821785,"orgIdentifier":"000-006","orgName":"福永中队","orgType":"1","orgTypeStr":"中队","parentOrgGuid":"1CDFAE9460CB4F8E81435F572E09F9D2","seq":5,"updateTime":1488605457000,"updateUser":"ycs"}
     * processDeptId : BFFC553C2024496D8DA981B71C86FC30
     * repeat : false
     * sendAllMsg : false
     * updateTime : 1489044407491
     */

    private String acceptDeptId;
    private long answeringTime;
    private long callingTime;
    private String caseDefine;
    private String caseDesc;
    private String caseDispatchState;
    private String caseLevel;
    private String caseState;
    private long caseTime;
    private String caseType;
    private String compDeptName;
    private String contactNO;
    private long createTime;
    private String delFlag;
    private String dispatchDeptId;
    private DispatchRecordBean dispatchRecord;
    private String guid;
    private boolean important;
    private boolean manualPot;
    private double mapx;
    private double mapy;
    private OrgInfoBean orgInfo;
    private String processDeptId;
    private boolean repeat;
    private boolean sendAllMsg;
    private long updateTime;

    public String getAcceptDeptId() {
        return acceptDeptId;
    }

    public void setAcceptDeptId(String acceptDeptId) {
        this.acceptDeptId = acceptDeptId;
    }

    public long getAnsweringTime() {
        return answeringTime;
    }

    public void setAnsweringTime(long answeringTime) {
        this.answeringTime = answeringTime;
    }

    public long getCallingTime() {
        return callingTime;
    }

    public void setCallingTime(long callingTime) {
        this.callingTime = callingTime;
    }

    public String getCaseDefine() {
        return caseDefine;
    }

    public void setCaseDefine(String caseDefine) {
        this.caseDefine = caseDefine;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public String getCaseDispatchState() {
        return caseDispatchState;
    }

    public void setCaseDispatchState(String caseDispatchState) {
        this.caseDispatchState = caseDispatchState;
    }

    public String getCaseLevel() {
        return caseLevel;
    }

    public void setCaseLevel(String caseLevel) {
        this.caseLevel = caseLevel;
    }

    public String getCaseState() {
        return caseState;
    }

    public void setCaseState(String caseState) {
        this.caseState = caseState;
    }

    public long getCaseTime() {
        return caseTime;
    }

    public void setCaseTime(long caseTime) {
        this.caseTime = caseTime;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCompDeptName() {
        return compDeptName;
    }

    public void setCompDeptName(String compDeptName) {
        this.compDeptName = compDeptName;
    }

    public String getContactNO() {
        return contactNO;
    }

    public void setContactNO(String contactNO) {
        this.contactNO = contactNO;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDispatchDeptId() {
        return dispatchDeptId;
    }

    public void setDispatchDeptId(String dispatchDeptId) {
        this.dispatchDeptId = dispatchDeptId;
    }

    public DispatchRecordBean getDispatchRecord() {
        return dispatchRecord;
    }

    public void setDispatchRecord(DispatchRecordBean dispatchRecord) {
        this.dispatchRecord = dispatchRecord;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isManualPot() {
        return manualPot;
    }

    public void setManualPot(boolean manualPot) {
        this.manualPot = manualPot;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public OrgInfoBean getOrgInfo() {
        return orgInfo;
    }

    public void setOrgInfo(OrgInfoBean orgInfo) {
        this.orgInfo = orgInfo;
    }

    public String getProcessDeptId() {
        return processDeptId;
    }

    public void setProcessDeptId(String processDeptId) {
        this.processDeptId = processDeptId;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isSendAllMsg() {
        return sendAllMsg;
    }

    public void setSendAllMsg(boolean sendAllMsg) {
        this.sendAllMsg = sendAllMsg;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public static class DispatchRecordBean {
        /**
         * biztype : 1 初次调派 2 增援 3 重新调派 4 预案调派
         * caseid : 20170309152647489
         * createtime : 1489044407587
         * createuser : system
         * delflag : 0
         * dispatcher : system
         * dispatchorgan : system
         * dispatchorglevel : 1， 4 中队 5，中队以下 调派级别
         * dispatchway : 0
         * guid : 352f0247-c10b-452b-b831-14b31916cca0
         * policetype : 4
         * receiveno : BFFC553C2024496D8DA981B71C86FC30
         * receiver : 福永中队
         * sourcetype : 3
         */

        private String biztype;
        private String caseid;
        private long createtime;
        private String createuser;
        private String delflag;
        private String dispatcher;
        private String dispatchorgan;
        private String dispatchorglevel;
        private String dispatchway;
        private String guid;
        private String policetype;
        private String receiveno;
        private String receiver;
        private String sourcetype;

        public String getBiztype() {
            return biztype;
        }

        public void setBiztype(String biztype) {
            this.biztype = biztype;
        }

        public String getCaseid() {
            return caseid;
        }

        public void setCaseid(String caseid) {
            this.caseid = caseid;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getCreateuser() {
            return createuser;
        }

        public void setCreateuser(String createuser) {
            this.createuser = createuser;
        }

        public String getDelflag() {
            return delflag;
        }

        public void setDelflag(String delflag) {
            this.delflag = delflag;
        }

        public String getDispatcher() {
            return dispatcher;
        }

        public void setDispatcher(String dispatcher) {
            this.dispatcher = dispatcher;
        }

        public String getDispatchorgan() {
            return dispatchorgan;
        }

        public void setDispatchorgan(String dispatchorgan) {
            this.dispatchorgan = dispatchorgan;
        }

        public String getDispatchorglevel() {
            return dispatchorglevel;
        }

        public void setDispatchorglevel(String dispatchorglevel) {
            this.dispatchorglevel = dispatchorglevel;
        }

        public String getDispatchway() {
            return dispatchway;
        }

        public void setDispatchway(String dispatchway) {
            this.dispatchway = dispatchway;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getPolicetype() {
            return policetype;
        }

        public void setPolicetype(String policetype) {
            this.policetype = policetype;
        }

        public String getReceiveno() {
            return receiveno;
        }

        public void setReceiveno(String receiveno) {
            this.receiveno = receiveno;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getSourcetype() {
            return sourcetype;
        }

        public void setSourcetype(String sourcetype) {
            this.sourcetype = sourcetype;
        }
    }

    public static class OrgInfoBean {
        /**
         * busessTypeStr :
         * contact : 吴晓斌
         * contactno : 13825269817
         * createTime : 1488544302000
         * createUser : admin
         * enableFlag : 1
         * flag : 0
         * guid : BFFC553C2024496D8DA981B71C86FC30
         * latitude : 22.681493
         * longitude : 113.821785
         * orgIdentifier : 000-006
         * orgName : 福永中队
         * orgType : 1
         * orgTypeStr : 中队
         * parentOrgGuid : 1CDFAE9460CB4F8E81435F572E09F9D2
         * seq : 5
         * updateTime : 1488605457000
         * updateUser : ycs
         */

        private String busessTypeStr;
        private String contact;
        private String contactno;
        private long createTime;
        private String createUser;
        private int enableFlag;
        private int flag;
        private String guid;
        private double latitude;
        private double longitude;
        private String orgIdentifier;
        private String orgName;
        private String orgType;
        private String orgTypeStr;
        private String parentOrgGuid;
        private int seq;
        private long updateTime;
        private String updateUser;

        public String getBusessTypeStr() {
            return busessTypeStr;
        }

        public void setBusessTypeStr(String busessTypeStr) {
            this.busessTypeStr = busessTypeStr;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getContactno() {
            return contactno;
        }

        public void setContactno(String contactno) {
            this.contactno = contactno;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public int getEnableFlag() {
            return enableFlag;
        }

        public void setEnableFlag(int enableFlag) {
            this.enableFlag = enableFlag;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
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

        public String getOrgType() {
            return orgType;
        }

        public void setOrgType(String orgType) {
            this.orgType = orgType;
        }

        public String getOrgTypeStr() {
            return orgTypeStr;
        }

        public void setOrgTypeStr(String orgTypeStr) {
            this.orgTypeStr = orgTypeStr;
        }

        public String getParentOrgGuid() {
            return parentOrgGuid;
        }

        public void setParentOrgGuid(String parentOrgGuid) {
            this.parentOrgGuid = parentOrgGuid;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }
    }
}
