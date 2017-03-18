package com.hytera.fcls.activity;

import android.os.Bundle;
import android.util.Log;
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
    public Button btpublish;
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
        Log.i("ddd","111111");
        switch (id) {
            case R.id.bt_publish:
                this.show_Toast("????");
                //执行推流
                videoPresenter.startPublish();
                Log.i("ddd","1112222111");
                break;
            case R.id.bt_swCam:
                //转换摄像头
                videoPresenter.switchCamera();
                break;
            case R.id.bt_record:
                //转换摄像头
                videoPresenter.beginRecord(bt_record);
                break;
            case R.id.bt_swEnc:
                //转换加密方式
                videoPresenter.switchEncode(bt_swEnc);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
