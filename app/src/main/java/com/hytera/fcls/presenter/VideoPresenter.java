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
    String baseRtmpUrl = "rtmp://192.168.43.22:1935/";//传入rtmurl
    private String recPath = Environment.getExternalStorageDirectory().getPath() + "/test.mp4";

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
        mPublisher.setPreviewResolution(640, 480);
//        mPublisher.setPreviewResolution(1280, 720);
        mPublisher.setOutputResolution(640, 480);///输出这个效果好些
//        mPublisher.setOutputResolution(1280, 720);
        mPublisher.setVideoHDMode();
    }

    private String getCurDateStr() {
        Date date = new Date(System.currentTimeMillis());
        String timeStr = new SimpleDateFormat("yyyMMdd_hhmmss", Locale.CHINA).format(date);
        Log.i(TAG, "timeStr is : " + timeStr);
        return timeStr;
    }

    public void Publish(TextView view) {

        if (view.getText().toString().contentEquals("开始")){
            SharedPreferences sharedPreferences = context.getSharedPreferences(DataUtil.LOGIN_XML, 0);
            String staff_name = sharedPreferences.getString(DataUtil.KEY_STAFFNAME, "amdin");
            staff_name="";
            String usercode = sharedPreferences.getString(DataUtil.KEY_USERCODE, "0000");
            String rtmpUrl = baseRtmpUrl + staff_name + "_" + usercode + "/" + getCurDateStr();

            Log.d(TAG, "rtmurl：" + rtmpUrl);
//        mPublisher.Publish(rtmpUrl);
//        mPublisher.startCamera();
            mPublisher.startPublish(rtmpUrl);
            mPublisher.startCamera();
            Log.d(TAG, "开始");
            view.setText("停止");
        }else if (view.getText().toString().contentEquals("停止")){
            mPublisher.stopPublish();
            mPublisher.stopRecord();

            Log.d(TAG, "停止");
            view.setText("开始");
        }
        /* if (btnPublish.getText().toString().contentEquals("publish")) {
                    rtmpUrl = efu.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("rtmpUrl", rtmpUrl);
                    editor.apply();

                    mPublisher.Publish(rtmpUrl);
                    mPublisher.startCamera();

                    if (btnSwitchEncoder.getText().toString().contentEquals("soft encoder")) {
                        Toast.makeText(getApplicationContext(), "Use hard encoder", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Use soft encoder", Toast.LENGTH_SHORT).show();
                    }
                    btnPublish.setText("stop");
                    btnSwitchEncoder.setEnabled(false);
                } else if (btnPublish.getText().toString().contentEquals("stop")) {
                    mPublisher.stopPublish();
                    mPublisher.stopRecord();
                    btnPublish.setText("publish");
                    btnRecord.setText("record");
                    btnSwitchEncoder.setEnabled(true);
                }*/
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
//            btnPublish.setText("publish");
//            btnRecord.setText("record");
//            btnSwitchEncoder.setEnabled(true);
        } catch (Exception e1) {
            //
        }
    }

    /**
     * 实现1srcEncoderHandler
     */
    @Override
    public void onNetworkWeak() {

    }

    /**
     * 实现2srcEncoderHandler
     */
    @Override
    public void onNetworkResume() {

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

    }

    /**
     * 实现2RtmpHandler
     *
     * @param msg
     */
    @Override
    public void onRtmpConnected(String msg) {

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

    }

    /**
     * 实现6RtmpHandler
     */
    @Override
    public void onRtmpDisconnected() {

    }

    /**
     * 实现7RtmpHandler
     */
    @Override
    public void onRtmpVideoFpsChanged(double fps) {

    }

    /**
     * 实现8RtmpHandler
     */
    @Override
    public void onRtmpVideoBitrateChanged(double bitrate) {

    }

    /**
     * 实现9RtmpHandler
     */
    @Override
    public void onRtmpAudioBitrateChanged(double bitrate) {

    }

    /**
     * 实现10RtmpHandler
     */
    @Override
    public void onRtmpSocketException(SocketException e) {

    }

    /**
     * 实现11RtmpHandler
     */
    @Override
    public void onRtmpIOException(IOException e) {

    }

    /**
     * 实现12RtmpHandler
     */
    @Override
    public void onRtmpIllegalArgumentException(IllegalArgumentException e) {

    }

    /**
     * 实现13RtmpHandler
     */
    @Override
    public void onRtmpIllegalStateException(IllegalStateException e) {

    }
    ////////////////////////////////////////////////////////////////////

    /**
     * 实现1SrsRecordHandler
     */
    @Override
    public void onRecordPause() {

    }

    /**
     * 实现2SrsRecordHandler
     */
    @Override
    public void onRecordResume() {

    }

    /**
     * 实现3SrsRecordHandler
     */
    @Override
    public void onRecordStarted(String msg) {

    }

    /**
     * 实现4SrsRecordHandler
     */
    @Override
    public void onRecordFinished(String msg) {

    }

    /**
     * 实现5SrsRecordHandler
     */
    @Override
    public void onRecordIllegalArgumentException(IllegalArgumentException e) {

    }

    /**
     * 实现6SrsRecordHandler
     */
    @Override
    public void onRecordIOException(IOException e) {

    }
}
