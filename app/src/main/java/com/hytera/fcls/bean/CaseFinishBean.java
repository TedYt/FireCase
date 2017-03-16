package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/15.
 *
 * 预结束警情和结束警情相关的数据
 */

public class CaseFinishBean {

    private String caseID; // 警情ID
    private String orgName; // 组织结构的名字，例如宝安中队，新安分队

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
