//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ServiceUtils {
    private ServiceUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static Set getAllRunningService(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        List infos = activityManager.getRunningServices(2147483647);
        HashSet names = new HashSet();
        if(infos != null && infos.size() != 0) {
            Iterator var4 = infos.iterator();

            while(var4.hasNext()) {
                RunningServiceInfo info = (RunningServiceInfo)var4.next();
                names.add(info.service.getClassName());
            }

            return names;
        } else {
            return null;
        }
    }

    public static void startService(Context context, String className) {
        try {
            startService(context, Class.forName(className));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void startService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startService(intent);
    }

    public static boolean stopService(Context context, String className) {
        try {
            return stopService(context, Class.forName(className));
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean stopService(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        return context.stopService(intent);
    }

    public static void bindService(Context context, String className, ServiceConnection conn, int flags) {
        try {
            bindService(context, Class.forName(className), conn, flags);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static void bindService(Context context, Class<?> cls, ServiceConnection conn, int flags) {
        Intent intent = new Intent(context, cls);
        context.bindService(intent, conn, flags);
    }

    public static void unbindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        List infos = activityManager.getRunningServices(2147483647);
        if(infos != null && infos.size() != 0) {
            Iterator var4 = infos.iterator();

            RunningServiceInfo info;
            do {
                if(!var4.hasNext()) {
                    return false;
                }

                info = (RunningServiceInfo)var4.next();
            } while(!className.equals(info.service.getClassName()));

            return true;
        } else {
            return false;
        }
    }
}
