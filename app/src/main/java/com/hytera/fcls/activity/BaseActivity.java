package com.hytera.fcls.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.hytera.fcls.FireApplication;

/**
 * Created by cctv on 2017/3/10.
 */

public class BaseActivity extends Activity {
    private FireApplication application;
    private  BaseActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(application == null){
//            application = (FireApplication) getApplication();
            application = FireApplication.getInstance();
        }
            context =this;
            addActivity();
    }

    /**
     * 添加Activity的方法
     */
    public void addActivity() {
        application.addActivity(context);
    }

    /**
     * 删除Activity
     */
    public void removeActivity(){
        application.removeActivity(context);
    }
    /**
     * 删除所有Activity
     */
    public void removeAllActivity(){
        application.removeAllActivity();
    }

    /*
     把Toast定义成一个方法  可以重复使用，
     */
    public void show_Toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
