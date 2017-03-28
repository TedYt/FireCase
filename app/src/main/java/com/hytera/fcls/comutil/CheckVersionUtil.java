package com.hytera.fcls.comutil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.hytera.fcls.DataUtil;
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

import okhttp3.Call;
import okhttp3.Request;

/**
 * 新增版本检查和更新工具
 * Created by cctv on 2017/3/14.
 */

public class CheckVersionUtil {
    private static final String TAG = "y20650" + CheckVersionUtil.class.getSimpleName();
    private static final int SHOW_UPDATA_DIALOG = 1;
    public static final int ERROR = 2;
    public static final String updatePath = DataUtil.UPDATE_URL;//获取网址
    static Message msg;
    static Context mcontext;
    private static String description;
    private static String downloadpath;
    private static String localVersion;

    private static ProgressDialog pd;

//    public CheckVersionUtil(Context context) {
//        mcontext = context;
//    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_UPDATA_DIALOG: //显示应用对话框
                    String desc = (String) msg.obj;
                    //显示对话框
                    showUpdateDialog(desc);
                    break;
                case ERROR:
                    Toast.makeText(mcontext, "错误码:" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    /**
     * 检查更新并下载
     */
    public static void startCheckAndDown(Context context) {
        mcontext = context;
        localVersion = AppInfoUtils.getPackageVersion(context);
        pd = new ProgressDialog(mcontext);
        new Thread(new checkVersionTask()).start();
    }

    /**
     * 检查是否有最新的版本
     */
    private static class checkVersionTask implements Runnable {

        @Override
        public void run() {
            msg = Message.obtain();
            Log.d(TAG, "开始检查是否有最新版本：" );
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
                    Log.d(TAG, "服务器版本：" + serverVersion);
                    Log.d(TAG, "下载路径：" + downloadpath);
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
//                pd = new ProgressDialog(mcontext);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.show();
                downloadFile();
            }
        });

        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //升级
                pd.dismiss();
                Log.e(TAG, "不下载了 :" +i);
            }
        });

        builder.show();
    }

    private static void downloadFile() {
        File sdDir = Environment.getExternalStorageDirectory();
//      File file = new File(sdDir, SystemClock.uptimeMillis() + ".apk");
//      String mdownloadpath = "http://192.168.43.22:8080/update/xxx.apk";
        String mdownloadpath = downloadpath;
        final String dest = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String destFileName = "fcls_hytera"+localVersion+".apk";
        OkHttpUtils
                .get()
                .url(mdownloadpath)
                .build()
                .execute(new FileCallBack(dest, destFileName) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                        pd.setProgress((int) (100 * progress));
                        Log.e(TAG, "inProgress :" + (int) (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pd.dismiss();
                        Log.e(TAG, "onError出错了 :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.e(TAG, "onResponse :下载完成" + response.getAbsolutePath());
                        pd.dismiss();
                        Toast.makeText(mcontext, "下载成功"+dest+destFileName,
                                Toast.LENGTH_SHORT).show();
                        // 覆盖安装apk文件
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setDataAndType(
                                Uri.fromFile(response),
                                "application/vnd.android.package-archive");
                        mcontext.startActivity(intent);
                    }
                });
    }
}
