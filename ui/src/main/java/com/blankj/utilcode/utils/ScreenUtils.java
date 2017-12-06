//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.graphics.Bitmap;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import com.blankj.utilcode.utils.BarUtils;
import com.blankj.utilcode.utils.Utils;

public class ScreenUtils {
    private ScreenUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager)Utils.getContext().getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager)Utils.getContext().getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(0);
    }

    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(1);
    }

    public static boolean isLandscape() {
        return Utils.getContext().getResources().getConfiguration().orientation == 2;
    }

    public static boolean isPortrait() {
        return Utils.getContext().getResources().getConfiguration().orientation == 1;
    }

    public static int getScreenRotation(Activity activity) {
        switch(activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 0:
            default:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            case 3:
                return 270;
        }
    }

    public static Bitmap captureWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        view.destroyDrawingCache();
        return ret;
    }

    public static Bitmap captureWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = BarUtils.getStatusBarHeight(activity);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        view.destroyDrawingCache();
        return ret;
    }

    public static boolean isScreenLock() {
        KeyguardManager km = (KeyguardManager)Utils.getContext().getSystemService("keyguard");
        return km.inKeyguardRestrictedInputMode();
    }

    public static void setSleepDuration(int duration) {
        System.putInt(Utils.getContext().getContentResolver(), "screen_off_timeout", duration);
    }

    public static int getSleepDuration() {
        try {
            return System.getInt(Utils.getContext().getContentResolver(), "screen_off_timeout");
        } catch (SettingNotFoundException var1) {
            var1.printStackTrace();
            return -123;
        }
    }
}
