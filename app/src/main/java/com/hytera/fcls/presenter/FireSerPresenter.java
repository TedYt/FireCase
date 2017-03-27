package com.hytera.fcls.presenter;

import com.hytera.fcls.DataUtil;
import com.hytera.fcls.IFireService;
import com.hytera.fcls.comutil.Log;

/**
 * Created by Tim on 17/3/16.
 *
 * 为了让FireService与MainAtvPresenter沟通
 * 结束警情时，在Service里能重置notification
 */

public class FireSerPresenter {

    public static final String TAG = DataUtil.BASE_TAG + "FireSerPresenter";

    private static FireSerPresenter _instance;
    private IFireService iFireService;

    public static FireSerPresenter getInstance(){
        if (_instance == null){
            synchronized (FireSerPresenter.class){
                if (_instance == null){
                    _instance = new FireSerPresenter();
                }
            }
        }

        return _instance;
    }

    public void setInterFace (IFireService interFace){
        iFireService = interFace;
    }

    /**
     * 点击结束警情时 会调用
     */
    public void onCaseFinish(){
        if (iFireService == null){
            Log.i(TAG, "iFireService is NOT initialized");
            return;
        }

        iFireService.onCaseFinished();
    }
}
