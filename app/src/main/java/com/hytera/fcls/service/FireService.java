package com.hytera.fcls.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.hytera.fcls.IMQConn;
import com.hytera.fcls.R;
import com.hytera.fcls.activity.MainActivity;
import com.hytera.fcls.bean.FireCaseBean;
import com.hytera.fcls.mqtt.MQTT;
import com.hytera.fcls.mqtt.event.MessageEvent;
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
            return;
        }

        String msg = new String(event.getMqttMessage().getPayload());
        String title = "新警情";
        Log.i(TAG, "getMessage from MQ : "
                + ", topic is : " + event.getTopic()
                + "; message is : " + new String(event.getMqttMessage().getPayload()));

        Gson gson = new Gson();
        FireCaseBean fireCase = gson.fromJson(msg,FireCaseBean.class);
        if (!copyThisCase(fireCase)) return;
        showNotification(fireCase.getCompDeptName(), fireCase.getCaseDesc(), false);

        playFireAlarm();
    }

    private boolean copyThisCase(FireCaseBean fireCase) {
        return true;
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
                .setSmallIcon(R.drawable.icon) // 必填的属性
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
