package com.hytera.fcls.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tim on 17/3/8.
 */

public class BootupReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 启动后台服务
        Intent intent1 = new Intent(context, FireService.class);
        context.startService(intent1);
    }
}
