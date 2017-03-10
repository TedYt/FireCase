package com.hytera.fcls.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {

    @BindView(R.id.server_ip)
    public EditText server_ip;
    @BindView(R.id.confirm_server_ip)
    public Button confirm_server_ip;
    @BindView(R.id.server_port)
    public EditText server_port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    /**
     * 为了测试加的设置ip的接口
     * 正式发布时，可以删除
     * @param view
     */
    @OnClick(R.id.confirm_server_ip)
    public void onClick(View view){
        DataUtil.setServerIP(server_ip.getText().toString(), server_port.getText().toString());
        Toast.makeText(this, "ip : " + server_ip.getText().toString()
                        + "， port : " + server_port.getText().toString(),
                        Toast.LENGTH_LONG).show();
        finish();
    }
}
