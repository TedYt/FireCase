package com.hytera.fcls;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.hytera.fcls.activity.KeepAliveActivity;

/**
 * Created by Tim on 17/2/24.
 */
public class FireApplication extends Application {
    private volatile static FireApplication _instance = new FireApplication();

    //private FireApplication(){}

    public static FireApplication getInstance(){
        if (_instance == null){
            synchronized (FireApplication.class){
                if (_instance == null){
                    _instance = new FireApplication();
                }
            }
        }

        return _instance;
    }

    public void startKeepLiveActivity(){
        Log.i("y20650", "FireApplication, startKeepLiveActivity");
        Intent intent = new Intent(getInstance().getApplicationContext(), KeepAliveActivity.class);
        getInstance().getApplicationContext().startActivity(intent);
    }

    public void stopKeepLiveActivity(){

    }
}
