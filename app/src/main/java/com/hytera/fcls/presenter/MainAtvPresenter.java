package com.hytera.fcls.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.NaviPara;
import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IMainAtv;
import com.hytera.fcls.activity.MainActivity;
import com.hytera.fcls.bean.FireCaseBean;
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
    public static final int CAMERA_RESULT = 100;

    /**
     * 一些常量
     */
    private final int IMAGE_WIDTH = 720;
    private final int IMAGE_HEIGHT = 1080;
    public static final int LOCATION_UPDATE_TIME = 5000; // GPS每5秒更新

    /**
     * 其他成员变量
     */
    private IMainAtv iMainAtv;

    private Context context;

    private String filePath;

    private String fileName;

    /**
     * 上传位置信息在 AmapGpsService 实现
     */
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
                mqtt.pushGPSLocation(Lat, Lng);
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
        File out = new File(getImagePath());
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, CAMERA_RESULT);
    }

    private String getImagePath() {
        String pathUri = Environment.getExternalStorageDirectory() + "/fireDispatcher/";
        fileName = getCurDateStr() + ".jpg";
        File file = new File(pathUri);
        if (!file.exists()) {
            file.mkdirs();
        }
        filePath = pathUri + fileName;
        return filePath;
    }

    /**
     * 上传图片
     */
    public synchronized void postImage() {
        // url : token + appGuid(userCode) + caseGuid(caseID)
        LoginResponseBean.UserBean userBean = DataUtil.getLoginUserBean();
        FireCaseBean fireCaseBean = DataUtil.getFireCaseBean();
        String url;
        if (userBean != null || fireCaseBean == null) {
            url = DataUtil.FIRE_CASE_IMG_URL
                    + "token="
                    + ((userBean.getToken() == null) ? "1233456789" : userBean.getToken())
                    + "&"
                    + "appGuid="
                    + ((userBean.getUserCode() == null) ? "y20650" : userBean.getUserCode())
                    + "&"
                    + "caseGuid="
                    + ((fireCaseBean.getGuid() == null) ? "123245" : fireCaseBean.getGuid());
        } else {
            url = DataUtil.FIRE_CASE_IMG_URL
                    + "token="
                    + "1233456789"
                    + "&"
                    + "appGuid="
                    + "y20650"
                    + "&"
                    + "caseGuid="
                    + "test";
        }

        HTTPPresenter.postFile(url, fileName, filePath, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "postImage, response is " + response);
            }
        });
    }

    public Bitmap getBitmapFromCamera() {
        //Drawable drawable = BitmapDrawable.createFromPath(getImagePath());
        Bitmap bitmap = getBitmapFromUrl(getImagePath());
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
            //mqtt.pushGPSLocation(110.110, 119.119);
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
        if (!checkLocationPermission()) {
            return;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);
    }

    private boolean checkLocationPermission() {

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
     * 上传图片
     */
    public void arriveDest() {
        if (!FireCaseStateUtil.lastStateIsDepart()) {
            Log.w(TAG, "last state is not Depart");
            Toast.makeText(context, "请先出发", Toast.LENGTH_SHORT).show();
            return;
        }

        //到达后停止gps上传服务
        Intent stopGpsUpLoad = new Intent(context, AmapGpsService.class);
        context.stopService(stopGpsUpLoad);

        postState(DataUtil.CASE_STATE_ARRIVE);
    }

    /**
     * 结束警情，上报服务器
     */
    public void closeCase() {
        if (!FireCaseStateUtil.lastStateIsArrive()) {
            Log.w(TAG, "last state is not arrive");
            Toast.makeText(context, "还未确认到达现场", Toast.LENGTH_SHORT).show();
            return;
        }

        postState(DataUtil.CASE_STATE_FINISH);
    }

    private void postState(final int state) {
        DataUtil.fireCaseState = state;
        String closeInfo = DataUtil.getStateInfo(state);
        Log.i(TAG, "close case Info : " + closeInfo);
        HTTPPresenter.post(DataUtil.FIRE_CASE_URL, "jsonStr=" + closeInfo, new HTTPPresenter.CallBack() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "state = " + state + "arriveDest, response is " + response);
            }
        });
    }

    /**
     * 出发去现场
     */
    public void depart() {

        FireCaseBean fireCaseBean = DataUtil.getFireCaseBean();
        double lng = fireCaseBean.getMapx();
        double lat = fireCaseBean.getMapy();

        if (lng < 0.0 || lng >= 180.0 || lat < 0.0 || lat >= 90.0){
            Log.e(TAG, "GPS is not available : mapx = " + lng + ", mapy = " + lat);
            return;
        }

        if (!FireCaseStateUtil.lastStateIsCopy()) {
            Log.w(TAG, "last state is not copy");
            Toast.makeText(context, "请先接警!", Toast.LENGTH_SHORT).show();
            return;
        }
        postState(DataUtil.CASE_STATE_DEPART);

        //开启外部导航
        InstatNav(lng,lat);

        //内置导航，暂未修订
//      Intent intent = new Intent(MainActivity.this,NaviActivity.class);
//      startActivity(intent);

        //2.开启服务上传定位结果
        Intent startGpsLocation = new Intent(context, AmapGpsService.class);
        context.startService(startGpsLocation);
        Log.d(TAG, "depart: 开始导航服务开启");
    }

    /**
     * 调动外部导航
     * @param lng
     * @param lat
     */
    public void InstatNav(double lng, double lat) {
        //创建一个虚拟位置
        LatLng destination = new LatLng(lng, lat);
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(destination);
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


    AnimationDrawable animation;

    /**
     * 播放主界面中的波浪动画
     * @param view
     */
    public void play(View view) {
        if (view.getBackground() instanceof AnimationDrawable) {
            animation = (AnimationDrawable) view.getBackground();
            animation.start();
        }
    }

    /**
     * 停止主界面中的波浪动画
     * @param view
     */
    public void endPlayAnim(View view) {
        if (view.getBackground() instanceof AnimationDrawable) {
            animation = (AnimationDrawable) view.getBackground();
            animation.stop();

        }
    }
}
