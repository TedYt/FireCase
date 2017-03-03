package com.hytera.fcls.presenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tim on 17/3/2.
 */

public class HTTPPresenter {

    /** 单任务线程池 */
    private static ExecutorService singleThreadExecutor =
            Executors.newSingleThreadExecutor();

    public interface CallBack{
        void onResponse(String response);
    }

    /**
     * 对NetUtils 中的get方法进行封装
     * @param url
     * @param callback
     */
    public static void get(final String url, final CallBack callback){
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final String response = NetUtils.get(url);
                callback.onResponse(response);
            }
        });
    }

    /**
     * 对NetUtils总的post方法进行封装
     * @param url
     * @param content
     * @param callBack
     */
    public static void post(final String url, final String content, final CallBack callBack){
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final String response = NetUtils.post(url, content);
                callBack.onResponse(response);
            }
        });
    }
}
