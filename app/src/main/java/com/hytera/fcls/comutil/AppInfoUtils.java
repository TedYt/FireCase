package com.hytera.fcls.comutil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 应用信息的工具类
 * Created by cctv on 2017/3/13.
 */

public class AppInfoUtils {
    /**
     * 获取应用程序版本信息
     * @param context
     * @return
     */
    public  static String getPackageVersion(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().
                                                getPackageInfo(context.getPackageName(),0);
            String version = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "解析版本号失败";
        }

    }
}
