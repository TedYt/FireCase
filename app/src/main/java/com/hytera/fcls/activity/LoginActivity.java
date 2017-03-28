package com.hytera.fcls.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hytera.fcls.ILogin;
import com.hytera.fcls.R;
import com.hytera.fcls.comutil.Log;
import com.hytera.fcls.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Tim on 17/2/25.
 */

public class LoginActivity extends BaseActivity implements ILogin {

    private static final String TAG = "y20650" + LoginActivity.class.getSimpleName();

    @BindView(R.id.login_btn)
    protected Button login_btn;
    @BindView(R.id.login_password)
    protected EditText login_password;
    @BindView(R.id.login_username)
    protected EditText login_username;
    @BindView(R.id.test_response_body)
    protected TextView forTest;
    @BindView(R.id.checkbox_remember_password)
    protected CheckBox remember_password;

    private LoginPresenter loginPresenter;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what){
                case 100:

                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_atv);

        ButterKnife.bind(this);

        loginPresenter = new LoginPresenter(this,this);

        initView();
    }

    private void initView() {
        login_btn.setEnabled(false);
        login_username.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                changeLoginBtnState();
            }
        });

        login_password.addTextChangedListener(new MyTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                changeLoginBtnState();
            }
        });

        /** 初始化姓名 密码 */
        boolean checked = loginPresenter.isCheckRemPas();
        remember_password.setChecked(checked);
        login_username.setText(loginPresenter.getUserCode());
        if (checked){
            login_password.setText(loginPresenter.getPassword());
        }
    }

    @Override
    public void onBackPressed() {
        /** 返回时保存用户名和密码 */
        saveLoginInfo();
        super.onBackPressed();
    }

    private void saveLoginInfo(){
        if (loginPresenter.isCheckRemPas()){
            loginPresenter.savePassword(login_password.getText().toString());
        }
        loginPresenter.saveUserCode(login_username.getText().toString());
    }

    @OnClick(R.id.login_btn)
    public void onClickAction(View view){
        Log.i(TAG, "onClickAction");
        /** 登录时保存用户名和密码 */
        saveLoginInfo();
        disableAllView();
        login_btn.setText(getString(R.string.logining));
        loginPresenter.Login(login_password.getText().toString(),
                    login_username.getText().toString());
    }

    /**
     * 辅助测试用
     * @param view
     */
    @OnClick(R.id.imageView)
    public void testToStartSetting(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void disableAllView() {
        login_username.setEnabled(false);
        login_password.setEnabled(false);
        remember_password.setEnabled(false);
        login_btn.setEnabled(false);
    }

    private void enableAllView(){
        login_username.setEnabled(true);
        login_password.setEnabled(true);
        remember_password.setEnabled(true);
        login_btn.setEnabled(true);
    }


    @OnCheckedChanged(R.id.checkbox_remember_password)
    protected void OnCheckedChanged(CompoundButton cb, boolean checked){
        Log.i(TAG, "OnCheckedChanged : " + checked);
        loginPresenter.onCheckedChange(checked);
    }

    public void changeLoginBtnState(){
        String s1 = login_username.getText().toString().trim();
        String s2 = login_password.getText().toString().trim();

        if (s1.length() != 0 && s2.length() != 0){
            login_btn.setEnabled(true);
        }else {
            login_btn.setEnabled(false);
        }
    }

    @Override
    public void LoginSuccess() {
        Log.i(TAG, "LoginSuccess");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        //Toast.makeText(this, "LoginSuccess", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void LoginFailed() {
        Log.i(TAG, "LoginFailed");
        enableAllView();
        login_btn.setText(R.string.login);
        //login_btn.setBackgroundColor(getResources().getColor(R.color.login_failed_color));
        Toast.makeText(this, R.string.login_failure, Toast.LENGTH_LONG).show();
    }

    /**
     * 辅助类,只是为了让代码看起来更简洁些
     */
    class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {}
    }

}
