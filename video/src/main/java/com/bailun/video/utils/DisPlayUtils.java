package com.bailun.video.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


public class DisPlayUtils {

    /**
     * dp转px
     *
     * @param context
     * @param dipFloat
     * @return
     */
    public static int dip2px(Context context, float dipFloat) {
        float f = context.getResources().getDisplayMetrics().density;
        return (int) (dipFloat * f + 0.5F);
    }

    /**
     * Get Screen Real Width
     *
     * @param context Context
     * @return Real Width
     */
    public static int getRealWidth(Context context) {
        Display display = getDisplay(context);
        if (display == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        return dm.widthPixels;
    }


    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    private static Display getDisplay(Context context) {
        WindowManager wm;
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            wm = activity.getWindowManager();
        } else {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wm != null) {
            return wm.getDefaultDisplay();
        }
        return null;
    }
}