package com.hytera.fcls.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.hytera.fcls.IMainAtv;
import com.hytera.fcls.activity.MainActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tim on 17/2/25.
 */

public class MainAtvPresenter {

    public static final String TAG = "y20650" + MainAtvPresenter.class.getSimpleName();

    private final int IMAGE_WIDTH = 720;
    private final int IMAGE_HEIGHT = 1080;

    private IMainAtv iMainAtv;

    private Context context;

    public MainAtvPresenter(MainActivity mainActivity, IMainAtv iMainAtv) {
        context = mainActivity;
        this.iMainAtv = iMainAtv;
    }

    public void startCamera(MainActivity context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File out = new File(getPhotoPath());
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, 1);
    }

    private String getPhotoPath() {
        String filepath = "";
        String pathUri = Environment.getExternalStorageDirectory() + "/fireDispatcher/";
        String imageName = "imageTest" + ".jpg";
        File file = new File(pathUri);
        file.mkdirs();
        filepath = pathUri + imageName;
        return filepath;
    }

    public Bitmap getBitmapFromCamera() {
        //Drawable drawable = BitmapDrawable.createFromPath(getPhotoPath());
        Bitmap bitmap = getBitmapFromUrl(getPhotoPath());
        if (bitmap != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            int size = bitmap.getByteCount();
            Log.i(TAG, "get return image, w = " + w + ", h = " + h
                    + "， size = " + size);
        } else {
            Log.i(TAG, "NOT get return image");
        }
        return bitmap;
    }

    private Bitmap getBitmapFromUrl(String uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(uri);
        // 防止OOM发生
        options.inJustDecodeBounds = false;
        int width = bitmap.getWidth();
        int height =  bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scalewWidth = 1;
        float scaleHeight = 1;

        if (width <= height){
            scalewWidth = IMAGE_WIDTH*1.0f/width;
            scaleHeight = IMAGE_HEIGHT*1.0f/height;
        }else {
            scalewWidth = IMAGE_HEIGHT*1.0f/width;
            scaleHeight = IMAGE_WIDTH*1.0f/height;
        }
        Log.i(TAG, "scalewWidth = "+ scalewWidth + ", scaleHeight = " + scaleHeight);
        matrix.postScale(scalewWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,
                0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return newBitmap;
    }

    private String getCurDateStr() {
        Date date = new Date(System.currentTimeMillis());
        String timeStr = new SimpleDateFormat("yyyMMdd_hhmmss", Locale.CHINA).format(date);
        Log.i(TAG, "timeStr is : " + timeStr);
        return timeStr;
    }

    /*public void initMQTT(MainActivity mainActivity) {
        MQTT mqtt = new MQTT(mainActivity);
        EventBus.getDefault().register(mainActivity);
    }*/

}
