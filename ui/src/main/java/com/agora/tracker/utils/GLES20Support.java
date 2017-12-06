//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ConfigurationInfo;
import android.util.Log;

public class GLES20Support {
    private static final String TAG = "GLES20Support";

    public GLES20Support() {
    }

    public static boolean detectOpenGLES20(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        Log.i("GLES20Support", "Device support OPENGL2.0;  info.reqGlEsVersion" + info.reqGlEsVersion);
        return info.reqGlEsVersion >= 131072;
    }

    public static boolean detectOpenGLES30(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService("activity");
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        Log.i("GLES20Support", "Device support OPENGL3.0;  info.reqGlEsVersion" + info.reqGlEsVersion);
        return info.reqGlEsVersion >= 196608;
    }

    public static Dialog getNoSupportGLES20Dialog(final Activity activity) {
        Builder b = new Builder(activity);
        b.setCancelable(false);
        b.setTitle("error");
        b.setMessage("No support OPENGL");
        b.setNegativeButton("Exit", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        Log.i("GLES20Support", "No support OPENGL2.0");
        return b.create();
    }
}
