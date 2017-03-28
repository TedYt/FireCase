package com.hytera.fcls.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytera.fcls.ISetting;
import com.hytera.fcls.R;
import com.hytera.fcls.comutil.AppInfoUtils;
import com.hytera.fcls.comutil.CheckVersionUtil;
import com.hytera.fcls.comutil.Log;
import com.hytera.fcls.presenter.SettingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity implements ISetting {
    @BindView(R.id.tv_version)
    public TextView tv_version;
    @BindView(R.id.rl_checkupdate)
    public RelativeLayout rl_about;
    @BindView(R.id.exit_application)
    public RelativeLayout exit_application;
    private SettingPresenter settingPresenter;


    @BindView(R.id.server_ip)
    public EditText server_ip;
    @BindView(R.id.confirm_server_ip)
    public Button confirm_server_ip;
    @BindView(R.id.server_port)
    public EditText server_port;
    @BindView(R.id.mq_server_ip)
    public TextView mq_server_ip;
    boolean flag_loading = false;
    CheckVersionUtil checkVersionUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        settingPresenter = new SettingPresenter(this);
        tv_version.setText("(" + AppInfoUtils.getPackageVersion(this) + ")");
        checkVersionUtil = new CheckVersionUtil(this);
    }

    /**
     * 为了测试加的设置ip的接口
     * 正式发布时，可以删除
     *
     * @param view
     */

    @OnClick({R.id.exit_application, R.id.rl_checkupdate, R.id.confirm_server_ip})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.exit_application:
                settingPresenter.exit();
                Log.e("setting", "getInstance: 222222");
                break;
            case R.id.confirm_server_ip:
                Toast.makeText(this, "ip : " + server_ip.getText().toString()
                                + "， port : " + server_port.getText().toString()
                                + ", mq : " + mq_server_ip.getText().toString(),
                        Toast.LENGTH_LONG).show();
                finish();
                break;
            case R.id.rl_checkupdate:
//                检查更新
                if (flag_loading){
                    Toast.makeText(this,"检查更新中，请勿重复点击",Toast.LENGTH_SHORT).show();
                    return;
                }
                checkVersionUtil.startCheckAndDown(SettingActivity.this);
//                rl_about.setEnabled(false);
                rl_about.postDelayed(new Runnable() { // 防止快速点击，出现多个对话框
                    @Override
                    public void run() {
                        rl_about.setEnabled(true);
                    }
                }, 100);
                break;
        }
    }

    @Override
    public void btenable() {
        rl_about.setEnabled(true);
        flag_loading=false;
    }

    @Override
    public void btcancle() {
        rl_about.setEnabled(false);
        flag_loading=true;
    }
}
