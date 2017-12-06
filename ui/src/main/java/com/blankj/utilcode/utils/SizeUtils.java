//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import com.blankj.utilcode.utils.Utils;

public class SizeUtils {
    private static SizeUtils.onGetSizeListener mListener;

    private SizeUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static int dp2px(float dpValue) {
        float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int px2dp(float pxValue) {
        float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }

    public static int sp2px(float spValue) {
        float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }

    public static int px2sp(float pxValue) {
        float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / fontScale + 0.5F);
    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch(unit) {
            case 0:
                return value;
            case 1:
                return value * metrics.density;
            case 2:
                return value * metrics.scaledDensity;
            case 3:
                return value * metrics.xdpi * 0.013888889F;
            case 4:
                return value * metrics.xdpi;
            case 5:
                return value * metrics.xdpi * 0.03937008F;
            default:
                return 0.0F;
        }
    }

    public static void forceGetViewSize(final View view, final SizeUtils.onGetSizeListener listener) {
        view.post(new Runnable() {
            public void run() {
                if(listener != null) {
                    listener.onGetSize(view);
                }

            }
        });
    }

    public static void setListener(SizeUtils.onGetSizeListener listener) {
        mListener = listener;
    }

    public static int[] measureView(View view) {
        LayoutParams lp = view.getLayoutParams();
        if(lp == null) {
            lp = new LayoutParams(-1, -2);
        }

        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int lpHeight = lp.height;
        int heightSpec;
        if(lpHeight > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            heightSpec = MeasureSpec.makeMeasureSpec(0, 0);
        }

        view.measure(widthSpec, heightSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    public static int getMeasuredWidth(View view) {
        return measureView(view)[0];
    }

    public static int getMeasuredHeight(View view) {
        return measureView(view)[1];
    }

    public interface onGetSizeListener {
        void onGetSize(View var1);
    }
}
