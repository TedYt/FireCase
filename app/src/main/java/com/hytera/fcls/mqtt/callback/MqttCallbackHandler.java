package com.hytera.fcls.mqtt.callback;

import android.content.Context;
import android.util.Log;

import com.hytera.fcls.mqtt.event.MessageEvent;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;


/**
 * Description :接收服务器推送过来的消息
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : 2016/10/25 0025
 */

public class MqttCallbackHandler implements MqttCallback {

    public static final String TAG = "MQ" + "MQCallbackHandler";

    private Context context;
    private String clientId;

    public MqttCallbackHandler(Context context,String clientId) {
        this.context=context;
        this.clientId=clientId;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Log.d(TAG,"MqttCallbackHandler/connectionLost");
    }

    /**
     *
     * @param s  主题
     * @param mqttMessage  内容信息
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Log.d(TAG, "MqttCallbackHandler/messageArrived="+s);
        Log.d(TAG, "message="+new String(mqttMessage.getPayload()));
        EventBus.getDefault().post(new MessageEvent(s,mqttMessage));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d(TAG, "MqttCallbackHandler/deliveryComplete");
    }

}
