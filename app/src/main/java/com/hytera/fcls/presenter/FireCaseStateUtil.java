package com.hytera.fcls.presenter;

import com.hytera.fcls.DataUtil;

/**
 * Created by Tim on 17/3/8.
 */

public class FireCaseStateUtil {

    public static boolean isInit(){
        if (DataUtil.fireCaseState == DataUtil.CASE_STATE_INIT){
            return true;
        }
        return false;
    }

    /**
     * 前一个状态是否是 接收警情
     * @return
     */
    public static boolean lastStateIsCopy(){
        if (DataUtil.fireCaseState == DataUtil.CASE_STATE_ACCEPT){
            return true;
        }
        return false;
    }

    /**
     * 当前是出发状态
     * @return
     */
    public static boolean isDepartState(){
        if (DataUtil.fireCaseState ==  DataUtil.CASE_STATE_DEPART){
            return true;
        }
        return false;
    }

    /**
     * 已经进入出发的下一个状态
     * @return
     */
    public static boolean hasDeparted(){
        if (DataUtil.fireCaseState > DataUtil.CASE_STATE_DEPART &&
                DataUtil.fireCaseState <= DataUtil.CASE_STATE_PRE_FINISH &&
                DataUtil.fireCaseState != DataUtil.CASE_STATE_REJECT){ // 由于变量值得设计没有考虑好，后期无法改变了
            return true;
        }
        return false;
    }

    /**
     * 前一个状态是否是 出发
     * @return
     */
    public static boolean lastStateIsDepart(){
        if (DataUtil.fireCaseState == DataUtil.CASE_STATE_DEPART){
            return true;
        }
        return false;
    }

    /**
     * 前一个状态是否是 到达
     * @return
     */
    public static boolean lastStateIsArrive(){
        if (DataUtil.fireCaseState == DataUtil.CASE_STATE_ARRIVE){
            return true;
        }
        return false;
    }

    /**
     * 前一个状态是否是 结束
     * @return
     */
    public static boolean lastStateIsFinish(){
        if (DataUtil.fireCaseState == DataUtil.CASE_STATE_FINISH){
            return true;
        }
        return false;
    }
}
