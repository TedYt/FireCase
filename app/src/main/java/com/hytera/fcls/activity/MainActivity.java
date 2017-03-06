package com.hytera.fcls.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hytera.fcls.IMainAtv;
import com.hytera.fcls.R;
import com.hytera.fcls.presenter.MPPresenter;
import com.hytera.fcls.presenter.MainAtvPresenter;
import com.hytera.fcls.service.FireService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements IMainAtv {

    public static final String TAG = "y20650" + MainActivity.class.getSimpleName();

    @BindView(R.id.textview)
    public TextView textView;
    @BindView(R.id.image_view)
    public ImageView imageView;
    @BindView(R.id.main_gridview_func)
    public GridView gridView;
    private MainAtvPresenter mainPresenter;

    String[] fuc_names = new String[] { "拍照", "视频", "出发", "确认到达", "不在本辖区",
            "警情信息" };
    int[] fuc_icons = new int[] {
            R.drawable.takephoto,
            R.drawable.videoshow,
            R.drawable.nav,
            R.drawable.confirmarrival,
            R.drawable.chakan,
            R.drawable.fireinformation,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        textView.setText("ButterKnife");// 测试

        Intent intent1 = new Intent(this, FireService.class);
        startService(intent1);

        mainPresenter = new MainAtvPresenter(this, this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        MediaPlayer mediaPlayer = MPPresenter.getInstance();
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    //初始化布局资源
    private void initView() {
        gridView.setAdapter(new GridAdapter());
        gridView.setOnItemClickListener(new GridViewOnItemClick());
    }

    @OnClick(R.id.textview)
    public void onClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }).start();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            String sdStatus = Environment.getExternalStorageState();
            if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                Log.e(TAG, "SD card is not available right now.");
            }
            Bitmap bitmap = mainPresenter.getBitmapFromCamera();
            imageView.setImageBitmap(bitmap);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDestroy();
    }

    @Override
    public void updateLocation(double latitude, double longitude) {
        textView.setText(latitude + ", " + longitude);// 测试
    }

    @Override
    public void showLogInMain(String s) {
        textView.setText(s);// 测试
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

    class GridAdapter extends BaseAdapter {

          @Override
          public int getCount() {
              return fuc_names.length;
          }

          @Override
          public Object getItem(int i) {
              return null;
          }

          @Override
          public long getItemId(int i) {
              return 0;
          }

          @Override
          public View getView(int i, View convertview, ViewGroup viewGroup) {
            Holder holder;
              if (convertview == null){
                  holder = new Holder();
                  convertview = View.inflate(MainActivity.this,R.layout.main_function_item,null);
                  holder.grid_item_text = (TextView) convertview.findViewById(R.id.grid_item_text);
                  holder.grid_item_image = (ImageView) convertview.findViewById(R.id.grid_item_image);

                  convertview.setTag(holder);
              }else{
                  holder = (Holder) convertview.getTag();
              }
              //设置item的标题文本
              holder.grid_item_image.setImageResource(fuc_icons[i]);
              holder.grid_item_text.setText(fuc_names[i]);
              return convertview;
          }
      }
    class Holder {
        TextView grid_item_text;
        ImageView grid_item_image;
    }

    class GridViewOnItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0 :
                    Toast.makeText(MainActivity.this, "照相", Toast.LENGTH_SHORT).show();
                    if (!checkCameraPermissoin()){
                        return;
                    }
                    mainPresenter.startCamera(MainActivity.this);

                    break;
                case 1 :
                    Toast.makeText(MainActivity.this, "视频", Toast.LENGTH_SHORT).show();
                    break;
                case 2 :
                    Toast.makeText(MainActivity.this, "出发", Toast.LENGTH_SHORT).show();
                    // 获取之前定位位置，如果之前未曾定位，则重新定位
                    //应先判断是否有gps信息数据，没有应该不开启
                    mainPresenter.depart();
                    break;
                case 3 :
                    Toast.makeText(MainActivity.this, "确认到达", Toast.LENGTH_SHORT).show();
                    mainPresenter.arriveDest();
                    break;
                case 4:
                    Toast.makeText(MainActivity.this, "结束火警", Toast.LENGTH_SHORT).show();
                    mainPresenter.closeCase();
                    break;
                case 5 :
                    Toast.makeText(MainActivity.this, "信息采集: 待优化", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
