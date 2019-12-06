package com.bailun.video;

import android.widget.SeekBar;

/**
 * Created by jiangwensong on 2019/11/26.
 * description：控制器点击等操作回调
 */
public interface IMediaControllerAction {
    void playVideo();
    void pauseVideo();
    void nextVideo();
    void videoSelect();
    boolean fullScreen();
    boolean cancelScreen();

    void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
    void onStartTrackingTouch(SeekBar seekBar);
    void onStopTrackingTouch(SeekBar seekBar);
}
