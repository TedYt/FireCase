package com.hytera.fcls.presenter;

import android.media.MediaPlayer;
import android.util.Log;

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IFireCasePop;
import com.hytera.fcls.activity.FireCasePopActivity;
import com.hytera.fcls.comutil.GpsUtil;

/**
 * Created by Tim on 17/3/6.
 */

public class FireCasePopPresenter {

    public static final String TAG = "y20650" + "CasePopPresenter";

    private IFireCasePop iFireCasePop;

    private FireCasePopActivity context;

    public FireCasePopPresenter(IFireCasePop iFireCasePop, FireCasePopActivity context) {
        this.iFireCasePop = iFireCasePop;
        this.context = context;
    }

    /**
     * 接收警情
     */
    public void acceptCase() {
        //接收警情开始定位
        GpsUtil.startLocation();
        iFireCasePop.showMainActivity();
        stopFireAlarm();

        // 上报状态
        /*if (DataUtil.fireCaseState != DataUtil.CASE_STATE_FINISH ||
                DataUtil.fireCaseState != DataUtil.CASE_STATE_INIT){
            Log.w(TAG, "last state is not init or finish");
            Toast.makeText(context, "请先结束警情", Toast.LENGTH_SHORT).show();
            return;
        }*/
        DataUtil.setAcceptCase(true);
        DataUtil.fireCaseState = DataUtil.CASE_STATE_ACCEPT;
        String content = DataUtil.getStateURLContent(DataUtil.CASE_STATE_ACCEPT);
        Log.i(TAG, "acceptCase case Info : " + content);
        HTTPPresenter.post(DataUtil.FIRE_CASE_STATE_URL, content, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "arriveDest, response is " + response);
            }
        });
    }

    /**
     * 不接收警情
     */
    public void rejectCase() {
        DataUtil.setAcceptCase(false);
        Log.i(TAG, "reject copy case : " + DataUtil.getFireCaseBean().getGuid());
        String content = DataUtil.getStateURLContent(DataUtil.CASE_STATE_REJECT);
        HTTPPresenter.post(DataUtil.FIRE_CASE_STATE_URL, content, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response is " + response);
            }
        });

        DataUtil.clearFireCase();

        iFireCasePop.closeActivity();
        stopFireAlarm();
    }

    private void stopFireAlarm() {
        MediaPlayer mediaPlayer = MPPresenter.getInstance();
        //if (mediaPlayer.isPlaying()){
        mediaPlayer.stop();
        //}
    }
}
