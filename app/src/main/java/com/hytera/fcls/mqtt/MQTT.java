package com.hytera.fcls.mqtt;

import android.content.Context;
import android.util.Log;

import com.hytera.fcls.IMQConn;
import com.hytera.fcls.mqtt.callback.ConnectCallBackHandler;
import com.hytera.fcls.mqtt.callback.MqttCallbackHandler;
import com.hytera.fcls.mqtt.callback.SubcribeCallBackHandler;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Tim on 17/2/28.
 */

public class MQTT {

//    private static final String ServerIP = "30.1.0.101"; // 测试地址
    private static final String ServerIP = "192.168.43.22"; // 测试地址
    private static final String PORTID = "1883"; // MQTT 协议的对应的端口
    private static final String ClientID = "y20650";
    private static final String TOPIC = "fire";

    private Context context;

    private MqttAndroidClient client;


    public MQTT(Context context) {
        this.context = context;
    }

    public void startConnect(IMQConn imqConn) {
        //服务器地址
        String  uri ="tcp://";
        uri=uri+ServerIP+":"+PORTID;
        Log.d("MainActivity",uri+"  "+ClientID);
        /**
         * 连接的选项
         */
        MqttConnectOptions conOpt = new MqttConnectOptions();
        /**设计连接超时时间*/
        conOpt.setConnectionTimeout(3000);
        /**设计心跳间隔时间300秒*/
        conOpt.setKeepAliveInterval(300);
        /**
         * 创建连接对象
         */
        client = new MqttAndroidClient(context, uri, ClientID);
        /**
         * 连接后设计一个回调
         */
        client.setCallback(new MqttCallbackHandler(context, ClientID));
        /**
         * 开始连接服务器，参数：ConnectionOptions,  IMqttActionListener
         */
        try {
            client.connect(conOpt, null, new ConnectCallBackHandler(context,imqConn));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅主题
     */
    public void subcribeTopic(){
        if (client != null) {
            try {
                // 第二参数表示订阅服务的质量
                // 0 之多发一次
                // 1 至少发一次
                // 2 只发一次
                client.subscribe(TOPIC, 0, null, new SubcribeCallBackHandler(context));
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
