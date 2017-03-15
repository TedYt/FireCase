package com.hytera.fcls.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IMQConn;
import com.hytera.fcls.R;
import com.hytera.fcls.activity.FireCasePopActivity;
import com.hytera.fcls.activity.MainActivity;
import com.hytera.fcls.bean.FireCaseBean;
import com.hytera.fcls.bean.LoginResponseBean;
import com.hytera.fcls.mqtt.MQTT;
import com.hytera.fcls.mqtt.event.MessageEvent;
import com.hytera.fcls.presenter.HTTPPresenter;
import com.hytera.fcls.presenter.MPPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

/**
 * Created by Tim on 17/2/24.
 */

public class FireService extends Service implements IMQConn {

    public static final String TAG = "y20650" + FireService.class.getSimpleName();

    public static final String FIRE_ALARM_FILE = "firealarm.wav";

    public static String DEFAULT_NOTI_TITLE;
    public static String DEFAULT_NOTI_CONTENT;

    public static final int FIRE_NOTIFICATION = 119;

    private MQTT mqtt;

    @Override
    public void onCreate() {
        super.onCreate();

        DEFAULT_NOTI_TITLE = getResources().getString(R.string.default_noti_title);
        DEFAULT_NOTI_CONTENT = getResources().getString(R.string.default_noti_content);

        EventBus.getDefault().register(this); // 订阅消息总线

        mqtt = MQTT.getInstance();
        mqtt.setContext(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mqtt.startConnect(FireService.this); // 参数是为了传递IMQConn
            }
        }).start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        showNotification(null,null,true);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        stopForeground(true);
        EventBus.getDefault().unregister(this);// 取消消息总线
    }

    /**
     * EvenBus的 回调函数
     * 在后台接受消息, 有新警情时，会首先传到这里
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MessageEvent event){
        if (event  == null){
            Log.i(TAG, "EVENT is NULL");
            return;
        }

        if (event.getMqttMessage() == null) {
            Log.i(TAG, "mqtt message is NULL");
            return;
        }

        String msg = new String(event.getMqttMessage().getPayload());
        String title = "新警情";
        Log.i(TAG, "getMessage from MQ : "
                + ", topic is : " + event.getTopic()
                + "; message is : " + new String(event.getMqttMessage().getPayload()));
        // 分队预结束警情的消息，发给中队
        if (isPreFinishTopic(event)){
            Log.i(TAG, "这是预结束警情的消息，由中队处理");
            return;
        }

        // 结束警情的消息，发给分队
        if (isFinishTopic(event)){
            Log.i(TAG, "这是结束警情的消息，通知给分队");
            return;
        }

        Gson gson = new Gson();
        FireCaseBean fireCase = gson.fromJson(msg,FireCaseBean.class);

        // orgIdentifier 不相等的时候，不通知警情
        if (!isTheSameOrgIdentifier(fireCase)){
            Log.w(TAG, "orgIdentifier are not same, Don NOT accept the fire case!");
            return;
        }

        // 如果已有一个警情，就不再处理新警情
        if (DataUtil.haveOneCase()){
            Log.w(TAG, "Had a case, DO NOT accept new case!");
            return;
        }

        DataUtil.saveFireCaseBean(fireCase);
        Log.i(TAG, "got fire case bean");


        showCasePop(fireCase);
        showNotification(fireCase.getCompDeptName(), fireCase.getCaseDesc(), false);
        playFireAlarm();
        // 上报服务器，已收到警情，但是不一定接收处理
        postServerCopyCase();
    }

    /**
     * 结束警情主题的判断，通知给分队
     * 接到这个主题的消息，说明中队已结束了警情
     * @param event
     * @return
     */
    private boolean isFinishTopic(MessageEvent event) {
        if (mqtt.isPreFinishTopic(event.getTopic())){
            LoginResponseBean.UserBean userBean = DataUtil.getLoginUserBean();
            if (!DataUtil.isZhongDui(userBean.getOrgType())){
                return true;
            }
        }
        return false;
    }

    /**
     * 预结束警情主题的判断，通知给中队
     * 接到这个主题的消息，说明有分队申请结束警情
     * @param event
     * @return
     */
    private boolean isPreFinishTopic(MessageEvent event) {
        if (mqtt.isFinishTopic(event.getTopic())){
            LoginResponseBean.UserBean userBean = DataUtil.getLoginUserBean();
            if (DataUtil.isZhongDui(userBean.getOrgType())){
                return true;
            }
        }
        return false;
    }

    /**
     * 上报服务器，已收到警情，但是不一定接收处理
     */
    private void postServerCopyCase() {
        // 上报一个初始状态，说明已收到警情
        DataUtil.fireCaseState = DataUtil.CASE_STATE_INIT;
        String content = DataUtil.getStateURLContent(DataUtil.CASE_STATE_INIT);
        Log.i(TAG, "init Info : " + content);
        HTTPPresenter.post(DataUtil.FIRE_CASE_STATE_URL, content, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "state = 0, " + "arriveDest, response is " + response);
            }
        });
    }

    /**
     * 判断登录的 orgIdentifier 和警情的orgIdentifier 是否相等
     * 若相等，则通知警情；反之则不通知
     * @param fireCase
     * @return
     */
    private boolean isTheSameOrgIdentifier(FireCaseBean fireCase) {
        String org1 = fireCase.getOrgInfo().getOrgIdentifier();
        Log.i(TAG, "org1 = " + org1);
        if (org1 == null) return true; // 若为null，则认为是相等, 可以接受警情

        LoginResponseBean.UserBean bean = DataUtil.getLoginUserBean();
        String org2 = bean.getOrgIdentifier();
        Log.i(TAG, "org2 = " + org2);
        if (org2 != null){
            if (org1.equals(org2.substring(0,7))){
                return true;
            }
            Log.i(TAG, "The Login orgIdentifier is " + org2.substring(0,7)
                    + ", fire case orgIdentifier is " + org1);
            return false;
        }
        return true;
    }

    /**
     * 显示新警情
     * @param fireCase
     */
    private void showCasePop(FireCaseBean fireCase) {
        Intent intent = new Intent(this, FireCasePopActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DataUtil.EXTRA_FIRE_DESC, fireCase.getCaseDesc());// 警情描述
        bundle.putString(DataUtil.EXTRA_FIRE_LEVERL, fireCase.getCaseLevel());// 警情级别
        bundle.putString(DataUtil.EXTRA_FIRE_DEPR, fireCase.getCompDeptName());// 主管中队
        intent.putExtra("data", bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    /**
     * 响警报
     */
    private void playFireAlarm() {

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前音量
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

        AssetFileDescriptor fileDescriptor;
        try {
            fileDescriptor = getAssets().openFd(FIRE_ALARM_FILE);
            MediaPlayer mediaPlayer = MPPresenter.getInstance();

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                                            fileDescriptor.getStartOffset(),
                                            fileDescriptor.getLength());

            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示更新通知
     * @param title
     * @param msg
     */
    private void showNotification(String title, String msg, boolean sound) {
        if (title == null || title.isEmpty()){
            title = DEFAULT_NOTI_TITLE;
        }
        if (msg == null || msg.isEmpty()){
            msg = DEFAULT_NOTI_CONTENT;
        }

        Notification.Builder builder = new Notification.Builder(this);
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this,0,nfIntent,0))
                .setContentTitle(title) // 必填的属性
                .setContentText(msg) // 必填的属性
                .setSmallIcon(R.mipmap.ic_launcher96) // 必填的属性
                .setWhen(System.currentTimeMillis());

        Notification notification = builder.build();
        //notification.flags = Notification.FLAG_ONGOING_EVENT;
        if (sound){
            notification.defaults = Notification.DEFAULT_SOUND;
        }

        startForeground(FIRE_NOTIFICATION, notification);
    }

    /**
     * 链接成功之后，订阅主题
     */
    @Override
    public void MQConnSuccess() {
        mqtt.subcribeTopic();
    }

    @Override
    public void MQConnFailure() {

    }
}
