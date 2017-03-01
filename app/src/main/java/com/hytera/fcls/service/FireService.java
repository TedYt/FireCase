package com.hytera.fcls.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hytera.fcls.IMQConn;
import com.hytera.fcls.R;
import com.hytera.fcls.activity.MainActivity;
import com.hytera.fcls.mqtt.MQTT;
import com.hytera.fcls.mqtt.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Tim on 17/2/24.
 */

public class FireService extends Service implements IMQConn {

    public static final String TAG = "y20650" + FireService.class.getSimpleName();

    public static final int FIRE_NOTIFICATION = 119;

    private MQTT mqtt;
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this); // 订阅消息总线


        mqtt = new MQTT(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mqtt.startConnect(FireService.this);
            }
        }).start();

        initMQTT();
        subscribeTopic();
    }

    private void subscribeTopic() {

    }

    private void initMQTT() {


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        Notification.Builder builder = new Notification.Builder(this);
        Intent nfIntent = new Intent(this, MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this,0,nfIntent,0))
                .setContentTitle("下拉列表中的Title") // 必填的属性
                .setContentText("要显示的内容") // 必填的属性
                .setSmallIcon(R.mipmap.ic_launcher) // 必填的属性
                .setWhen(System.currentTimeMillis());
        Notification notification = builder.build();
        //notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.defaults = Notification.DEFAULT_SOUND;

        startForeground(FIRE_NOTIFICATION, notification);
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
     * 在后台接受消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(MessageEvent event){
        Log.i(TAG, "getMessage from MQ : " + event.getString()
                + ", topic is : " + event.getTopic()
                + "message is : " + new String(event.getMqttMessage().getPayload()));
    }

    @Override
    public void MQConnSuccess() {
        mqtt.subcribeTopic();
    }

    @Override
    public void MQConnFailure() {

    }
}
