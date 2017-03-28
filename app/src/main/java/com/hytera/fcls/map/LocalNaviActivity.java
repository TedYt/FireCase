package com.hytera.fcls.map;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.hytera.fcls.DataUtil;
import com.hytera.fcls.R;
import com.hytera.fcls.comutil.GpsUtil;

public class LocalNaviActivity extends AMapBaseActivity {
    public static final String TAG = "y20650" + LocalNaviActivity.class.getSimpleName();

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

        sList.clear();
        eList.clear();
        double lngend = DataUtil.getFireCaseBean().getMapx(); //mapx : 113.801231经度
        double latend = DataUtil.getFireCaseBean().getMapy();//mapy : 22.69247纬度
//        double lngend = 113.801231;
//        double latend = 22.69247;
        NaviLatLng mEndLatlng = new NaviLatLng(latend, lngend);
//
        double lngstart = GpsUtil.getInstance().getLocation().getLongitude();
        double latstart = GpsUtil.getInstance().getLocation().getLatitude();
//        double lngstart = 113.901231;
//        double latstart = 22.39247;
        NaviLatLng mStartLatlng = new NaviLatLng(latstart, lngstart);
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);
        Log.d(TAG, "起点坐标" + mStartLatlng.toString() + "/r/n" + "终点坐标" + mEndLatlng.toString());
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onCalculateRouteSuccess() {
        super.onCalculateRouteSuccess();
        Log.i(TAG, "计算路径成功开启导航");
        mAMapNavi.startNavi(NaviType.GPS);
    }
}
