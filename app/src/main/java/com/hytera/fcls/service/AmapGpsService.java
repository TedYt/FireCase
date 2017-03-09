package com.hytera.fcls.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.hytera.fcls.comutil.GpsUtil;
import com.hytera.fcls.mqtt.MQTT;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AmapGpsService extends Service {
    public static final String TAG = "AmapGpsService";
    public boolean flag_upload_gps =true; //true开启上传
    Date date;//应用当前时间

    public AmapGpsService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        // 定位工具初始化
        GpsUtil.init(this);
    }

    //调用Gps开启
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启前需要先执行定位。
        GpsUtil.startLocation();
        Log.d(TAG, "执行上传");
        startGps();
        return super.onStartCommand(intent, flags, startId);
    }

    //服务停止，停止上传gps
    @Override
    public void onDestroy() {
        super.onDestroy();
        flag_upload_gps =false;
        GpsUtil.destroy();
        Log.d(TAG, "关闭服务");
    }

    /**
     * 开启gps上传
     */
    public  void startGps(){
        GpsUtil.mlocationClient.startLocation();
        Log.d(TAG, "位置信息上传准备开始");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag_upload_gps){
                    try {
                        //设置5s 上传一次位置
                        Thread.sleep(5000);
                        getLocationInfo();//获得位置信息
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    /**
     * 获得位置信息并上传位置信息
     */
    public void getLocationInfo(){
        AMapLocation aMapLocation = GpsUtil.getLocation();
        //获取定位时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date(aMapLocation.getTime());
        df.format(date);
        Log.d(TAG," 当前位置信息"+ df.format(date)+"\r \n"+
                "获取纬度"+aMapLocation.getLatitude()+"\r \n"+
                "获取经度"+aMapLocation.getLongitude()+"\r \n"+
                "地址"+ aMapLocation.getAddress()+"\r \n"+
                "城市信息"+ aMapLocation.getCity()+"\r \n"+
                "gps状态"+ aMapLocation.getGpsAccuracyStatus()+"\r \n"+
                "gps来源"+ aMapLocation.getLocationType()+"\r \n"
        );
        // 上传位置信息
        MQTT mqtt = MQTT.getInstance();
        mqtt.pushGPSLocation(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }
}
