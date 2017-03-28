package com.hytera.fcls.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.hytera.fcls.comutil.Log;
import android.view.Window;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.maps.model.Poi;
import com.hytera.fcls.R;

public class NaviActivity extends BaseActivity implements LocationSource, AMapLocationListener,AMap.OnPOIClickListener
{
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationManager locMgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.activity_navi);
        initview(savedInstanceState);
        initLocationManager();
//        initMap();
        LatLng case2 = new LatLng(22.534606,113.943771);//纬度，精度
        MarkerOptions testicon2 =new MarkerOptions().position(case2)
                .snippet("着火了")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.fire))
                .title("ddd")
                .draggable(true)
                .visible(true);
        aMap.addMarker(testicon2);
    }

    private void initLocationManager() {
         locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 8, new LocationListener() {
             @Override
             public void onLocationChanged(Location location) {

                 // 使用GPS提供的定位信息来更新位置
                 updatePosition(location);
             }

             @Override
             public void onStatusChanged(String provider, int status, Bundle extras) {

             }

             @Override
             public void onProviderEnabled(String provider) {
                    // 使用GPS提供的定位信息来更新位置
                 updatePosition(locMgr
                         .getLastKnownLocation(provider));
             }

             @Override
             public void onProviderDisabled(String s) {

             }
         });
    }
    //更新位置
    private void updatePosition(Location location) {
        LatLng pos = new LatLng(location.getLatitude(),location.getLongitude());

        // 创建一个设置经纬度的CameraUpdate
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
        // 更新地图的显示区域
        aMap.moveCamera(cu);
        // 清除所有Marker等覆盖物
        aMap.clear();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pos)
                .title("着火了怎么办")
                .snippet("速呼119，119，119")
                // 设置使用自定义图标
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                .draggable(true);
        // 添加Marker
        aMap.addMarker(markerOptions);

    }

    private void initview(Bundle savedInstanceState) {

        mapView = (MapView) findViewById(R.id.amap);

        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);


    }

    //LocationSource的接口实现方法2-1
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();

        }
    }
    //LocationSource的接口实现方法2-2
    @Override
    public void deactivate() {

    }
//AMapLocationListener接口实现方法1-1,能获得经纬度
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
//                mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                //TODO 测试着火点图标
                Location mypos = aMap.getMyLocation();
                aMapLocation.getLatitude();
                aMapLocation.getLongitude();
                LatLng fireposition =new LatLng(aMapLocation.getLatitude(),aMapLocation.getLatitude());
                mypos.getLatitude();
                mypos.getLongitude();
                Log.e("当前经纬度1",""+mypos.getLongitude()+"dfdfdfdfd"+mypos.getLatitude());//当前经纬度: 113.943766dfdfdfdfd22.534607
                Log.e("当前经纬度2",""+mypos.getLongitude()+"dfdfdfdfd"+mypos.getLatitude());//当前经纬度: 113.943766dfdfdfdfd22.534607
                LatLng testfire = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                MarkerOptions testicon =new MarkerOptions().position(fireposition)
                        .snippet("着火了")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.fire))
                        .title("ddd")
                        .draggable(true)
                        .visible(true);
                aMap.addMarker(testicon);
            } else {
//                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
//                Log.e("AmapErr",errText);
//                mLocationErrText.setVisibility(View.VISIBLE);
//                mLocationErrText.setText(errText);
            }
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }
//这个是Poi接口的回掉
    @Override
    public void onPOIClick(Poi poi) {

    }
    //手写一个方法导航
    public void InstatNav(){
        //创建一个虚拟位置
        LatLng fakepostion = new LatLng(39.92463,116.389139);
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(fakepostion);
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
        try {
            // 调起高德地图导航
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps.AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }
//        mAMap.clear();
    }
}
