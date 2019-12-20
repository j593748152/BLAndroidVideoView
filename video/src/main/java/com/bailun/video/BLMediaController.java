package com.bailun.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bailun.video.utils.DisPlayUtils;

/**
 * Created by jiangwensong on 2019/11/21.
 * description：
 */
public class BLMediaController extends ConstraintLayout implements View.OnClickListener, IMediaController {
    private static final String TAG = "BLMediaController";
    private Context mContext;
    private static final int ICON_PLAY = R.drawable.uilib_icon_play;
    private static final int ICON_PAUSE = R.drawable.uilib_icon_pause_play;
    private static final int ICON_NEXT = R.drawable.module_study_icon_next_video;
    private static final int ICON_FULL_SCREEN = R.drawable.module_study_icon_full_screen;
    private static final int ICON_CANCEL_FULL_SCREEN = R.drawable.module_study_icon_full_cancel;
    private static final int SEEKBAR_THUMB = R.drawable.uilib_shape_seekbar_thumb_normal;
    private static final int SEEKBAR_BACKGROUND = R.drawable.uilib_background_seek_bar;


    private int mIconPlay = R.drawable.uilib_icon_play;
    private int mIconPause = R.drawable.uilib_icon_pause_play;
    private int mIconNext = R.drawable.module_study_icon_next_video;
    private int mIconFullScreen = R.drawable.module_study_icon_full_screen;
    private int mIconCancelFullScreen = R.drawable.module_study_icon_full_cancel;
    private int mSeekbarThmub = R.drawable.uilib_shape_seekbar_thumb_normal;
    private int mSeekbarBackground = R.drawable.uilib_background_seek_bar;
    private String mTextVideoSelect;

    private boolean playOrPause = false;
    private boolean fullOrCancel = true;

    private int mType;//样式
    protected TextView mTvNow;
    protected SeekBar mSeekBar;
    protected TextView mTvAll;
    protected ImageView mIvPlayOrPause;
    protected ImageView mIvPlayNext;
    protected TextView mTvSelect;
    protected ImageView mIvFullScreen;

    private IMediaControllerAction mMediaAction;

    public BLMediaController(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BLMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    public BLMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs){
        if (attrs != null){
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BLMediaController);
            mIconPlay = typedArray.getResourceId(R.styleable.BLMediaController_iconPlay, ICON_PLAY);
            mIconPause = typedArray.getResourceId(R.styleable.BLMediaController_iconPause, ICON_PAUSE);
            mIconNext = typedArray.getResourceId(R.styleable.BLMediaController_iconNext, ICON_NEXT);
            mIconFullScreen  = typedArray.getResourceId(R.styleable.BLMediaController_iconFullScreen, ICON_FULL_SCREEN);
            mIconCancelFullScreen  = typedArray.getResourceId(R.styleable.BLMediaController_iconCancelFullScreen, ICON_CANCEL_FULL_SCREEN);
            mSeekbarThmub = typedArray.getResourceId(R.styleable.BLMediaController_seekbarThumb, SEEKBAR_THUMB);
            mSeekbarBackground = typedArray.getResourceId(R.styleable.BLMediaController_seekbarBackground, SEEKBAR_BACKGROUND);
            mTextVideoSelect = typedArray.getString(R.styleable.BLMediaController_textVideoSelect);
        }
    }

    private void init() {
        initView();
        initListener();
        showType(mType);
    }

    /**
     * 初始化界面内容
     */
    protected void initView() {
        mIvPlayOrPause = new ImageView(mContext);
        mIvPlayNext = new ImageView(mContext);
        mTvNow = new TextView(mContext);
        mTvAll = new TextView(mContext);
        mSeekBar = new SeekBar(mContext);
        mTvSelect = new TextView(mContext);
        mIvFullScreen = new ImageView(mContext);

        mIvPlayOrPause.setId(View.generateViewId());
        mIvPlayNext.setId(View.generateViewId());
        mTvNow.setId(View.generateViewId());
        mTvAll.setId(View.generateViewId());
        mSeekBar.setId(View.generateViewId());
        mTvSelect.setId(View.generateViewId());
        mIvFullScreen.setId(View.generateViewId());

        mTvNow.setText("00:00");
        mTvNow.setTextColor(Color.WHITE);
        mTvAll.setText("00:00");
        mTvAll.setTextColor(Color.WHITE);
        mTvSelect.setText(mTextVideoSelect);
        mTvSelect.setTextColor(Color.WHITE);
        setPlayOrPause(playOrPause);
        setFullOrCancel(fullOrCancel);
        mIvPlayNext.setImageResource(mIconNext);
        mSeekBar.setThumb(getResources().getDrawable(mSeekbarThmub));
        mSeekBar.setProgressDrawable(getResources().getDrawable(mSeekbarBackground));
    }

    /**
     * 播放 暂停图标切换
     *
     * @param b 是否显示播放图标
     */
    public void setPlayOrPause(boolean b) {
        playOrPause = b;
        if (playOrPause) {
            mIvPlayOrPause.setImageResource(mIconPlay);
        } else {
            mIvPlayOrPause.setImageResource(mIconPause);
        }
    }

    /**
     * 全屏 非全屏图标切换
     *
     * @param b 是否显示全屏图标
     */
    public void setFullOrCancel(boolean b) {
        fullOrCancel = b;
        if (fullOrCancel) {
            mIvFullScreen.setImageResource(mIconFullScreen);
        } else {
            mIvFullScreen.setImageResource(mIconCancelFullScreen);
        }
    }

    /**
     * 初始化监听
     */
    protected void initListener() {
        mIvPlayOrPause.setOnClickListener(this);
        mIvPlayNext.setOnClickListener(this);
        mTvSelect.setOnClickListener(this);
        mIvFullScreen.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mMediaAction != null) {
                    mMediaAction.onProgressChanged(seekBar, progress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mMediaAction != null) {
                    mMediaAction.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaAction != null) {
                    mMediaAction.onStopTrackingTouch(seekBar);
                }
            }
        });
    }


    /**
     * 界面显示样式
     *
     * @param type
     */
    public void showType(int type) {
        mType = type;
        switch (mType) {
            case 0:
                showDefaultType();
                break;
            case 1:
                showType_land();
                break;
            case 2:
                showType_other();
                break;
            default:
                showDefaultType();
                break;
        }
    }

    /**
     * 界面默认布局
     */
    protected void showDefaultType() {
        removeAllViews();
        //播放按钮
        ConstraintLayout.LayoutParams ivPlayOrPauseParams = new ConstraintLayout.LayoutParams(DisPlayUtils.dip2px(mContext, 24), DisPlayUtils.dip2px(mContext, 24));
        ivPlayOrPauseParams.topToTop = 0;
        ivPlayOrPauseParams.bottomToBottom = 0;
        ivPlayOrPauseParams.startToStart = 0;
        ivPlayOrPauseParams.setMarginStart(DisPlayUtils.dip2px(mContext, 16));
        ivPlayOrPauseParams.bottomMargin = DisPlayUtils.dip2px(mContext, 10);
        mIvPlayOrPause.setLayoutParams(ivPlayOrPauseParams);
        addView(mIvPlayOrPause);
        //播放时间
        ConstraintLayout.LayoutParams tvNowParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvNowParams.topToTop = mIvPlayOrPause.getId();
        tvNowParams.bottomToBottom = mIvPlayOrPause.getId();
        tvNowParams.startToEnd = mIvPlayOrPause.getId();
        tvNowParams.setMarginStart(DisPlayUtils.dip2px(mContext, 16));
        mTvNow.setLayoutParams(tvNowParams);
        addView(mTvNow);
        //全屏图标
        ConstraintLayout.LayoutParams ivFullParams = new ConstraintLayout.LayoutParams(DisPlayUtils.dip2px(mContext, 24), DisPlayUtils.dip2px(mContext, 24));
        ivFullParams.topToTop = mIvPlayOrPause.getId();
        ivFullParams.bottomToBottom = mIvPlayOrPause.getId();
        ivFullParams.endToEnd = 0;
        ivFullParams.setMarginEnd(DisPlayUtils.dip2px(mContext, 16));
        mIvFullScreen.setLayoutParams(ivFullParams);
        addView(mIvFullScreen);
        //播放总时长
        ConstraintLayout.LayoutParams tvAllParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvAllParams.topToTop = mIvPlayOrPause.getId();
        tvAllParams.bottomToBottom = mIvPlayOrPause.getId();
        tvAllParams.endToStart = mIvFullScreen.getId();
        tvAllParams.setMarginEnd(DisPlayUtils.dip2px(mContext, 16));
        mTvAll.setLayoutParams(tvAllParams);
        addView(mTvAll);
        //进度条
        ConstraintLayout.LayoutParams seekBarParams = new ConstraintLayout.LayoutParams(LayoutParams.MATCH_CONSTRAINT, DisPlayUtils.dip2px(mContext, 24));
        seekBarParams.topToTop = mIvPlayOrPause.getId();
        seekBarParams.bottomToBottom = mIvPlayOrPause.getId();
        seekBarParams.startToEnd = mTvNow.getId();
        seekBarParams.endToStart = mTvAll.getId();
        mSeekBar.setLayoutParams(seekBarParams);
        addView(mSeekBar);
        //横屏图标
        fullOrCancel = true;
        setFullOrCancel(fullOrCancel);
    }

    /**
     * 横屏样式
     */
    protected void showType_land() {
        removeAllViews();
        //播放按钮
        ConstraintLayout.LayoutParams ivPlayOrPauseParams = new ConstraintLayout.LayoutParams(DisPlayUtils.dip2px(mContext, 24), DisPlayUtils.dip2px(mContext, 24));
        ivPlayOrPauseParams.bottomToBottom = 0;
        ivPlayOrPauseParams.startToStart = 0;
        ivPlayOrPauseParams.setMarginStart(DisPlayUtils.dip2px(mContext, 16));
        ivPlayOrPauseParams.bottomMargin = DisPlayUtils.dip2px(mContext, 18);
        mIvPlayOrPause.setLayoutParams(ivPlayOrPauseParams);
        addView(mIvPlayOrPause);
        //下一集
        ConstraintLayout.LayoutParams ivNextParams = new ConstraintLayout.LayoutParams(DisPlayUtils.dip2px(mContext, 24), DisPlayUtils.dip2px(mContext, 24));
        ivNextParams.topToTop = mIvPlayOrPause.getId();
        ivNextParams.bottomToBottom = mIvPlayOrPause.getId();
        ivNextParams.startToEnd = mIvPlayOrPause.getId();
        ivNextParams.setMarginStart(DisPlayUtils.dip2px(mContext, 16));
        mIvPlayNext.setLayoutParams(ivNextParams);
        addView(mIvPlayNext);
        //全屏图标
        ConstraintLayout.LayoutParams ivFullParams = new ConstraintLayout.LayoutParams(DisPlayUtils.dip2px(mContext, 24), DisPlayUtils.dip2px(mContext, 24));
        ivFullParams.topToTop = mIvPlayOrPause.getId();
        ivFullParams.bottomToBottom = mIvPlayOrPause.getId();
        ivFullParams.endToEnd = 0;
        ivFullParams.setMarginEnd(DisPlayUtils.dip2px(mContext, 16));
        mIvFullScreen.setLayoutParams(ivFullParams);
        addView(mIvFullScreen);
        //选集
        ConstraintLayout.LayoutParams tvSelectParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvSelectParams.topToTop = mIvPlayOrPause.getId();
        tvSelectParams.bottomToBottom = mIvPlayOrPause.getId();
        tvSelectParams.endToStart = mIvFullScreen.getId();
        tvSelectParams.goneEndMargin = DisPlayUtils.dip2px(mContext, 16);
        tvSelectParams.setMarginEnd(DisPlayUtils.dip2px(mContext, 16));
        mTvSelect.setLayoutParams(tvSelectParams);
        addView(mTvSelect);
        //播放时间
        ConstraintLayout.LayoutParams tvNowParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvNowParams.topToTop = 0;
        tvNowParams.startToStart = 0;
        tvNowParams.bottomToTop = mIvPlayOrPause.getId();
        tvNowParams.bottomMargin = DisPlayUtils.dip2px(mContext, 24);
        tvNowParams.setMarginStart(DisPlayUtils.dip2px(mContext, 16));
        mTvNow.setLayoutParams(tvNowParams);
        addView(mTvNow);
        //播放总时长
        ConstraintLayout.LayoutParams tvAllParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvAllParams.topToTop = mTvNow.getId();
        tvAllParams.bottomToBottom = mTvNow.getId();
        tvAllParams.endToEnd = 0;
        tvAllParams.setMarginEnd(DisPlayUtils.dip2px(mContext, 16));
        mTvAll.setLayoutParams(tvAllParams);
        addView(mTvAll);
        //进度条
        ConstraintLayout.LayoutParams seekBarParams = new ConstraintLayout.LayoutParams(LayoutParams.MATCH_CONSTRAINT, DisPlayUtils.dip2px(mContext, 24));
        seekBarParams.topToTop = mTvNow.getId();
        seekBarParams.bottomToBottom = mTvNow.getId();
        seekBarParams.startToEnd = mTvNow.getId();
        seekBarParams.endToStart = mTvAll.getId();
        mSeekBar.setLayoutParams(seekBarParams);
        addView(mSeekBar);
        //横屏图标
        fullOrCancel = false;
        setFullOrCancel(fullOrCancel);
    }

    /**
     * 其他样式
     */
    protected void showType_other() {

    }

    public void setMediaAction(IMediaControllerAction mediaAction) {
        mMediaAction = mediaAction;
    }

    public void setIvNextVisibility(int visibility) {
        mIvPlayNext.setVisibility(visibility);
    }

    public void setNowTimeText(String nowTime) {
        mTvNow.setText(nowTime);
    }

    public void setAllTimeText(String allTime) {
        mTvAll.setText(allTime);
    }

    public void setMaxProcess(int max) {
        mSeekBar.setMax(max);
    }

    @Override
    public int getMaxProgress() {
        return mSeekBar.getMax();
    }

    @Override
    public void setProgress(int precent) {
        mSeekBar.setProgress(precent);
    }

    public void setSecondaryProgress(int precent) {
        mSeekBar.setSecondaryProgress(precent);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == mIvPlayOrPause.getId()) {
            if (playOrPause) {
                if (mMediaAction != null) {
                    mMediaAction.playVideo();
                }
            } else {
                if (mMediaAction != null) {
                    mMediaAction.pauseVideo();
                }
            }
        } else if (viewId == mIvPlayNext.getId()) {
            if (mMediaAction != null) {
                mMediaAction.nextVideo();
            }
        } else if (viewId == mTvSelect.getId()) {
            if (mMediaAction != null) {
                mMediaAction.videoSelect();
            }
        } else if (viewId == mIvFullScreen.getId()) {
            if (fullOrCancel) {
                if (mMediaAction != null) {
                    mMediaAction.fullScreen();
                }
            } else {
                if (mMediaAction != null) {
                    mMediaAction.cancelScreen();
                }
            }
        }
    }

    @Override
    public void mediaPaused() {
        setPlayOrPause(true);
        show();
    }

    @Override
    public void mediaPlaying() {
        setPlayOrPause(false);
        hide();
    }

    @Override
    public void show() {
        removeHideMsg();
        setVisibility(VISIBLE);
        if (!playOrPause) {
            //播放状态，延时消失
            hide();
        }
    }

    @Override
    public void hide() {
        sendHideMsg(5000);
    }

    private void sendHideMsg(long delay) {
        removeHideMsg();
        Message msg = new Message();
        msg.what = Msg.hide;
        mHandler.sendMessageDelayed(msg, delay);
    }

    private void removeHideMsg() {
        if (mHandler.hasMessages(Msg.hide)) {
            mHandler.removeMessages(Msg.hide);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Msg.hide) {
                setVisibility(GONE);
            } else if (msg.what == Msg.show) {
                setVisibility(VISIBLE);
            }
        }
    };

    private @interface Msg {
        int hide = 0;
        int show = 1;
    }
}


