package com.hytera.fcls.presenter;

import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Tim on 17/3/2.
 */


/**
 * 控制警情的铃声
 * 在FireService中响铃，
 * 在MainActivity中停止
 */
public class MPPresenter extends MediaPlayer {

    public static final String TAG = "y20650" + MPPresenter.class.getSimpleName();

    private static MPPresenter _instance;

    public static MPPresenter getInstance(){
        if (_instance == null){
            synchronized (MPPresenter.class){
                if (_instance == null){
                    _instance = new MPPresenter();
                }
            }
        }
        return _instance;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        Log.i(TAG, "start playing media !");
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        Log.i(TAG, "Stop playing media !");
        _instance.reset();// 不加这句话的话，只有第一次有media才能正常播放
    }
}
