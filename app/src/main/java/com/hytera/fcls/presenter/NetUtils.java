package com.hytera.fcls.presenter;

/**
 * Created by Tim on 17/3/3.
 */

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 处理HTTP的get和post方法
 */
public class NetUtils {

    private static final String TAG = "y20650" + "NetUtils";

    public static String post(final String url, final String data){
        HttpURLConnection conn = null;

        try{
            // 创建URL对象
            URL u = new URL(url);
            // 获取HttpURLConnection 对象
            conn = (HttpURLConnection)u.openConnection();

            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);
            conn.setDoOutput(true); // post方法必须设置允许输入输出
            conn.setDoInput(true);

            OutputStream out = conn.getOutputStream(); // 获得输出流，向服务器写数据
            out.write(data.getBytes("utf-8"));
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode(); // 调动此方法，就不用调用connect方法了
            if (responseCode == 200){
                Log.i(TAG, "connect ok.");
                InputStream is = conn.getInputStream();
                return getStringFromInputStream(is);// 返回response
            }else {
                Log.i(TAG, "connect failed : " + responseCode);
                throw new NetworkErrorException("response status is " + responseCode + "y20650");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (conn != null){
                conn.disconnect(); // 关闭连接
            }
        }

        return null;
    }

    public static String get(final String url){
        HttpURLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection)u.openConnection();

            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(10000);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200){
                Log.i(TAG, "connect ok.");
                InputStream is = conn.getInputStream();
                return getStringFromInputStream(is); // 返回response
            }else {
                Log.i(TAG, "connect failed : " + responseCode);
                throw new NetworkErrorException("Response status is " + responseCode + "y20650");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (conn != null){
                conn.disconnect();
            }
        }
        return null;
    }

    private static String getStringFromInputStream(InputStream is) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1){
            os.write(buffer,0,len);
        }
        is.close();

        String state = os.toString(); // 把流中的数据转换成字符串，采用的编码是utf-8（默认）
        os.close();

        return state;
    }
}
