package com.hytera.fcls.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hytera.fcls.IMainAtv;
import com.hytera.fcls.R;
import com.hytera.fcls.presenter.MainAtvPresenter;
import com.hytera.fcls.service.FireService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements IMainAtv {

    public static final String TAG = "y20650" + MainActivity.class.getSimpleName();

    @BindView(R.id.image_wave)
    public ImageView image_wave;
    @BindView(R.id.image_view)
    public ImageView imageView;
    @BindView(R.id.case_info_detail)
    public TextView case_info;

    private MainAtvPresenter mainPresenter;

    String[] fuc_names = new String[] { "拍照", "视频", "出发", "确认到达", "结束警情",
            "设置" };
    int[] fuc_icons = new int[] {
            R.drawable.sel_1_upload_photo,
            R.drawable.sel_1_upload_video,
            R.drawable.sel_1_depart,
            R.drawable.sel_1_confrim_arrival,
            R.drawable.sel_1_endfire,
            R.drawable.sel_1_setting,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Intent intent1 = new Intent(this, FireService.class);
        startService(intent1);

        mainPresenter = new MainAtvPresenter(this, this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        mainPresenter.stopFireAlarm();
        mainPresenter.getFireCaseInfo();
    }

    //初始化布局资源
    private void initView() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mainPresenter.CAMERA_RESULT && resultCode == RESULT_OK){
            String sdStatus = Environment.getExternalStorageState();
            if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                Log.e(TAG, "SD card is not available right now.");
            }
            mainPresenter.postImage();
        }
    }

    @OnClick(R.id.image_wave)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.image_wave:
                mainPresenter.play(image_wave);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        /**
         * 按返回键时，启动home界面
         */
        Intent intent =  new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }

    @OnClick({R.id.case_arrive, R.id.case_depart, R.id.case_end_fire, R.id.case_setting,
        R.id.case_upload_photo, R.id.case_upload_video})
    protected void caseFunClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.case_upload_photo:
                Toast.makeText(MainActivity.this, "照相", Toast.LENGTH_SHORT).show();
                mainPresenter.startCamera(MainActivity.this);
                break;
            case R.id.case_upload_video:
                Toast.makeText(MainActivity.this, "视频", Toast.LENGTH_SHORT).show();
                mainPresenter.depart();
                break;
            case R.id.case_depart:
                Toast.makeText(MainActivity.this, "出发", Toast.LENGTH_SHORT).show();
                mainPresenter.depart();
                break;
            case R.id.case_arrive:
                Toast.makeText(MainActivity.this, "到达", Toast.LENGTH_SHORT).show();
                mainPresenter.arriveDest();
                break;
            case R.id.case_end_fire:
                Toast.makeText(MainActivity.this, "结束火警", Toast.LENGTH_SHORT).show();
                mainPresenter.endPlayAnim(image_wave);
                mainPresenter.finishCase();
                break;
            case R.id.case_setting:
                Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    @Override
    public void updateLocation(double latitude, double longitude) {

    }

    @Override
    public void showLogInMain(String s) {

    }

    /**
     * 在主界面显示警情
     * @param s
     */
    @Override
    public void showFireCaseInfo(String s) {
        case_info.setText(s);
    }

    private boolean checkCameraPermissoin(){
        //降低应用支持版本22，否则动态权限在华为7.0获取不成功
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA )
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 2:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mainPresenter.startCamera(MainActivity.this);
                }else {
                    Toast.makeText(MainActivity.this, "权限未开启", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
