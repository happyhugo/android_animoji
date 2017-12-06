//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.Utils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ProcessUtils {
    private ProcessUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static String getForegroundProcessName() {
        ActivityManager manager = (ActivityManager)Utils.getContext().getSystemService("activity");
        List infos = manager.getRunningAppProcesses();
        if(infos != null && infos.size() != 0) {
            Iterator packageManager = infos.iterator();

            while(packageManager.hasNext()) {
                RunningAppProcessInfo intent = (RunningAppProcessInfo)packageManager.next();
                if(intent.importance == 100) {
                    return intent.processName;
                }
            }
        }

        if(VERSION.SDK_INT > 21) {
            PackageManager packageManager1 = Utils.getContext().getPackageManager();
            Intent intent1 = new Intent("android.settings.USAGE_ACCESS_SETTINGS");
            List list = packageManager1.queryIntentActivities(intent1, 65536);
            System.out.println(list);
            if(list.size() > 0) {
                try {
                    ApplicationInfo e = packageManager1.getApplicationInfo(Utils.getContext().getPackageName(), 0);
                    AppOpsManager aom = (AppOpsManager)Utils.getContext().getSystemService("appops");
                    if(aom.checkOpNoThrow("android:get_usage_stats", e.uid, e.packageName) != 0) {
                        Utils.getContext().startActivity(intent1);
                    }

                    if(aom.checkOpNoThrow("android:get_usage_stats", e.uid, e.packageName) != 0) {
                        LogUtils.d("getForegroundApp", "没有打开\"有权查看使用权限的应用\"选项");
                        return null;
                    }

                    UsageStatsManager usageStatsManager = (UsageStatsManager)Utils.getContext().getSystemService("usagestats");
                    long endTime = System.currentTimeMillis();
                    long beginTime = endTime - 604800000L;
                    List usageStatses = usageStatsManager.queryUsageStats(4, beginTime, endTime);
                    if(usageStatses != null && !usageStatses.isEmpty()) {
                        UsageStats recentStats = null;
                        Iterator var14 = usageStatses.iterator();

                        while(true) {
                            UsageStats usageStats;
                            do {
                                if(!var14.hasNext()) {
                                    return recentStats == null?null:recentStats.getPackageName();
                                }

                                usageStats = (UsageStats)var14.next();
                            } while(recentStats != null && usageStats.getLastTimeUsed() <= recentStats.getLastTimeUsed());

                            recentStats = usageStats;
                        }
                    }

                    return null;
                } catch (NameNotFoundException var16) {
                    var16.printStackTrace();
                }
            } else {
                LogUtils.d("getForegroundApp", "无\"有权查看使用权限的应用\"选项");
            }
        }

        return null;
    }

    public static Set<String> getAllBackgroundProcesses() {
        ActivityManager am = (ActivityManager)Utils.getContext().getSystemService("activity");
        List infos = am.getRunningAppProcesses();
        HashSet set = new HashSet();
        Iterator var3 = infos.iterator();

        while(var3.hasNext()) {
            RunningAppProcessInfo info = (RunningAppProcessInfo)var3.next();
            Collections.addAll(set, info.pkgList);
        }

        return set;
    }

    public static Set<String> killAllBackgroundProcesses() {
        ActivityManager am = (ActivityManager)Utils.getContext().getSystemService("activity");
        List infos = am.getRunningAppProcesses();
        HashSet set = new HashSet();
        Iterator var3 = infos.iterator();

        RunningAppProcessInfo info;
        String[] var5;
        int var6;
        int var7;
        String pkg;
        while(var3.hasNext()) {
            info = (RunningAppProcessInfo)var3.next();
            var5 = info.pkgList;
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
                pkg = var5[var7];
                am.killBackgroundProcesses(pkg);
                set.add(pkg);
            }
        }

        infos = am.getRunningAppProcesses();
        var3 = infos.iterator();

        while(var3.hasNext()) {
            info = (RunningAppProcessInfo)var3.next();
            var5 = info.pkgList;
            var6 = var5.length;

            for(var7 = 0; var7 < var6; ++var7) {
                pkg = var5[var7];
                set.remove(pkg);
            }
        }

        return set;
    }

    public static boolean killBackgroundProcesses(String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return false;
        } else {
            ActivityManager am = (ActivityManager)Utils.getContext().getSystemService("activity");
            List infos = am.getRunningAppProcesses();
            if(infos != null && infos.size() != 0) {
                Iterator var3 = infos.iterator();

                RunningAppProcessInfo info;
                while(var3.hasNext()) {
                    info = (RunningAppProcessInfo)var3.next();
                    if(Arrays.asList(info.pkgList).contains(packageName)) {
                        am.killBackgroundProcesses(packageName);
                    }
                }

                infos = am.getRunningAppProcesses();
                if(infos != null && infos.size() != 0) {
                    var3 = infos.iterator();

                    do {
                        if(!var3.hasNext()) {
                            return true;
                        }

                        info = (RunningAppProcessInfo)var3.next();
                    } while(!Arrays.asList(info.pkgList).contains(packageName));

                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }
}
