package com.hytera.fcls.presenter;

import android.media.MediaPlayer;
import android.util.Log;

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IFireCasePop;
import com.hytera.fcls.activity.FireCasePopActivity;

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
    public void copyCase() {

        iFireCasePop.showMainActivity();
        stopFireAlarm();

        // 上报状态
        /*if (DataUtil.fireCaseState != DataUtil.CASE_STATE_FINISH ||
                DataUtil.fireCaseState != DataUtil.CASE_STATE_INIT){
            Log.w(TAG, "last state is not init or finish");
            Toast.makeText(context, "请先结束警情", Toast.LENGTH_SHORT).show();
            return;
        }*/

        DataUtil.fireCaseState = DataUtil.CASE_STATE_COPY;
        String copyInfo = DataUtil.getStateInfo(DataUtil.CASE_STATE_COPY);
        Log.i(TAG, "copyCase case Info : " + copyInfo);
        HTTPPresenter.post(DataUtil.FIRE_CASE_STATE_URL, "jsonStr=" + copyInfo, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "arriveDest, response is " + response);
            }
        });
    }

    /**
     * 不接收警情
     */
    public void rejectCopy() {
        Log.i(TAG, "reject copy case : " + DataUtil.getFireCaseBean().getGuid());
        final String rejectInfo = DataUtil.getStateInfo(DataUtil.CASE_STATE_REJECT);
        HTTPPresenter.post(DataUtil.FIRE_CASE_STATE_URL, "jsonStr=" + rejectInfo, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "response is " + response);
            }
        });

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
