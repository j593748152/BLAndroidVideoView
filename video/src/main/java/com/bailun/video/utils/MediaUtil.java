package com.bailun.video.utils;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;


/**
 * Created by jiangwensong on 2019/10/31.
 * description：
 */
public class MediaUtil {

    private static final long SECOND = 1000;
    private static final long MINUTE = SECOND * 60;
    private static final long HOUR = MINUTE * 60;

    //视频时间显示格式
    public static String getTime(long time) {
        int h = (int) (time / HOUR);
        int m = (int) ((time - h * HOUR) / MINUTE);
        int s = (int) ((time - h * HOUR - m * MINUTE) / 1000);
        StringBuilder builder = new StringBuilder();
        if (h > 0) {
            builder.append(h).append(':').append(m > 9 ? m : ("0" + m)).append(':').append(s > 9 ? s : ("0" + s));
        } else if (m > 0) {
            builder.append(m > 9 ? m : ("0" + m)).append(':').append(s > 9 ? s : ("0" + s));
        } else {
            builder.append("00").append(':').append(s > 9 ? s : ("0" + s));
        }
        return builder.toString();
    }

    private static AudioFocusRequest mAudioFocusRequest;
    private static AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;

    /**
     * 获取音频焦点
     * @param audioManager
     * @param audioFocusChangeListener
     * @return 音频焦点状态
     */
    public static int requestAudioFocus(AudioManager audioManager, AudioManager.OnAudioFocusChangeListener audioFocusChangeListener){
        if (audioManager == null){
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        }
        mOnAudioFocusChangeListener = audioFocusChangeListener;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mAudioFocusRequest == null){
                mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MOVIE).build())
                        .setOnAudioFocusChangeListener(audioFocusChangeListener)
                        .build();
            }

            return audioManager.requestAudioFocus(mAudioFocusRequest);
        } else {
            return audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    /**
     * 放弃音频焦点
     * @param audioManager
     */
    public static void abandonAudioFocus(AudioManager audioManager){
        if (audioManager == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mAudioFocusRequest == null){
                audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            }else {
                audioManager.abandonAudioFocusRequest(mAudioFocusRequest);
            }
        }else {
            audioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }


    /**
     * 时间与拖拽像素 的比例
     * @param videoDuration  视频总长
     * @return
     */
    public static long getTimeOfPixel(long videoDuration) {
        if (videoDuration < 30 * SECOND) {
            return videoDuration / ScreenUtil.getWidth();
        }else {
            return 2 * MINUTE / ScreenUtil.getWidth() ;
        }
    }
}
