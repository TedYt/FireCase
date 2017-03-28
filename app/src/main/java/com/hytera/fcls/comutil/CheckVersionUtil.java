package com.hytera.fcls.comutil;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.ISetting;
import com.hytera.fcls.R;
import com.hytera.fcls.presenter.StreamTools;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.hytera.fcls.service.FireService.DEFAULT_NOTI_CONTENT;
import static com.hytera.fcls.service.FireService.DEFAULT_NOTI_TITLE;

/**
 * 新增版本检查和更新工具
 * Created by cctv on 2017/3/14.
 */

public class CheckVersionUtil {
    public static final int DOWNLOAD_NOTIFY = 0;
    public static boolean DOWNLOADING = false;//true代表下载中，false代表没有下载
    private static final String TAG = DataUtil.BASE_TAG + CheckVersionUtil.class.getSimpleName();
    private static final int VERSION_SAME = 0;
    private static final int SHOW_UPDATA_DIALOG = 1;
    public static final int ERROR = 2;
    public static final String updatePath = DataUtil.UPDATE_URL;//获取网址
    static Message msg;
    static Context mcontext;
    static int limit = 0;
    private static String description;
    private static String downloadpath;
    private static String localVersion;
    static String destFileName = "";
    static String destDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/0FCLS/";
    static String destAbsPath = "";
    static NotificationCompat.Builder builder2;
    private ISetting setting;
    public  CheckVersionUtil(ISetting setting){
        this.setting = setting;
    }
    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATA_DIALOG: //显示应用对话框
                    String desc = (String) msg.obj;
                    //显示对话框
                    destAbsPath = destDir + destFileName;
                    if (checkFileExist(destAbsPath)) {
                        //如果文件存在，直接打开安装不显示对话框；
                        Toast.makeText(mcontext, "文件已存在，直接安装", Toast.LENGTH_SHORT).show();
                        install(new File(destAbsPath));
                        return;
                    }
                    //不存在
                    showUpdateDialog(desc);
                    break;
                case ERROR:
                    Toast.makeText(mcontext, "错误码:" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case VERSION_SAME:
                    Toast.makeText(mcontext, ""+msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
            setting.btenable();
        }
    };

    /**
     * 检查更新并下载
     */
    public void startCheckAndDown(Context context) {
        setting.btcancle();
        if (DOWNLOADING) {
            //正在下载中不去下载
            Toast.makeText(mcontext, "任务正在下载中", Toast.LENGTH_SHORT).show();
            return;
        }

        mcontext = context;
        localVersion = AppInfoUtils.getPackageVersion(context);
        new Thread(new checkVersionTask()).start();
    }

    /**
     * 检查文件是否存在，存在返回true,不存在false,可以下载
     *
     * @param destAbsPath
     * @return
     */
    public static boolean checkFileExist(String destAbsPath) {
        File dir = new File(destDir);//检查文件夹是否存在
//        File dir = new File(destAbsPath);
        Log.d(TAG, "文件目录" + dir);
        if (!dir.exists()) {

//            dir.mkdir();
            return false;
        }
        File file = new File(destAbsPath);
        Log.w(TAG, "文件名称" + file.getName());
        if (!file.exists()) {             //如果文件不存在就下载
            try {
//                file.createNewFile();
                //不存在返回false
                Log.w(TAG, "文件名称" + file.getName());
                Log.w(TAG, "文件不存在");
                return false;
            } catch (Exception e) {

            }
        }

        return true;
    }

    /**
     * 检查是否有最新的版本
     */
    private  class checkVersionTask implements Runnable {

        @Override
        public void run() {
            msg = Message.obtain();
//            if(handler.obtainMessage(msg.what, msg.obj) != null){
//                Message _msg = new Message();
//                _msg.what = msg.what;
//                _msg.obj= msg.obj;
//                msg = _msg;
////			return;
//            }
//            msg = Message.obtain();
            Log.d(TAG, "开始检查是否有最新版本：");
            long startTime = System.currentTimeMillis();
            try {
                URL url = new URL(updatePath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(2000);
                int code = connection.getResponseCode();
                Log.i(TAG, "run: code" + code);
                if (200 == code) {
                    InputStream is = connection.getInputStream();
                    String result = StreamTools.readStream(is);
                    JSONObject jsonObject = new JSONObject(result);
                    String serverVersion = jsonObject.getString("version");
                    description = jsonObject.getString("description");
                    downloadpath = jsonObject.getString("downloadpath");
                    destFileName = "fcls_hytera" + serverVersion + ".apk";
                    Log.d(TAG, "服务器版本：" + serverVersion);
                    Log.d(TAG, "下载路径：" + downloadpath);
                    Log.d(TAG, "保存文件名：" + destFileName);

                    checkVersion(localVersion, serverVersion);

                } else {
                    msg.what = ERROR;
                    msg.obj = "code:410";
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                msg.what = ERROR;
                msg.obj = "code:404";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                msg.what = ERROR;
                msg.obj = "code:405";
            } catch (IOException e) {
                e.printStackTrace();
                msg.what = ERROR;
                msg.obj = "code:408";//服务器等候请求时发生超时？
            } catch (JSONException e) {
                e.printStackTrace();
                msg.what = ERROR;
                msg.obj = "code:409";
            } finally {
                // 计算代码走到这花费的时间
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                if (duration > 2000) {
                    Log.d(TAG, "请求响应时间差：" + duration);
                }
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 检查版本号
     *
     * @param localVersion
     * @param serverVersion
     */
    private static void checkVersion(String localVersion, String serverVersion) {

        if (localVersion.equals(serverVersion)) {
            Log.d(TAG, "版本号相同无需升级");
            msg.what = VERSION_SAME;
            msg.obj ="版本号相同，无需升级";

        } else {
            Log.d(TAG, "版本号不相同提示用户升级");
            msg.what = SHOW_UPDATA_DIALOG;
            msg.obj = description;
        }
    }

    protected static void showUpdateDialog(String desc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setCancelable(false);
        builder.setTitle("升级提醒");
        builder.setMessage(desc);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(mcontext, "开始下载", Toast.LENGTH_SHORT).show();
                DOWNLOADING = true;
                downloadFile();
            }
        });

        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e(TAG, "不下载了 :" + i);
            }
        });

        builder.show();
    }


    private static void downloadFile() {
        builder2 = new NotificationCompat.Builder(mcontext);
        new Thread(downLoadRunnable).start();

    }

    /**
     * 显示更新通知
     *
     * @param title
     * @param msg
     */
    public static void showNotification(String title, String msg, int progress) {
        if (title == null || title.isEmpty()) {
            title = DEFAULT_NOTI_TITLE;
        }
        if (msg == null || msg.isEmpty()) {
            msg = DEFAULT_NOTI_CONTENT;
        }
        NotificationManager notificationManager = (NotificationManager) mcontext.getSystemService(NOTIFICATION_SERVICE);
        builder2
                .setContentTitle(title) // 必填的属性
                .setContentText(msg) // 必填的属性
                .setSmallIcon(R.mipmap.ic_launcher96) // 必填的属性
                .setWhen(System.currentTimeMillis());
        if (progress > 0 && progress <= 100) {
            builder2.setProgress(100, progress, false);
        } else {
            builder2.setProgress(0, 0, false);
        }
        builder2.setAutoCancel(false);
        builder2.setWhen(System.currentTimeMillis());
        builder2.setTicker(title);
        Notification notification = builder2.build();
        notificationManager.notify(DOWNLOAD_NOTIFY, notification);
    }


    static Runnable downLoadRunnable = new Runnable() {
        @Override
        public void run() {

            OkHttpUtils
                    .get()
                    .url(downloadpath)
                    .build()
                    .execute(new FileCallBack(destDir, destFileName) {

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                        }

                        @Override
                        public void inProgress(float progress, long total, int id) {
                            super.inProgress(progress, total, id);
                            if (limit % 50 == 0 || progress == 100) {//隔30次更新一次notification，防止卡顿
                                Log.e(TAG, "inProgress :" + (int) (100 * progress));
                                showNotification("正在下载", "" + (int) (100 * progress) + "%", (int) (100 * progress));
                            }

                            limit++;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e(TAG, "onError出错了 :" + e.getMessage());
                            DOWNLOADING = false;
                            Toast.makeText(mcontext, "下载异常，请检查网络", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onResponse(final File response, int id) {
                            limit = 0;
                            DOWNLOADING = false;

                            showNotification("下载完成", "下载完成", 100);
                            Log.e(TAG, "onResponse :下载完成" + response.getAbsolutePath());
                            Toast.makeText(mcontext, "下载成功" + destDir + destFileName,
                                    Toast.LENGTH_SHORT).show();

                            NotificationManager manger = (NotificationManager) mcontext.getSystemService(NOTIFICATION_SERVICE);
                            manger.cancel(DOWNLOAD_NOTIFY);
                            install(response);

                        }
                    });
        }
    };

    public static void install(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(
                Uri.fromFile(file),
                "application/vnd.android.package-archive");
        mcontext.startActivity(intent);
    }

    /**
     * 检查 DownloadeManager是否可以用
     * @param context
     * @return
     */
    public static boolean isDownloadManangerAvailable(Context context){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD){
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName("com.android.providers.downloads.ui",
                "com.android.providers.downloads.ui.DownloadList");
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        Log.i(DataUtil.BASE_TAG, "list size is " + list.size());
        return list.size() > 0;
    }


}
