package com.bailun.videodemo;

import android.app.Application;

import com.bailun.video.utils.ScreenUtil;

/**
 * Created by jiangwensong on 2019/12/5.
 * description：
 */
public class BaseAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.initScreen(this);
    }
}
