package com.hytera.fcls.presenter;

import android.content.Intent;
import android.widget.Toast;

import com.hytera.fcls.activity.SettingActivity;
import com.hytera.fcls.mqtt.MQTT;
import com.hytera.fcls.service.FireService;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by cctv on 2017/3/10.
 */

public class SettingPresenter {
    public static final String TAG = "y20650" + MainAtvPresenter.class.getSimpleName();
    private SettingActivity context;
    private long time = 0;

    public SettingPresenter() {
    }

    public SettingPresenter(SettingActivity context) {
        this.context = context;
    }



    /**
     * 退出程序
     */
    public void exit() {
        //如果在两秒大于2秒
        if (System.currentTimeMillis() - time > 2000) {
            //获得当前的时间
            time = System.currentTimeMillis();
            Toast.makeText(context, "再点击一次退出应用程序", Toast.LENGTH_SHORT).show();
        } else {
            //点击在两秒以内
           context.removeAllActivity();//执行移除所以Activity方法
            Intent serviceIntent = new Intent(context,FireService.class);
            context.stopService(serviceIntent);
            MQTT.getInstance().getClient().unregisterResources();
            MQTT.getInstance().getClient().close();
            try {
                MQTT.getInstance().getClient().disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

}
