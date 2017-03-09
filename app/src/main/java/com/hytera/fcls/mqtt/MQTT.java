package com.hytera.fcls.mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IMQConn;
import com.hytera.fcls.bean.GPSBean;
import com.hytera.fcls.bean.LoginResponseBean;
import com.hytera.fcls.mqtt.callback.ConnectCallBackHandler;
import com.hytera.fcls.mqtt.callback.MqttCallbackHandler;
import com.hytera.fcls.mqtt.callback.PublishCallBackHandler;
import com.hytera.fcls.mqtt.callback.SubcribeCallBackHandler;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tim on 17/2/28.
 */

public class MQTT {

    public static final String TAG = "y20650" + "MQTT";

    private static final String ServerIP = "192.168.123.91"; // 测试地址
    private static final String PORTID = "1883"; // MQTT 协议的对应的端口
    //private static final String ClientID = "y20650";

    /** 接受警情的主题 */
    private static final String PUSH_STATE_TOPIC = "topic_type_case_push";
    /** 上报GPS的主题 */
    private static final String GPS_TOPIC = "topic_type_gps";

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
        String clientID;
        try{
            clientID = DataUtil.getLoginUserBean().getUserCode();
        }catch (NullPointerException e){
            clientID = "y20650";
        }


        //服务器地址
        String  uri ="tcp://";
        uri=uri+ServerIP+":"+PORTID;
        Log.d("MainActivity",uri+"  "+clientID);
        /**
         * 连接的选项
         */
        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setUserName("user");
        conOpt.setPassword("test".toCharArray());
        /**设计连接超时时间*/
        conOpt.setConnectionTimeout(3000);
        /**设计心跳间隔时间300秒*/
        conOpt.setKeepAliveInterval(300);
        /**
         * 创建连接对象
         */
        client = new MqttAndroidClient(context, uri, clientID);
        /**
         * 连接后设计一个回调
         */
        client.setCallback(new MqttCallbackHandler(context, clientID));
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
                client.subscribe(PUSH_STATE_TOPIC, 0, null, new SubcribeCallBackHandler(context));
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
    public void pushGPSLocation(double lat, double lng) {
        /**消息的服务质量*/
        int qos=0;
        /**消息是否保持*/
        boolean retain=false;
        /**要发布的消息内容*/
        byte[] message = getGPSMsg(lat,lng);
        if(GPS_TOPIC!=null&&!"".equals(GPS_TOPIC)){
            /**获取client对象*/
            //MqttAndroidClient client = MainActivity.getMqttAndroidClientInstace();
            if(client!=null){
                try {
                    /**发布一个主题:如果主题名一样不会新建一个主题，会复用*/
                    client.publish(GPS_TOPIC,message,qos,retain,null,new PublishCallBackHandler(context));
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

    /**
     * 获取用户名
     * @return
     */
    public String getName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML,0);
        return sharedPreferences.getString(DataUtil.KEY_USERNAME,"");
    }

    private byte[] getGPSMsg(double lat, double lng) {
        GPSBean gpsBean = new GPSBean();
        gpsBean.setLatitude(lat);
        gpsBean.setLongitude(lng);
        gpsBean.setCaseID(DataUtil.getFireCaseBean().getGuid());
        gpsBean.setGpsDateTime(System.currentTimeMillis());

        GPSBean.UserBean userBean = new GPSBean.UserBean();
        /* 测试数据
        userBean.setOrgGuid("124");
        userBean.setOrgName("宝安大队");
        userBean.setStaffName("张大安");
        userBean.setToken("1E5CA34FC811430FBD401CF2187C81C1");
        userBean.setUserCode("20650");
*/
        LoginResponseBean.UserBean loginBean = DataUtil.getLoginUserBean();
        userBean.setOrgGuid(loginBean.getOrgGuid());
        userBean.setOrgName(loginBean.getOrgName());
        userBean.setStaffName(loginBean.getStaffName());
        userBean.setToken(loginBean.getToken());
        userBean.setUserCode(loginBean.getUserCode());

        gpsBean.setUserBean(userBean);

        Gson gson = new Gson();
        String json = gson.toJson(gpsBean);
        return json.getBytes(Charset.defaultCharset());
    }

    private String getCurDateStr() {
        Date date = new Date(System.currentTimeMillis());
        String timeStr = new SimpleDateFormat("yyyMMdd_hhmmss", Locale.CHINA).format(date);
        Log.i(TAG, "timeStr is : " + timeStr);
        return timeStr;
    }
}
