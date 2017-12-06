//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.lang.reflect.Method;

public class BarUtils {
    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    private BarUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static void setColor(Activity activity, int color) {
        setColor(activity, color, 112);
    }

    public static void setColor(Activity activity, int color, int statusBarAlpha) {
        if(VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(-2147483648);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if(VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if(count > 0 && decorView.getChildAt(count - 1) instanceof BarUtils.StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                BarUtils.StatusBarView statusView = createStatusBarView(activity, color, statusBarAlpha);
                decorView.addView(statusView);
            }

            setRootView(activity);
        }

    }

    public static void setColorForSwipeBack(Activity activity, int color) {
        setColorForSwipeBack(activity, color, 112);
    }

    public static void setColorForSwipeBack(Activity activity, int color, int statusBarAlpha) {
        if(VERSION.SDK_INT >= 19) {
            ViewGroup contentView = (ViewGroup)activity.findViewById(16908290);
            contentView.setPadding(0, getStatusBarHeight(activity), 0, 0);
            contentView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            setTransparentForWindow(activity);
        }

    }

    public static void setColorNoTranslucent(Activity activity, int color) {
        setColor(activity, color, 0);
    }

    /** @deprecated */
    @Deprecated
    public static void setColorDiff(Activity activity, int color) {
        if(VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if(count > 0 && decorView.getChildAt(count - 1) instanceof BarUtils.StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(color);
            } else {
                BarUtils.StatusBarView statusView = createStatusBarView(activity, color);
                decorView.addView(statusView);
            }

            setRootView(activity);
        }
    }

    public static void setTranslucent(Activity activity) {
        setTranslucent(activity, 112);
    }

    public static void setTranslucent(Activity activity, int statusBarAlpha) {
        if(VERSION.SDK_INT >= 19) {
            setTransparent(activity);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTranslucentForCoordinatorLayout(Activity activity, int statusBarAlpha) {
        if(VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparent(Activity activity) {
        if(VERSION.SDK_INT >= 19) {
            transparentStatusBar(activity);
            setRootView(activity);
        }
    }

    /** @deprecated */
    @Deprecated
    public static void setTranslucentDiff(Activity activity) {
        if(VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            setRootView(activity);
        }

    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 112);
    }

    public static void setColorNoTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color) {
        setColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    public static void setColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int color, int statusBarAlpha) {
        if(VERSION.SDK_INT >= 19) {
            if(VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(-2147483648);
                activity.getWindow().clearFlags(67108864);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(67108864);
            }

            ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
            if(contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof BarUtils.StatusBarView) {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                BarUtils.StatusBarView drawer = createStatusBarView(activity, color);
                contentLayout.addView(drawer, 0);
            }

            if(!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(), contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
            }

            ViewGroup drawer1 = (ViewGroup)drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer1.setFitsSystemWindows(false);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    /** @deprecated */
    @Deprecated
    public static void setColorForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout, int color) {
        if(VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
            if(contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof BarUtils.StatusBarView) {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, 112));
            } else {
                BarUtils.StatusBarView drawer = createStatusBarView(activity, color);
                contentLayout.addView(drawer, 0);
            }

            if(!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }

            ViewGroup drawer1 = (ViewGroup)drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer1.setFitsSystemWindows(false);
        }

    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(activity, drawerLayout, 112);
    }

    public static void setTranslucentForDrawerLayout(Activity activity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if(VERSION.SDK_INT >= 19) {
            setTransparentForDrawerLayout(activity, drawerLayout);
            addTranslucentView(activity, statusBarAlpha);
        }
    }

    public static void setTransparentForDrawerLayout(Activity activity, DrawerLayout drawerLayout) {
        if(VERSION.SDK_INT >= 19) {
            if(VERSION.SDK_INT >= 21) {
                activity.getWindow().addFlags(-2147483648);
                activity.getWindow().clearFlags(67108864);
                activity.getWindow().setStatusBarColor(0);
            } else {
                activity.getWindow().addFlags(67108864);
            }

            ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
            if(!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(activity), 0, 0);
            }

            ViewGroup drawer = (ViewGroup)drawerLayout.getChildAt(1);
            drawerLayout.setFitsSystemWindows(false);
            contentLayout.setFitsSystemWindows(false);
            contentLayout.setClipToPadding(true);
            drawer.setFitsSystemWindows(false);
        }
    }

    /** @deprecated */
    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(Activity activity, DrawerLayout drawerLayout) {
        if(VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            ViewGroup contentLayout = (ViewGroup)drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            ViewGroup vg = (ViewGroup)drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            drawerLayout.setFitsSystemWindows(false);
        }

    }

    public static void setTransparentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, View needOffsetView) {
        setTranslucentForImageView(activity, 112, needOffsetView);
    }

    public static void setTranslucentForImageView(Activity activity, int statusBarAlpha, View needOffsetView) {
        if(VERSION.SDK_INT >= 19) {
            setTransparentForWindow(activity);
            addTranslucentView(activity, statusBarAlpha);
            if(needOffsetView != null) {
                MarginLayoutParams layoutParams = (MarginLayoutParams)needOffsetView.getLayoutParams();
                layoutParams.setMargins(0, getStatusBarHeight(activity), 0, 0);
            }

        }
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 112, needOffsetView);
    }

    public static void setTransparentForImageViewInFragment(Activity activity, View needOffsetView) {
        setTranslucentForImageViewInFragment(activity, 0, needOffsetView);
    }

    public static void setTranslucentForImageViewInFragment(Activity activity, int statusBarAlpha, View needOffsetView) {
        setTranslucentForImageView(activity, statusBarAlpha, needOffsetView);
        if(VERSION.SDK_INT >= 19 && VERSION.SDK_INT < 21) {
            clearPreviousSetting(activity);
        }

    }

    @TargetApi(19)
    private static void clearPreviousSetting(Activity activity) {
        ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if(count > 0 && decorView.getChildAt(count - 1) instanceof BarUtils.StatusBarView) {
            decorView.removeViewAt(count - 1);
            ViewGroup rootView = (ViewGroup)((ViewGroup)activity.findViewById(16908290)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }

    }

    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup)activity.findViewById(16908290);
        if(contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }

    }

    private static BarUtils.StatusBarView createStatusBarView(Activity activity, int color) {
        BarUtils.StatusBarView statusBarView = new BarUtils.StatusBarView(activity);
        LayoutParams params = new LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    private static BarUtils.StatusBarView createStatusBarView(Activity activity, int color, int alpha) {
        BarUtils.StatusBarView statusBarView = new BarUtils.StatusBarView(activity);
        LayoutParams params = new LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    @TargetApi(14)
    private static void setRootView(Activity activity) {
        ViewGroup rootView = (ViewGroup)((ViewGroup)activity.findViewById(16908290)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

    private static void setTransparentForWindow(Activity activity) {
        if(VERSION.SDK_INT >= 21) {
            activity.getWindow().setStatusBarColor(0);
            activity.getWindow().getDecorView().setSystemUiVisibility(1280);
        } else if(VERSION.SDK_INT >= 19) {
            activity.getWindow().setFlags(67108864, 67108864);
        }

    }

    @TargetApi(19)
    private static void transparentStatusBar(Activity activity) {
        if(VERSION.SDK_INT >= 21) {
            activity.getWindow().addFlags(-2147483648);
            activity.getWindow().clearFlags(67108864);
            activity.getWindow().addFlags(134217728);
            activity.getWindow().setStatusBarColor(0);
        } else {
            activity.getWindow().addFlags(67108864);
        }

    }

    private static BarUtils.StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        BarUtils.StatusBarView statusBarView = new BarUtils.StatusBarView(activity);
        LayoutParams params = new LayoutParams(-1, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    public static int getStatusBarHeight(Context context) {
        int result = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private static int calculateStatusColor(int color, int alpha) {
        float a = 1.0F - (float)alpha / 255.0F;
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        red = (int)((double)((float)red * a) + 0.5D);
        green = (int)((double)((float)green * a) + 0.5D);
        blue = (int)((double)((float)blue * a) + 0.5D);
        return -16777216 | red << 16 | green << 8 | blue;
    }

    public static void setTransparentStatusBar(Activity activity) {
        if(VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(67108864);
            activity.getWindow().addFlags(134217728);
        }

    }

    public static void hideStatusBar(Activity activity) {
        activity.requestWindowFeature(1);
        activity.getWindow().setFlags(1024, 1024);
    }

    public static boolean isStatusBarExists(Activity activity) {
        android.view.WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        return (params.flags & 1024) != 1024;
    }

    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        return activity.getTheme().resolveAttribute(16843499, tv, true)?TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics()):0;
    }

    public static void showNotificationBar(Context context, boolean isSettingPanel) {
        String methodName = VERSION.SDK_INT <= 16?"expand":(isSettingPanel?"expandSettingsPanel":"expandNotificationsPanel");
        invokePanels(context, methodName);
    }

    public static void hideNotificationBar(Context context) {
        String methodName = VERSION.SDK_INT <= 16?"collapse":"collapsePanels";
        invokePanels(context, methodName);
    }

    private static void invokePanels(Context context, String methodName) {
        try {
            Object e = context.getSystemService("statusbar");
            Class statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod(methodName, new Class[0]);
            expand.invoke(e, new Object[0]);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static class StatusBarView extends View {
        public StatusBarView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public StatusBarView(Context context) {
            super(context);
        }
    }
}
