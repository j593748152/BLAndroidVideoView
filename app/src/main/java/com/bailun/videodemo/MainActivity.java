package com.bailun.videodemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bailun.video.BLMediaController;
import com.bailun.video.BLVideoView;
import com.bailun.video.IMediaControllerAction;
import com.bailun.video.IVideoView;
import com.bailun.video.utils.MediaUtil;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity implements IMediaControllerAction, View.OnTouchListener {

    private static final String TAG = "MainActivity";


    static {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    BLVideoView mBLVideoView;
    BLMediaController mMediaController;
    ConstraintLayout constraintLayout;
    private Handler mHandler = new Handler();

    /**
     * 更新进度条，当前播放时间 线程
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mBLVideoView == null) {
                return;
            }
            mMediaController.setNowTimeText(MediaUtil.getTime(mBLVideoView.getCurrentPosition()));
            int percentage;
            if (mBLVideoView.getDuration() == 0) {
                percentage = 0;
            } else {
                percentage = (int) ((mBLVideoView.getCurrentPosition() * 1d / mBLVideoView.getDuration()) * mMediaController.getMaxProgress());
            }
            mMediaController.setProgress(percentage);
            mHandler.postDelayed(this, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mBLVideoView = findViewById(R.id.video_view);
        mMediaController = findViewById(R.id.media_controller);
        constraintLayout = findViewById(R.id.constraintLayout);
        constraintLayout.setOnTouchListener(this);

        mMediaController.setMediaAction(this);
        mBLVideoView.setMediaController(mMediaController);

        //视频准备播放
        mBLVideoView.setOnPreparedListener(new IVideoView.OnPreparedListener() {
            @Override
            public void onPrepared(IVideoView iMediaPlayer) {
                mMediaController.setNowTimeText(MediaUtil.getTime(iMediaPlayer.getCurrentPosition()));
                mMediaController.setAllTimeText(MediaUtil.getTime(iMediaPlayer.getDuration()));
                mBLVideoView.pause(); // start 或者 pause 作为初始状态
                startTiming();
            }
        });
        //视频完成播放
        mBLVideoView.setOnCompletionListener(new IVideoView.OnCompletionListener() {
            @Override
            public void onCompletion(IVideoView iMediaPlayer) {
            }
        });
        //视频播放错误
        mBLVideoView.setOnErrorListener(new IVideoView.OnErrorListener() {
            @Override
            public boolean onError(IVideoView iMediaPlayer, int i, int i1) {
                Log.e(TAG, "onError: " + "arg1" + i + "arg2" + i1);
                return true;
            }
        });
        //视频播放信息
        mBLVideoView.setOnInfoListener(new IVideoView.OnInfoListener() {
            @Override
            public boolean onInfo(IVideoView iMediaPlayer, int i, int i1) {
                Log.i(TAG, "onInfo: " + "arg1" + i + "arg2" + i1);
                if (IMediaPlayer.MEDIA_INFO_BUFFERING_START == i) {
                    //开始缓冲
                } else if (IMediaPlayer.MEDIA_INFO_BUFFERING_END == i) {
                    //缓存结束
                } else if (IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == i) {
                    //开始渲染画面
                } else if (IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START == i) {
                    //跳转时间，开始渲染画面
                }
                return false;
            }
        });
        //视频缓冲进度
        mBLVideoView.setOnBufferingUpdateListener(new IVideoView.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IVideoView var1, int var2) {
                Log.i(TAG, "onBufferingUpdate: " + "var2 " + var2);
                int percentage = var2 * mMediaController.getMaxProgress() / 100;
                mMediaController.setSecondaryProgress(percentage);
            }
        });
    }

    private void initData() {
        mBLVideoView.setVideoURL("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBLVideoView != null) {
            mBLVideoView.stopPlayback();
        }
    }

//***********************************
//
// 视频播放控制
//
// ***********************************

    @Override
    public void playVideo() {
        mBLVideoView.start();
    }

    @Override
    public void pauseVideo() {
        mBLVideoView.pause();
    }

    @Override
    public void nextVideo() {
        mBLVideoView.stopPlayback();
        mBLVideoView.setVideoURL("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", null);
    }

    @Override
    public void videoSelect() {
        //视频选集的按钮
    }

// ***********************************
//
// 全屏横竖屏控制
//
// ***********************************

    @Override
    public boolean fullScreen() {
        //全屏的按钮
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        return false;
    }

    @Override
    public boolean cancelScreen() {
        //取消全屏的按钮
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return false;
    }

    //Manifest需要添加   android:configChanges="orientation|screenSize"
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "orientation == " + newConfig.orientation);
        //是否竖屏
        boolean mIsPort = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        if (mIsPort) {
            //横屏控制器样式
            mMediaController.showType(0);
        } else {
            //竖屏控制器样式
            mMediaController.showType(1);
        }
    }

// ***********************************
//
// 进度条控制
//
// ***********************************
    private int progress;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.progress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //进度条触摸
        stopTiming();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //进度条结束触摸
        mBLVideoView.seekTo(mBLVideoView.getDuration() * progress / mMediaController.getMaxProgress());
        startTiming();
    }

    /**
     * 开始更新进度条
     */
    private void startTiming() {
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, 10);
    }

    /**
     * 停止更新进度条
     */
    private void stopTiming() {
        mHandler.removeCallbacks(runnable);
    }


    //屏幕拖拽，调节播放时间
    private long onTouchPlayTime;
    private float x;
    private boolean isChange;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mMediaController != null) {
            mMediaController.show();
        }
        //视频未准备好，不予操作进度条
        if (mBLVideoView == null || mBLVideoView.getDuration() == 0) {
            return true;
        }

        int eventAction = event.getAction();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                onTouchPlayTime = mBLVideoView.getCurrentPosition();
                x = event.getRawX();
                isChange = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getRawX();
                float disX = moveX - x;
                if (isChange && Math.abs(disX) > 2) {
                    x = moveX;
                    long addTimeOfPixel = MediaUtil.getTimeOfPixel(mBLVideoView.getDuration(),this); //拖拽像素与时长调节的比例
                    onTouchPlayTime += addTimeOfPixel * disX;
                    stopTiming();
                    if (onTouchPlayTime > mBLVideoView.getDuration()) {
                        onTouchPlayTime = mBLVideoView.getDuration();
                    } else if (onTouchPlayTime < 0) {
                        onTouchPlayTime = 0;
                    }
                    int movePercentage = (int) ((onTouchPlayTime * 1d / mBLVideoView.getDuration()) * mMediaController.getMaxProgress());
                    mMediaController.setProgress(movePercentage);
                }

                if (Math.abs(disX) > 30) { //防止误操作距离
                    isChange = true;
                    x = moveX;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (isChange) {
                    int percentage;
                    if (mBLVideoView.getDuration() == 0) {
                        percentage = 0;
                    } else {
                        percentage = (int) ((onTouchPlayTime * 1d / mBLVideoView.getDuration()) * mMediaController.getMaxProgress());
                    }

                    mMediaController.setProgress(percentage);
                    mBLVideoView.seekTo(onTouchPlayTime); //播放时间跳转
                    startTiming();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
