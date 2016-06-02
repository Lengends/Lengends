package com.org.lengend.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by wangyanfei on 2016/6/2.
 */
public class SystemPhoneUtil {


    /**
     * 获取屏幕的宽高
     * @param activity
     * @return
     */
    public static Rect getScreenWidthAndHight(Activity activity){
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return new Rect(0,0,dm.widthPixels, dm.heightPixels);
    }

/**
     * 获取屏幕的宽高
     * @param activity
     * @return
     */
    public static float getPhoneDensity(Activity activity){
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }






}
