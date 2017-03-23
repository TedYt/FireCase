package com.hytera.fcls.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

import com.hytera.fcls.R;
import com.hytera.fcls.comutil.AppInfoUtils;
import com.hytera.fcls.comutil.CheckVersionUtil;
import com.hytera.fcls.presenter.SettingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends BaseActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        settingPresenter = new SettingPresenter(this);
        tv_version.setText("(" + AppInfoUtils.getPackageVersion(this) + ")");
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
                CheckVersionUtil.startCheckAndDown(this);
                break;
        }
    }
}
