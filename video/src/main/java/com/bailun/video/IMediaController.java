package com.bailun.video;

/**
 * Created by jiangwensong on 2019/12/4.
 * description：BLVideoView回调部分
 */
public interface IMediaController {
    /**
     * 视频暂停，控制器更新界面，例如：显示控制器，暂停显示为播放,...
     */
    void mediaPaused();
    /**
     * 视频播放，控制器更新界面，例如：延时隐藏控制器，播放显示为暂停...
     */
    void mediaPlaying();
    /**
     * 设置时间进度条
     */
    void setProgress(int progress);
    /**
     * 设置时间进度条最大值
     */
    int getMaxProgress();
    /**
     * 显示控制器
     */
    void show();
    /**
     * 隐藏控制器
     */
    void hide();
}
