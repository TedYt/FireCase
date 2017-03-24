package com.hytera.fcls.map;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.hytera.fcls.R;

public class LocalNaviActivity extends AMapBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        sList.clear();
//        eList.clear();
//        double lngend = DataUtil.getFireCaseBean().getMapx(); //mapx : 113.801231经度
//        double latend = DataUtil.getFireCaseBean().getMapy();//mapy : 22.69247纬度
//        double lngend = 113.801231;
//        double latend = 22.69247;
//        NaviLatLng mEndLatlng = new NaviLatLng(latend, lngend);
//
//        double lngstart = GpsUtil.getLocation().getLongitude();
//        double latstart = GpsUtil.getLocation().getLatitude();
//        double lngstart = 113.901231;
//        double latstart = 22.39247;
//        NaviLatLng mStartLatlng = new NaviLatLng(latstart, lngstart);
//        sList.add(mStartLatlng);
//        eList.add(mEndLatlng);
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        Log.e("aaaaa", "计算成功");
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
