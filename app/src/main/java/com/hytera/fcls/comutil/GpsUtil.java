package com.hytera.fcls.comutil;


import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * @Description 高德地图定位工具类
 * Created by cctv on 2017/3/3.
 */

public class GpsUtil {

    public static AMapLocationClient mlocationClient;
    public static AMapLocationClientOption mLocationOption = null;
    public static AMapLocation aMapLocation =null;

    static AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation a) {
            if (a !=null){
                aMapLocation = a;
            }else {
                //获取定位数据失败
                Log.e("GpsUtil","获取定位数据失败");
            }
        }
    };

    /**
     * @Title init
     * @Description :初始化地图导航，在Application Oncreate中调用，只需要调用一次
     * @param context
     */
    public static void  init(Context context) {
        // 声明mLocationOption对象
        mlocationClient = new AMapLocationClient(context);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //TODO 获取一次定位结果：
        //mLocationOption.setOnceLocation(true);  //该方法默认为false。
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //TODO 获取一次定位结果
        //TODO 连续定位设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        mLocationOption.setLocationCacheEnable(false);//关闭缓存机制
        mlocationClient.setLocationOption(mLocationOption);// 设置定位参数
        mlocationClient.setLocationListener(aMapLocationListener);
//        aMapLocation = mlocationClient.getLastKnownLocation(); // 初始化
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }


    public static AMapLocation getLocation (){
        return aMapLocation;
    }

    /**
     *
     * @Title: destroy
     * @Description: 销毁定位，必须在退出程序时调用，否则定位会发生异常
     */
    public static void destroy() {
        mlocationClient.onDestroy();
        mlocationClient = null;
        mLocationOption = null;
        aMapLocationListener = null;
    }



    /**
     * 需要执行先执行定位才能获得client
     */
    public static void startLocation(){
        mlocationClient.startLocation();
    }


}

