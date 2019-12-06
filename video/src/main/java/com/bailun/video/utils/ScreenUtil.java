package com.bailun.video.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Created by chengl on 2018/5/24.
 *
 * 获取屏幕尺寸与尺寸之间进行转换
 *
 */

public class ScreenUtil {

    private static int screenW = -1;
    private static int screenH = -1;
    private static float screenDensity = -1;
    private static float screenScaledDensity = -1;
    //状态栏的高度
    private static int statusBarHeight = -1;
    //虚拟按键的高度
    private static int virtualBarHeigth = -1;
    //导航栏的高度
    private static int navigationBarHeight = -1;

    /**
     * 在activity或Application中进行初始化(必须在适配之前初始化)
     * @param context
     */
    public static void initScreen(Context context){
        checkContext(context);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenW = displayMetrics.widthPixels;
        screenH = displayMetrics.heightPixels;
        statusBarHeight = initStatusBarHeight(context);
        virtualBarHeigth = initVirtualBarHeigh(context);
        navigationBarHeight = initNavigationBarHeight(context);
        initUnitSwitch(context);
    }

    /**
     * 初始化dp,sp,px的单位切换 : 使用更改dp与px系数的适配方案时必须重新初始化
     * @param context
     */
    public static void initUnitSwitch(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenDensity = displayMetrics.density;
        screenScaledDensity = displayMetrics.scaledDensity;
    }

    //获取导航栏的高度
    private static int initNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    //获取状态栏的高度
    private static int initStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    //获取虚拟按键的高度
    private static int initVirtualBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * @return ：获取屏幕的宽
     */
    public static int getWidth(){
        checkSize(screenW);
        return screenW;
    }

    /**
     * @return ：获取屏幕的高（包含状态栏的高度 + 虚拟按键的高度显示或隐藏虚拟按键后获取的值不同）
     */
    public static int getHeight(){
        checkSize(screenH);
        return screenH;
    }

    /**
     * @return : 状态栏的高度
     */
    public static int getStatusBarHeight() {
        checkSize(statusBarHeight);
        return statusBarHeight;
    }

    /**
     * @return ：虚拟按键的高度
     */
    public static int getVirtualBarHeigh() {
        checkSize(virtualBarHeigth);
        return virtualBarHeigth;
    }

    /**
     * @return : 导航栏的高度
     */
    public static int getNavigationBarHeight() {
        checkSize(navigationBarHeight);
        return navigationBarHeight;
    }

    /**
     * dp转换为px
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        checkSize(screenDensity);
        return (int) (dpValue*screenDensity+0.5);
    }

    /**
     * px转换为dp
     * @param pxValue
     * @return
     */
    public static float px2dp(int pxValue){
        checkSize(screenDensity);
        return pxValue * 1.0f / screenDensity;
    }

    /**
     * sp转换为px
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue){
        checkSize(screenScaledDensity);
        return (int) (spValue*screenScaledDensity+0.5);
    }

    /**
     * px转换为sp
     * @param pxValue
     * @return
     */
    public static float px2sp(int pxValue){
        checkSize(screenScaledDensity);
        return pxValue * 1.0f / screenScaledDensity;
    }

    /**
     * 对初始化时的context参数进行校验
     * @param context
     */
    private static void checkContext(Context context) {
        if (!(context instanceof Activity || context instanceof Application)) {
            throw new IllegalArgumentException("传入的context类型不对");
        }
    }

    /**
     * 通过校验参数是否为0才判断是否已经进行了初始化
     * @param size
     */
    private static void checkSize(float size) {
        if (size < 0) {
            throw new IllegalArgumentException("请先调用initScreen方法进行初始化");
        }
    }

}
