package com.hytera.fcls.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tim on 17/2/24.
 */

/**
 * 没有作用 2017.3.8
 */
public class KeepLiveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_OFF)){ // 灭屏时，启动activity
            //Log.i("y20650", "KeepLiveReceiver, ACTION_SCREEN_OFF");
            //FireApplication.getInstance().startKeepLiveActivity();
        }else if (action.equals(Intent.ACTION_USER_PRESENT)){ // 解锁后，注销activity
            //FireApplication.getInstance().startKeepLiveActivity();
        }else if (action.equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent intent1 = new Intent(context, FireService.class);
            context.startService(intent1);
        }

    }
}
