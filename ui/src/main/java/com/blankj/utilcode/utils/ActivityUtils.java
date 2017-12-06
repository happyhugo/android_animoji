//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import java.util.Iterator;
import java.util.List;

public class ActivityUtils {
    private ActivityUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isActivityExists(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return context.getPackageManager().resolveActivity(intent, 0) != null && intent.resolveActivity(context.getPackageManager()) != null && context.getPackageManager().queryIntentActivities(intent, 0).size() != 0;
    }

    public static void launchActivity(Context context, String packageName, String className) {
        launchActivity(context, packageName, className, (Bundle)null);
    }

    public static void launchActivity(Context context, String packageName, String className, Bundle bundle) {
        context.startActivity(IntentUtils.getComponentIntent(packageName, className, bundle));
    }

    public static String getLauncherActivity(Context context, String packageName) {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri)null);
        intent.addCategory("android.intent.category.LAUNCHER");
        PackageManager pm = context.getPackageManager();
        List infos = pm.queryIntentActivities(intent, 0);
        Iterator var5 = infos.iterator();

        ResolveInfo info;
        do {
            if(!var5.hasNext()) {
                return "no " + packageName;
            }

            info = (ResolveInfo)var5.next();
        } while(!info.activityInfo.packageName.equals(packageName));

        return info.activityInfo.name;
    }
}
