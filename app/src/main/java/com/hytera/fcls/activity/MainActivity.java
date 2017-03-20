package com.hytera.fcls.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IMainAtv;
import com.hytera.fcls.R;
import com.hytera.fcls.presenter.MainAtvPresenter;
import com.hytera.fcls.service.FireService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainAtv {

    public static final String TAG = "y20650" + MainActivity.class.getSimpleName();

    @BindView(R.id.image_wave)
    public ImageView image_wave;
    @BindView(R.id.image_view)
    public ImageView imageView;
    @BindView(R.id.case_info_detail)
    public TextView case_info;
    @BindView(R.id.case_info_deptname)
    public TextView case_info_deptname;
    @BindView(R.id.case_info_level)
    public TextView case_info_level;

    private MainAtvPresenter mainPresenter;

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
        // 接受警情后，获取警情的一些信息
        if (DataUtil.isAcceptCase()){
            mainPresenter.getFireCaseInfo();
        }
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
        R.id.case_upload_photo, R.id.case_upload_video, R.id.case_detail})
    protected void caseFunClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.case_upload_photo:
                mainPresenter.startCamera(MainActivity.this);
                break;
            case R.id.case_upload_video:
                mainPresenter.goVideoActivity();
                break;
            case R.id.case_depart:
                mainPresenter.depart();
                break;
            case R.id.case_arrive:
                mainPresenter.arriveDest();
                break;
            case R.id.case_end_fire:
                mainPresenter.endWaveAnim(image_wave);
                mainPresenter.finishCase();
                break;
            case R.id.case_setting:
                mainPresenter.goSettingActivity();
                break;
            case R.id.case_detail:
                mainPresenter.showDetail();
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
     * @param levelDesc
     * @param caseDesc
     * @param deptName
     */
    @Override
    public void showFireCaseInfo(String levelDesc, String caseDesc, String deptName) {

        imageView.setBackground(getResources().getDrawable(R.drawable.banner));
        case_info.setVisibility(View.VISIBLE);
        case_info_deptname.setVisibility(View.VISIBLE);
        case_info_level.setVisibility(View.VISIBLE);

        case_info.setText(caseDesc);
        case_info_deptname.setText(deptName);
        case_info_level.setText(levelDesc);

        mainPresenter.playWaveAnim(image_wave);
    }

    /**
     * 警情结束时，显示 消防救援 的标题
     */
    @Override
    public void showTitle() {
        imageView.setBackground(getResources().getDrawable(R.drawable.main_ditu));
        case_info.setVisibility(View.GONE);
        case_info_deptname.setVisibility(View.GONE);
        case_info_level.setVisibility(View.GONE);
    }

    @Override
    public void showFinishCaseDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.str_main_item_finish_case)
                .setMessage(R.string.confirm_finish_case)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mainPresenter.confirmFinishCase();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void showNavDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.navigation)
                .setMessage(R.string.need_navigation_or_not)
                .setPositiveButton(R.string.need, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mainPresenter.launchNav();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.no_need, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mainPresenter.justPostArrState();
                        dialog.dismiss();
                    }
                });
        dialog.show();
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
