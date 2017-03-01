package com.hytera.fcls.mqtt.callback;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hytera.fcls.IMQConn;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * Description :
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : 2016/10/25 0025
 */

public class ConnectCallBackHandler implements IMqttActionListener {

    public static final String TAG = "MQ" + "ConnectCallBackHand";

    private Context context;

    private IMQConn imqConn;

    public ConnectCallBackHandler(Context context, IMQConn imqConn) {
        this.context=context;
        this.imqConn = imqConn;
    }

    @Override
    public void onSuccess(IMqttToken iMqttToken) {
        Log.d(TAG,"ConnectCallBackHandler/onSuccess");
        Toast.makeText(context,"连接成功",Toast.LENGTH_SHORT).show();
        imqConn.MQConnSuccess();
        //HomeActivity.startAction(context);
    }

    @Override
    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
        Log.d(TAG,"ConnectCallBackHandler/onFailure");
        Toast.makeText(context,"连接失败",Toast.LENGTH_SHORT).show();
        imqConn.MQConnFailure();
    }
}
