package com.hytera.fcls.bean;

/**
 * Created by Tim on 17/3/3.
 */

/**
 * 用于 接警，出发，到达，结束 状态的上报
 */
public class CaseStateBean extends BaseBean {

    /**
     * 接警,出发，到达，结束
     */
    private String state;

    /**
     * 状态改变时间
     */
    private long updatetime;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }
}
