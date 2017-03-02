package com.hytera.fcls.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hytera.fcls.service.FireService;
import com.hytera.fcls.IMainAtv;
import com.hytera.fcls.R;
import com.hytera.fcls.mqtt.MQTT;
import com.hytera.fcls.mqtt.event.MessageEvent;
import com.hytera.fcls.presenter.MainAtvPresenter;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity implements IMainAtv {

    public static final String TAG = "y20650" + MainActivity.class.getSimpleName();

    private static final String ServerIP = "192.168.43.22"; // 测试地址
    private static final String PORTID = "1883"; // MQTT 协议的对应的端口
    private static final String ClientID = "y20650";
    @BindView(R.id.textview)
    public TextView textView;



    @BindView(R.id.image_view)
    public ImageView imageView;

    @BindView(R.id.main_gridview_func)
    public GridView gridView;
    private MainAtvPresenter mainPresenter;

    private static MqttAndroidClient client;
    private MQTT mqtt;
    String[] fuc_names = new String[] { "拍照", "视频", "出发", "确认到达", "结束火警",
            "信息采集" };
    int[] fuc_icons = new int[] { R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        textView.setText("ButterKnife");

        Intent intent1 = new Intent(this, FireService.class);
        startService(intent1);

        mainPresenter = new MainAtvPresenter(this, this);
        //mainPresenter.initMQTT(this);
        EventBus.getDefault().register(this); // 订阅消息总线
        initView();
    }
    //初始化布局资源
    private void initView() {
        gridView.setAdapter(new GridAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0 :
                        Toast.makeText(MainActivity.this, "照相", Toast.LENGTH_SHORT).show();
                        //降低应用支持版本22，否则动态权限在华为7.0获取不成功
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA )!= PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    2);
                        }else {

                            mainPresenter.startCamera(MainActivity.this);
                        }

                        break;
                    case 1 :
                        Toast.makeText(MainActivity.this, "视频", Toast.LENGTH_SHORT).show();
                        break;
                    case 2 :
                        Toast.makeText(MainActivity.this, "出发", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,DriveRoutesActivity.class);
                        startActivity(intent);
                        break;
                    case 3 :
                        Toast.makeText(MainActivity.this, "确认到达", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "结束火警", Toast.LENGTH_SHORT).show();
                        break;
                    case 5 :
                        Toast.makeText(MainActivity.this, "信息采集", Toast.LENGTH_SHORT).show();
                        break;
                }
//                Toast.makeText(MainActivity.this, "ssd?dd+"+i+"====L?"+l, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.textview)
    public void onClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginAtivity.class);
                startActivity(intent);
            }
        }).start();

//        mainPresenter.startCamera(this);
        //startConnect(ClientID,ServerIP,PORTID);
    }

    private void initMQTT() {
        //mqtt = new MQTT(this, this);
        //mqtt.startConnect(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        Log.i(TAG, "getMessage from MQ : " + event.getString()
                + ", topic is : " + event.getTopic());
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(client!=null) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void updateLocation(double latitude, double longitude) {

    }

    @Override
    public void showLogInMain(String s) {

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
            Holer holer;
              if (convertview == null){
                  holer = new Holer();
                  convertview = View.inflate(MainActivity.this,R.layout.main_function_item,null);
                  holer.grid_item_text = (TextView) convertview.findViewById(R.id.grid_item_text);
                  holer.grid_item_image = (ImageView) convertview.findViewById(R.id.grid_item_image);

                  convertview.setTag(holer);
              }else{
                  holer = (Holer) convertview.getTag();
              }
              //设置item的标题文本
              holer.grid_item_image.setImageResource(fuc_icons[i]);
              holer.grid_item_text.setText(fuc_names[i]);
              return convertview;
          }
      }
    class Holer {
        TextView grid_item_text;
        ImageView grid_item_image;
    }
    /*private void startConnect(String clientID, String serverIP, String port) {
        //服务器地址
        String  uri ="tcp://";
        uri=uri+serverIP+":"+port;
        Log.d(TAG, uri+"  "+clientID);
        *//**
         * 连接的选项
         *//*
        MqttConnectOptions conOpt = new MqttConnectOptions();
        *//**设计连接超时时间*//*
        conOpt.setConnectionTimeout(3000);
        *//**设计心跳间隔时间300秒*//*
        conOpt.setKeepAliveInterval(300);
        *//**
         * 创建连接对象
         *//*
        client = new MqttAndroidClient(this,uri, clientID);
        *//**
         * 连接后设计一个回调
         *//*
        client.setCallback(new MqttCallbackHandler(this, clientID));
        *//**
         * 开始连接服务器，参数：ConnectionOptions,  IMqttActionListener
         *//*
        try {
            client.connect(conOpt, null, new ConnectCallBackHandler(this, imqConn));
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }*/
}
