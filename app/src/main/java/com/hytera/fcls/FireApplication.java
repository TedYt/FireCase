package com.hytera.fcls;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 17/2/24.
 */
public class FireApplication extends Application {

    private static FireApplication _instance = new FireApplication();
    private List<Activity> runningActivities = new ArrayList<Activity>();//用于存放启动的activity的集合
    private static final String TAG = "y20650" + FireApplication.class.getSimpleName();
    //private FireApplication(){}

    public static FireApplication getInstance(){
        if (_instance == null){
            Log.e(TAG, "getInstance: 1111111" );
            synchronized (FireApplication.class){
                if (_instance == null){
                    _instance = new FireApplication();
                    Log.e(TAG, "getInstance: 222222" );
                }
            }
        }
        Log.e(TAG, "getInstance: 3333333333" );
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), "db81990995", false);
//        _instance = this;
    }

    /**
     * 增加Activity
     * @param activity
     */
    public void addActivity(Activity activity){
        //如果不在集合就添加
        if (!runningActivities.contains(activity)){
            runningActivities.add(activity);
            Log.d(TAG, "addActivity: "+activity);
        }
    }

    /**
     * 移除Activity
     * @param activity
     */
    public void  removeActivity(Activity activity){
        //如果在集合中就删除
        if (runningActivities.contains(activity)){
            runningActivities.remove(activity);
            Log.d(TAG, "removeActivity: "+activity);
            activity.finish();
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void  removeAllActivity(){
        for (Activity activity: runningActivities){
            Log.d(TAG, "removeAllActivity: 移除所有Activity"+activity);
            activity.finish();
        }
    }
}
