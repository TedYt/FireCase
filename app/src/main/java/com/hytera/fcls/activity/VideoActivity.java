package com.hytera.fcls.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hytera.fcls.IVideo;
import com.hytera.fcls.R;
import com.hytera.fcls.presenter.VideoPresenter;

import net.ossrs.yasea.SrsCameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends BaseActivity implements IVideo {

    @BindView(R.id.glsurfaceview_camera)
    public SrsCameraView srsCameraView;
    @BindView(R.id.bt_publish)
    public Button bt_publish;
    @BindView(R.id.bt_swCam)
    public Button bt_swam;
    @BindView(R.id.bt_record)
    public Button bt_record;
    @BindView(R.id.bt_swEnc)
    public Button bt_swEnc;
    VideoPresenter videoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoPresenter = new VideoPresenter(this, this);
        ButterKnife.bind(this);
        videoPresenter.initPublisher(srsCameraView);
    }

    @OnClick({R.id.bt_publish, R.id.bt_swCam})
    public void onclick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_publish:
                //执行推流
                videoPresenter.Publish(bt_publish);
                break;
            case R.id.bt_swCam:
                //转换摄像头
                videoPresenter.switchCamera();
                break;
            case R.id.bt_record:
                //录像
                videoPresenter.Record(bt_record);
                break;
            case R.id.bt_swEnc:
                //转换加密方式
                videoPresenter.switchEncode(bt_swEnc);
                break;
        }

    }

    /**
     * 恢复时调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        bt_publish.setEnabled(true);
        videoPresenter.resumeRecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPresenter.pauseRecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPresenter.release();
    }
}
