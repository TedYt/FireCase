package com.hytera.fcls.presenter;

import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.github.faucamp.simplertmp.RtmpHandler;
import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IVideo;
import com.hytera.fcls.R;
import com.hytera.fcls.activity.VideoActivity;
import com.hytera.fcls.mqtt.MQTT;

import net.ossrs.yasea.SrsCameraView;
import net.ossrs.yasea.SrsEncodeHandler;
import net.ossrs.yasea.SrsPublisher;
import net.ossrs.yasea.SrsRecordHandler;

import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cctv on 2017/3/18.
 */

public class VideoPresenter implements SrsEncodeHandler.SrsEncodeListener, RtmpHandler.RtmpListener, SrsRecordHandler.SrsRecordListener {

    public static final String TAG = "y20650" + "VideoPresenter";

    private IVideo iVideo;

    private VideoActivity context;
    private SrsPublisher mPublisher;
    private String recPath = Environment.getExternalStorageDirectory().getPath() + "/test.mp4";
    boolean flag_start = true; //默认点击
    public VideoPresenter(IVideo iVideo, VideoActivity context) {
        this.iVideo = iVideo;
        this.context = context;
    }

    public void initPublisher(SrsCameraView srsCameraView) {

        mPublisher = new SrsPublisher(srsCameraView);
        mPublisher.setEncodeHandler(new SrsEncodeHandler(this));
        mPublisher.setRtmpHandler(new RtmpHandler(this));
        mPublisher.setRecordHandler(new SrsRecordHandler(this));
        //参数配置详见ReadMe
//        mPublisher.setPreviewResolution(640, 480);//1.41
        mPublisher.setPreviewResolution(1280, 720);
//        mPublisher.setOutputResolution(640, 480);///输出这个效果好些
        mPublisher.setOutputResolution(1280, 720);//1.78
        mPublisher.setVideoHDMode();//设置高质量模式

        mPublisher.startCamera();
    }

    private String getCurDateStr() {
        Date date = new Date(System.currentTimeMillis());
        String timeStr = new SimpleDateFormat("yyyMMdd_hhmmss", Locale.CHINA).format(date); // _hhmmss
        Log.i(TAG, "timeStr is : " + timeStr);
        return timeStr;
    }

    public void Publish(TextView view) {

        if(flag_start){

            //开始上传
            SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
            //String staff_name = sharedPreferences.getString(DataUtil.KEY_STAFFNAME, "amdin");
            //staff_name="";
            String usercode = sharedPreferences.getString(DataUtil.KEY_USERCODE, "0000");
            String rtmpUrl = DataUtil.VIDEO_RTMP_URL + usercode + "/" + getCurDateStr();

            pushVideoURL(rtmpUrl);
            view.setBackgroundResource(R.drawable.sel_2_bt_video_stop);
            Log.d(TAG, "rtmurl：" + rtmpUrl);
            mPublisher.startPublish(rtmpUrl);
            mPublisher.startCamera();
            Log.d(TAG, "开始");
            flag_start= false;
        }else {
            mPublisher.stopPublish();
            mPublisher.stopRecord();
            Log.d(TAG, "停止");
            view.setBackgroundResource(R.drawable.sel_2_bt_video_publish);
            flag_start= true;
        }
    }

    /**
     * 通过总线向服务器发送
     * @param rtmpUrl
     */
    private void pushVideoURL(String rtmpUrl) {
        MQTT mqtt = MQTT.getInstance();
        mqtt.setContext(context.getApplicationContext());
        MQTT.getInstance().pushVideoURL(rtmpUrl); // 告诉服务器视频推流的URL
    }

    public void switchCamera() {
        mPublisher.switchCameraFace((mPublisher.getCamraId() + 1) % Camera.getNumberOfCameras());
    }

    /**
     * 录像功能
     * @param view
     */
    public void Record(TextView view) {
        if (view.getText().toString().contentEquals("record")) {
            if (mPublisher.startRecord(recPath)) {
                view.setText("pause");
            }
        } else if (view.getText().toString().contentEquals("pause")) {
            mPublisher.pauseRecord();
            view.setText("resume");
        } else if (view.getText().toString().contentEquals("resume")) {
            mPublisher.resumeRecord();
            view.setText("pause");
        }

    }

    /**
     * 软硬解加密
     * @param view
     */
    public void switchEncode(TextView view) {
        if (view.getText().toString().contentEquals("软编码")) {
            mPublisher.switchToSoftEncoder();
            view.setText(R.string.hard_encoder);
        } else if (view.getText().toString().contentEquals("硬编码")) {
            mPublisher.switchToHardEncoder();
            view.setText(R.string.soft_encoder);
        }
    }

    private void handleException(Exception e) {
        try {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            mPublisher.stopPublish();
            mPublisher.stopRecord();
        } catch (Exception e1) {
            //
        }
    }

    /**
     * 实现1srcEncoderHandler弱网络
     */
    @Override
    public void onNetworkWeak() {
        //弱网络
        Toast.makeText(context, "Network weak", Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现2srcEncoderHandler网络恢复
     */
    @Override
    public void onNetworkResume() {
        //网络恢复
        Toast.makeText(context, "Network resume", Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现3srcEncoderHandler 处理编码异常
     */
    @Override
    public void onEncodeIllegalArgumentException(IllegalArgumentException e) {

        handleException(e);
    }

    /**
     * 实现1RtmpHandler
     *
     * @param msg
     */
    @Override
    public void onRtmpConnecting(String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现2RtmpHandler
     *
     * @param msg
     */
    @Override
    public void onRtmpConnected(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现3RtmpHandler
     */
    @Override
    public void onRtmpVideoStreaming() {

    }

    /**
     * 实现4RtmpHandler
     */
    @Override
    public void onRtmpAudioStreaming() {

    }

    /**
     * 实现5RtmpHandler
     */
    @Override
    public void onRtmpStopped() {
        //推流停止
        //Toast.makeText(context, "Stopped", Toast.LENGTH_SHORT).show();

    }

    /**
     * 实现6RtmpHandler
     */
    @Override
    public void onRtmpDisconnected() {
        //断开连接
        Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现7RtmpHandler
     */
    @Override
    public void onRtmpVideoFpsChanged(double fps) {
        //fps
        Log.i(TAG, String.format("Output Fps: %f", fps));
    }

    /**
     * 实现8RtmpHandler
     */
    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Video bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Video bitrate: %d bps", rate));
        }
    }

    /**
     * 实现9RtmpHandler
     */
    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {
        int rate = (int) bitrate;
        if (rate / 1000 > 0) {
            Log.i(TAG, String.format("Audio bitrate: %f kbps", bitrate / 1000));
        } else {
            Log.i(TAG, String.format("Audio bitrate: %d bps", rate));
        }
    }

    /**
     * 实现10RtmpHandler
     */
    @Override
    public void onRtmpSocketException(SocketException e) {
            handleException(e);
    }

    /**
     * 实现11RtmpHandler
     */
    @Override
    public void onRtmpIOException(IOException e) {
              handleException(e);
    }

    /**
     * 实现12RtmpHandler
     */
    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {
              handleException(e);
    }

    /**
     * 实现13RtmpHandler
     */
    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {
               handleException(e);
    }
    ////////////////////////////////////////////////////////////////////

    /**
     * 实现1SrsRecordHandler
     */
    @Override
    public void onRecordPause() {
        Toast.makeText(context, "Record paused", Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现2SrsRecordHandler
     */
    @Override
    public void onRecordResume() {
        Toast.makeText(context, "Record resumed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现3SrsRecordHandler
     */
    @Override
    public void onRecordStarted(String msg) {
        Toast.makeText(context, "Recording file: " + msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现4SrsRecordHandler
     */
    @Override
    public void onRecordFinished(String msg) {
        Toast.makeText(context, "MP4 file saved: " + msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 实现5SrsRecordHandler
     */
    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {
        handleException(e);
    }

    /**
     * 实现6SrsRecordHandler
     */
    @Override
    public void onRecordIOException(IOException e) {

    }

    /**
     * 恢复录制
     */
    public void resumeRecord() {
        mPublisher.resumeRecord();
    }

    public void pauseRecord() {
        mPublisher.pauseRecord();

    }

    public void release() {
        mPublisher.stopPublish();
        mPublisher.stopRecord();
    }
}
