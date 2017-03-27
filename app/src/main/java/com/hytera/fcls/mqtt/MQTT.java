package com.hytera.fcls.mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import com.hytera.fcls.comutil.Log;

import com.google.gson.Gson;
import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IMQConn;
import com.hytera.fcls.bean.GPSBean;
import com.hytera.fcls.bean.LoginResponseBean;
import com.hytera.fcls.bean.RtmpVideoBean;
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

    public static final String TAG = DataUtil.BASE_TAG + "MQTT";

    //private static final String ServerIP = "192.168.1.104"; // 测试地址
    private static final String PORTID = "1883"; // MQTT 协议的对应的端口
    //private static final String ClientID = DataUtil.BASE_TAG;

    /** 接受警情的主题 */
    private static final String PUSH_STATE_TOPIC = "topic_type_case_push";
    /** 上报GPS的主题 */
    private static final String GPS_TOPIC = "topic_type_gps";
    /** 分队申请结束警情，服务器下发，通知中队 */
    private static final String PRE_FINISH_TOPIC = "topic_type_case_pre_finish";
    /** 中队结束警情，服务器下发通知分队 */
    private static final String CASE_FINISH_TOPIC = "topic_type_case_finish";
    /** 视频推送主题， */
    private static final String VIDEO_TOPIC = "topic_type_video";

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
        if (_instance.context == null){
            _instance.context = context;
        }
    }

    public void startConnect(IMQConn imqConn) {
        String clientID;
        try{
            clientID = DataUtil.getLoginUserBean().getUserCode();
        }catch (NullPointerException e){
            clientID = DataUtil.BASE_TAG;
        }


        //服务器地址
        String  uri ="tcp://" + DataUtil.MQ_URL + ":" + PORTID;
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
                if (DataUtil.isZhongDui()){// 中队账号订阅 预结束警情主题
                    client.subscribe(PRE_FINISH_TOPIC, 0, null, new SubcribeCallBackHandler(context));
                }else { // 分队账号订阅 结束警情主题
                    client.subscribe(CASE_FINISH_TOPIC, 0, null, new SubcribeCallBackHandler(context));
                }
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
        if(message != null){
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
            Log.w(TAG, "向主题" + GPS_TOPIC + "发送的message为null");
        }
    }

    /**
     * 推送视频信息，主要是告诉服务器推送流的url
     * @param url
     */
    public void pushVideoURL(String url){
        int qos = 0;
        boolean retain = false;
        byte[] message = getVideoMsg(url);
        if (message != null){
            if (client != null){
                /**发布一个主题:如果主题名一样不会新建一个主题，会复用*/
                try {
                    client.publish(VIDEO_TOPIC,message,qos,retain,null,new PublishCallBackHandler(context));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }else {
                Log.e(TAG,"MqttAndroidClient==null");
            }
        }else {
            Log.w(TAG, "向主题" + VIDEO_TOPIC + "发送的message为null");
        }
    }

    /**
     * 获取video的信息，包括rtmp的url和登录用户的信息
     * @param url
     * @return
     */
    private byte[] getVideoMsg(String url) {

        RtmpVideoBean videoBean = new RtmpVideoBean();

        Gson gson = new Gson();
        // 将loginbean转成json
        String json = gson.toJson(DataUtil.getLoginUserBean());
        Log.i(TAG, "json is : " + json);
        // 再将json转成rtmp的userbean
        videoBean.setUser(gson.fromJson(json, RtmpVideoBean.UserBean.class));
        videoBean.setUrl(url);
        // 将videobean转成json
        json = gson.toJson(videoBean);

        return json.getBytes(Charset.defaultCharset());
    }

    /**
     * 获取用户名
     * @return
     */
    public String getName() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML,0);
        return sharedPreferences.getString(DataUtil.KEY_USERCODE,"");
    }

    private byte[] getGPSMsg(double lat, double lng) {
        if (!DataUtil.haveOneCase()){
            Log.w(TAG, "No fire case");
            return null;
        }

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

    public  MqttAndroidClient getClient(){
        return client;
    }

    /**
     * 判断是否是预结束警情主题
     * @param topic
     * @return
     */
    public boolean isPreFinishTopic(String topic){
        return (topic != null && topic.equals(PRE_FINISH_TOPIC));
    }

    /**
     * 判断是否是结束警情的主题
     * @param topic
     * @return
     */
    public boolean isFinishTopic(String topic){
        return (topic != null && topic.equals(CASE_FINISH_TOPIC));
    }
}
