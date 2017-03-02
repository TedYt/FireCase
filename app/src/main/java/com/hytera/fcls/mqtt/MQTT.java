package com.hytera.fcls.mqtt;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hytera.fcls.IMQConn;
import com.hytera.fcls.mqtt.callback.ConnectCallBackHandler;
import com.hytera.fcls.mqtt.callback.MqttCallbackHandler;
import com.hytera.fcls.mqtt.callback.PublishCallBackHandler;
import com.hytera.fcls.mqtt.callback.SubcribeCallBackHandler;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Tim on 17/2/28.
 */

public class MQTT {

    public static final String TAG = "MQTT";

//    private static final String ServerIP = "30.1.0.101"; // 测试地址
    private static final String ServerIP = "192.168.123.64"; // 测试地址
    private static final String PORTID = "1883"; // MQTT 协议的对应的端口
    private static final String ClientID = "y20650";
    private static final String TOPIC = "fire";

    private Context context;

    private MqttAndroidClient client;

    private static MQTT _instance;

    //public MQTT(Context context) {
    //    this.context = context;
    //}

    public static MQTT getInstance(){
        if (_instance == null){
            synchronized (MQTT.class){
                if (_instance == null){
                    _instance = new MQTT();
                }
            }
        }

        return _instance;
    }

    public void setContext(Context context){
        _instance.context = context;
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

    /**
     * 上报GPS信息
     * @param lat
     * @param lng
     */
    public void postGPSLocation(double lat, double lng) {
        /**消息的服务质量*/
        int qos=0;
        /**消息是否保持*/
        boolean retain=false;
        /**要发布的消息内容*/
        byte[] message = ("Latitude : " + lat + ", Longitude : " + lng).getBytes();
        if(TOPIC!=null&&!"".equals(TOPIC)){
            /**获取client对象*/
            //MqttAndroidClient client = MainActivity.getMqttAndroidClientInstace();
            if(client!=null){
                try {
                    /**发布一个主题:如果主题名一样不会新建一个主题，会复用*/
                    client.publish(TOPIC,message,qos,retain,null,new PublishCallBackHandler(context));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }else{
                Log.e(TAG,"MqttAndroidClient==null");
            }
        }else{
            Toast.makeText(context,"发布的主题不能为空",Toast.LENGTH_SHORT).show();
        }
    }
}
