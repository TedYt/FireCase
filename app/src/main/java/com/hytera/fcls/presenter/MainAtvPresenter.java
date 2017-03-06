package com.hytera.fcls.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.hytera.fcls.DataUtil;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.hytera.fcls.IMainAtv;
import com.hytera.fcls.activity.MainActivity;
import com.hytera.fcls.bean.CaseStateBean;
import com.hytera.fcls.bean.LoginResponseBean;
import com.hytera.fcls.mqtt.MQTT;
import com.hytera.fcls.service.AmapGpsService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tim on 17/2/25.
 */

public class MainAtvPresenter {

    public static final String TAG = "y20650" + MainAtvPresenter.class.getSimpleName();

    /** 一些常量 */
    private final int IMAGE_WIDTH = 720;
    private final int IMAGE_HEIGHT = 1080;
    public static final int LOCATION_UPDATE_TIME = 5000; // GPS每5秒更新

    /** 其他成员变量 */
    private IMainAtv iMainAtv;

    private Context context;

    /** 具体实现 */
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 当坐标改变时触发此方法，如果Provider传进相同的坐标，它就不会被触发
            if (location != null) {
                double Lat = 0.0;
                double Lng = 0.0;
                Log.i(TAG, "Location changd : Lat : "
                        + location.getLatitude() + ", Lng : "
                        + location.getLongitude());
                iMainAtv.showLogInMain("onLocationChanged, lat : " + location.getLatitude()
                        + ", Lng : " + location.getLongitude());
                iMainAtv.updateLocation(location.getLatitude(), location.getLongitude());
                /** 在这里处理位置信息上报 */
                MQTT mqtt = MQTT.getInstance();
                mqtt.postGPSLocation(Lat, Lng);
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            // Provider 的状态在可用，暂时不用和无服务三个状态直接时 触发该方法
        }

        @Override
        public void onProviderEnabled(String s) {
            // Provider 被enable时触发此函数，比如GPS 被打开
        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    public MainAtvPresenter(MainActivity mainActivity, IMainAtv iMainAtv) {
        context = mainActivity;
        this.iMainAtv = iMainAtv;
    }

    public void startCamera(MainActivity context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File out = new File(getPhotoPath());
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, 1);
    }

    private String getPhotoPath() {
        String filepath = "";
        String pathUri = Environment.getExternalStorageDirectory() + "/fireDispatcher/";
        String imageName = "imageTest" + ".jpg";
        File file = new File(pathUri);
        file.mkdirs();
        filepath = pathUri + imageName;
        return filepath;
    }

    public Bitmap getBitmapFromCamera() {
        //Drawable drawable = BitmapDrawable.createFromPath(getPhotoPath());
        Bitmap bitmap = getBitmapFromUrl(getPhotoPath());
        if (bitmap != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int size = bitmap.getByteCount();
            Log.i(TAG, "get return image, w = " + w + ", h = " + h
                    + "， size = " + size);
        } else {
            Log.i(TAG, "NOT get return image");
        }
        return bitmap;
    }

    private Bitmap getBitmapFromUrl(String uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(uri);
        // 防止OOM发生
        options.inJustDecodeBounds = false;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scalewWidth = 1;
        float scaleHeight = 1;

        if (width <= height) {
            scalewWidth = IMAGE_WIDTH * 1.0f / width;
            scaleHeight = IMAGE_HEIGHT * 1.0f / height;
        } else {
            scalewWidth = IMAGE_HEIGHT * 1.0f / width;
            scaleHeight = IMAGE_WIDTH * 1.0f / height;
        }
        Log.i(TAG, "scalewWidth = " + scalewWidth + ", scaleHeight = " + scaleHeight);
        matrix.postScale(scalewWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,
                0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return newBitmap;
    }

    private String getCurDateStr() {
        Date date = new Date(System.currentTimeMillis());
        String timeStr = new SimpleDateFormat("yyyMMdd_hhmmss", Locale.CHINA).format(date);
        Log.i(TAG, "timeStr is : " + timeStr);
        return timeStr;
    }

    /**
     * 测试方法
     * 获取GPS信息
     */
    public void getLocation() {
        double latitude = 0.0;
        double longitude = 0.0;

        if (!checkLocationPermission()) {
            iMainAtv.showLogInMain("No permission");
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            iMainAtv.showLogInMain("GPS_Provider is enable");
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            //        LOCATION_UPDATE_TIME, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                iMainAtv.showLogInMain("GPS_PROVIDER, lat : " + latitude
                        + ", Lng : " + longitude);
            }
        } else {
            //MQTT mqtt = MQTT.getInstance();
            //mqtt.postGPSLocation(110.110, 119.119);
            //iMainAtv.showLogInMain("NETWORK_Provider is enable");
            /**
             * 绑定监听
             * 参数1：获取GPS的方式：GPS 还是 NETWORK
             * 参数2：更新周期，单位毫秒
             * 参数3：更新位置的最小距离。
             *      若为0，则以第2个参数为准，若不为0，则以该参数为准。
             *      若2和3都为0，则随时刷新
             * 参数4：listener
             */
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_TIME, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                iMainAtv.showLogInMain("NETWORK_PROVIDER, lat : " + latitude
                        + ", Lng : " + longitude);
            }
        }
    }

    public void onDestroy() {
        if (!checkLocationPermission()){
            return;
        }
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }

    private boolean checkLocationPermission(){

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }

        return true;
    }

    /**
     * 确认到达现场
     * 上传确认到达信息
     */
    public void arriveDest() {
        DataUtil.fireCaseState = DataUtil.CASE_STATE_ARRIVE;
        String arriveInfo = getStateInfo(DataUtil.CASE_STATE_ARRIVE);
        Log.i(TAG, "info : " + arriveInfo);
        HTTPPresenter.post(DataUtil.FIRE_CASE_URL, arriveInfo, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "arriveDest, response is " + response);
            }
        });
    }

    /**
     * 结束警情，上报服务器
     */
    public void closeCase() {
        DataUtil.fireCaseState = DataUtil.CASE_STATE_FINISH;
        String closeInfo = getStateInfo(DataUtil.CASE_STATE_FINISH);
        Log.i(TAG, "info : " + closeInfo);
        HTTPPresenter.post(DataUtil.FIRE_CASE_URL, closeInfo, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "arriveDest, response is " + response);
            }
        });
    }

    private String getStateInfo(int state) {
        CaseStateBean caseStateBean = new CaseStateBean();
        caseStateBean.setState(state);
        caseStateBean.setUpdatetime(System.currentTimeMillis());
        caseStateBean.setCaseID("1234567890");
        LoginResponseBean.UserBean loginUserBean;
        CaseStateBean.UserBean userBean;

        try {
            loginUserBean = DataUtil.getLoginUserBean();
            userBean = new CaseStateBean.UserBean();
            userBean.setUserCode(loginUserBean.getUserCode());
            userBean.setToken(loginUserBean.getToken());
            userBean.setStaffName(loginUserBean.getStaffName());
            userBean.setOrgName(loginUserBean.getOrgName());
            userBean.setOrgGuid(loginUserBean.getOrgGuid());
        }catch (NullPointerException e){
            userBean = new CaseStateBean.UserBean();
            userBean.setUserCode("303798");
            userBean.setToken("1E5CA34FC811430FBD401CF2187C81C1");
            userBean.setStaffName("张大安");
            userBean.setOrgName("新安大队");
            userBean.setOrgGuid("1234");
            caseStateBean.setUserBean(userBean);
        }
    }
    //开始出发
    public void depart(){
        //1.开始导航
        //外部导航
        InstatNav();
        //内部导航暂未修订
        //修改内置
//                    Intent intent = new Intent(MainActivity.this,NaviActivity.class);
//                    startActivity(intent);
        //2.开启服务上传定位结果
         Intent startGpsLocation =new Intent(context, AmapGpsService.class);
         context.startService(startGpsLocation);
         Log.d(TAG, "depart: 开始导航服务开启");
    }

        Gson gson = new Gson();
        return gson.toJson(caseStateBean);
    }
    //手写一个方法导航
    public void InstatNav(){
        //创建一个虚拟位置
        LatLng fakepostion = new LatLng(23.534606,114.943771);
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(fakepostion);
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
        try {
            // 调起高德地图导航
            AMapUtils.openAMapNavi(naviPara, context);
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(context);
        }
//        mAMap.clear();
    }
}
