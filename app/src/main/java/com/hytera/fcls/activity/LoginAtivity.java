package com.hytera.fcls.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hytera.fcls.ILogin;
import com.hytera.fcls.R;
import com.hytera.fcls.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tim on 17/2/25.
 */

public class LoginAtivity extends Activity implements ILogin {

    private static final String TAG = "y20650" + LoginAtivity.class.getSimpleName();

    @BindView(R.id.login_btn)
    protected Button login_btn;
    @BindView(R.id.login_password)
    protected EditText login_password;
    @BindView(R.id.login_username)
    protected EditText login_username;
    @BindView(R.id.test_response_body)
    protected TextView forTest;

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

        loginPresenter = new LoginPresenter(this);

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
    }

    @OnClick(R.id.login_btn)
    public void onClickAction(View view){
        Log.i(TAG, "onClickAction");
        loginPresenter.Login();

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
        Toast.makeText(this, "LoginFailed", Toast.LENGTH_SHORT).show();
    }

    /**
     * 辅助类
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
