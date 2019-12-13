package com.bailun.video;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by jiangwensong on 2019/12/6.
 * description：
 */
public interface IVideoView {

    void start();

    void pause();

    void seekTo(long msec);

    boolean isPlaying();

    int getBufferPercentage();

    long getCurrentPosition();

    long getDuration();

    void stopPlayback();

    interface OnInfoListener {
        boolean onInfo(IVideoView var1, int var2, int var3);
    }

    interface OnErrorListener {
        boolean onError(IVideoView var1, int var2, int var3);
    }

     interface OnBufferingUpdateListener {
        void onBufferingUpdate(IVideoView var1, int var2);
    }

     interface OnCompletionListener {
        void onCompletion(IVideoView var1);
    }

     interface OnPreparedListener {
        void onPrepared(IVideoView var1);
    }
}
