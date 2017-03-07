package com.hytera.fcls.presenter;

/**
 * Created by Tim on 17/3/3.
 */

import android.accounts.NetworkErrorException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    /**
     * 上传文件
     * @param u
     * @param filename
     * @param filepath
     */
    public static void postFile(String u, String filename,String filepath) {
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------hytera";

        try {
            File f = new File(filepath);
            URL url = new URL(u);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:26.0) Gecko/20100101 Firefox/26.0");
            conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            byte[] fb = new byte[(int) f.length()];
            FileInputStream fis = new FileInputStream(f);
            fis.read(fb);
            StringBuffer strBuf = new StringBuffer();
            String inputName = "uploadfile";
            String contentType = "image/jpeg";
            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            strBuf.append(
                    "Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());
            out.flush();
            out.write(fb);
            out.flush();
            out.write(("\r\n--"+BOUNDARY+"--\r\n").getBytes());
            out.flush();
            out.close();
            fis.close();
            strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            Log.e(TAG, "post successfully");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "post failed : " + e.getMessage());
        }

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
