package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/3.
 */

/**
 * 消防警情的详细信息
 */
public class FireCaseBean {

    /**
     * caseDesc : 宝安区福永大洋田中粮福安机器人产业园15栋4楼被困电梯13380333115
     * caseDispatchState : 2
     * caseLevel : 1
     * caseTime : 1487577365000
     * caseType : 2
     * compDeptName : 福永中队
     * contactNO :
     * createTime : 1487577305000
     * guid : 110_S2017022010054_00Fri Mar 03 20:36:50 CST 2017
     * important : false
     * manualPot : false
     * mapx : 113.801231
     * mapy : 22.69247
     * repeat : false
     * sendAllMsg : false
     * orgIdentifier : null. 与登录的 orgIdentifier 匹配，确定是哪个中队负责该警情
     */

    private String caseDesc;
    private String caseDispatchState;
    private String caseLevel;
    private long caseTime;
    private String caseType;
    private String compDeptName;
    private String contactNO;
    private long createTime;
    private String guid;
    private boolean important;
    private boolean manualPot;
    private double mapx;
    private double mapy;
    private boolean repeat;
    private boolean sendAllMsg;

    public String getOrgIdentifier() {
        return orgIdentifier;
    }

    public void setOrgIdentifier(String orgIdentifier) {
        this.orgIdentifier = orgIdentifier;
    }

    private String orgIdentifier;

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
}
