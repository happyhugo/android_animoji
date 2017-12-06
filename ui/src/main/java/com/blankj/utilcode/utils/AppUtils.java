//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import com.blankj.utilcode.utils.CleanUtils;
import com.blankj.utilcode.utils.EncryptUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.IntentUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ProcessUtils;
import com.blankj.utilcode.utils.ShellUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.Utils;
import com.blankj.utilcode.utils.ShellUtils.CommandResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isInstallApp(Context context, String packageName) {
        return !StringUtils.isSpace(packageName) && IntentUtils.getLaunchAppIntent(context, packageName) != null;
    }

    public static void installApp(Context context, String filePath) {
        installApp(context, FileUtils.getFileByPath(filePath));
    }

    public static void installApp(Context context, File file) {
        if(FileUtils.isFileExists(file)) {
            context.startActivity(IntentUtils.getInstallAppIntent(file));
        }
    }

    public static void installApp(Activity activity, String filePath, int requestCode) {
        installApp(activity, FileUtils.getFileByPath(filePath), requestCode);
    }

    public static void installApp(Activity activity, File file, int requestCode) {
        if(FileUtils.isFileExists(file)) {
            activity.startActivityForResult(IntentUtils.getInstallAppIntent(file), requestCode);
        }
    }

    public static boolean installAppSilent(String filePath) {
        File file = FileUtils.getFileByPath(filePath);
        if(!FileUtils.isFileExists(file)) {
            return false;
        } else {
            String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
            CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(Utils.getContext()), true);
            return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
        }
    }

    public static void uninstallApp(Context context, String packageName) {
        if(!StringUtils.isSpace(packageName)) {
            context.startActivity(IntentUtils.getUninstallAppIntent(packageName));
        }
    }

    public static void uninstallApp(Activity activity, String packageName, int requestCode) {
        if(!StringUtils.isSpace(packageName)) {
            activity.startActivityForResult(IntentUtils.getUninstallAppIntent(packageName), requestCode);
        }
    }

    public static boolean uninstallAppSilent(Context context, String packageName, boolean isKeepData) {
        if(StringUtils.isSpace(packageName)) {
            return false;
        } else {
            String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (isKeepData?"-k ":"") + packageName;
            CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(context), true);
            return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
        }
    }

    public static boolean isAppRoot() {
        CommandResult result = ShellUtils.execCmd("echo root", true);
        if(result.result == 0) {
            return true;
        } else {
            if(result.errorMsg != null) {
                LogUtils.d("isAppRoot", result.errorMsg);
            }

            return false;
        }
    }

    public static void launchApp(Context context, String packageName) {
        if(!StringUtils.isSpace(packageName)) {
            context.startActivity(IntentUtils.getLaunchAppIntent(context, packageName));
        }
    }

    public static void launchApp(Activity activity, String packageName, int requestCode) {
        if(!StringUtils.isSpace(packageName)) {
            activity.startActivityForResult(IntentUtils.getLaunchAppIntent(activity, packageName), requestCode);
        }
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }

    public static void getAppDetailsSettings(Context context) {
        getAppDetailsSettings(context, context.getPackageName());
    }

    public static void getAppDetailsSettings(Context context, String packageName) {
        if(!StringUtils.isSpace(packageName)) {
            context.startActivity(IntentUtils.getAppDetailsSettingsIntent(packageName));
        }
    }

    public static String getAppName(Context context) {
        return getAppName(context, context.getPackageName());
    }

    public static String getAppName(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return null;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                PackageInfo pi = e.getPackageInfo(packageName, 0);
                return pi == null?null:pi.applicationInfo.loadLabel(e).toString();
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    public static Drawable getAppIcon(Context context) {
        return getAppIcon(context, context.getPackageName());
    }

    public static Drawable getAppIcon(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return null;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                PackageInfo pi = e.getPackageInfo(packageName, 0);
                return pi == null?null:pi.applicationInfo.loadIcon(e);
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    public static String getAppPath(Context context) {
        return getAppPath(context, context.getPackageName());
    }

    public static String getAppPath(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return null;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                PackageInfo pi = e.getPackageInfo(packageName, 0);
                return pi == null?null:pi.applicationInfo.sourceDir;
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    public static String getAppVersionName(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return null;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                PackageInfo pi = e.getPackageInfo(packageName, 0);
                return pi == null?null:pi.versionName;
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    public static int getAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    public static int getAppVersionCode(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return -1;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                PackageInfo pi = e.getPackageInfo(packageName, 0);
                return pi == null?-1:pi.versionCode;
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return -1;
            }
        }
    }

    public static boolean isSystemApp(Context context) {
        return isSystemApp(context, context.getPackageName());
    }

    public static boolean isSystemApp(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return false;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                ApplicationInfo ai = e.getApplicationInfo(packageName, 0);
                return ai != null && (ai.flags & 1) != 0;
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return false;
            }
        }
    }

    public static boolean isAppDebug(Context context) {
        return isAppDebug(context, context.getPackageName());
    }

    public static boolean isAppDebug(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return false;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                ApplicationInfo ai = e.getApplicationInfo(packageName, 0);
                return ai != null && (ai.flags & 2) != 0;
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return false;
            }
        }
    }

    public static Signature[] getAppSignature(Context context) {
        return getAppSignature(context, context.getPackageName());
    }

    @SuppressLint({"PackageManagerGetSignatures"})
    public static Signature[] getAppSignature(Context context, String packageName) {
        if(StringUtils.isSpace(packageName)) {
            return null;
        } else {
            try {
                PackageManager e = context.getPackageManager();
                PackageInfo pi = e.getPackageInfo(packageName, 64);
                return pi == null?null:pi.signatures;
            } catch (NameNotFoundException var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    public static String getAppSignatureSHA1(Context context) {
        return getAppSignatureSHA1(context, context.getPackageName());
    }

    public static String getAppSignatureSHA1(Context context, String packageName) {
        Signature[] signature = getAppSignature(context, packageName);
        return signature == null?null:EncryptUtils.encryptSHA1ToString(signature[0].toByteArray()).replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    public static boolean isAppForeground(Context context) {
        ActivityManager manager = (ActivityManager)context.getSystemService("activity");
        List infos = manager.getRunningAppProcesses();
        if(infos != null && infos.size() != 0) {
            Iterator var3 = infos.iterator();

            RunningAppProcessInfo info;
            do {
                if(!var3.hasNext()) {
                    return false;
                }

                info = (RunningAppProcessInfo)var3.next();
            } while(info.importance != 100);

            return info.processName.equals(context.getPackageName());
        } else {
            return false;
        }
    }

    public static boolean isAppForeground(Context context, String packageName) {
        return !StringUtils.isSpace(packageName) && packageName.equals(ProcessUtils.getForegroundProcessName());
    }

    public static AppUtils.AppInfo getAppInfo(Context context) {
        return getAppInfo(context, context.getPackageName());
    }

    public static AppUtils.AppInfo getAppInfo(Context context, String packageName) {
        try {
            PackageManager e = context.getPackageManager();
            PackageInfo pi = e.getPackageInfo(packageName, 0);
            return getBean(e, pi);
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    private static AppUtils.AppInfo getBean(PackageManager pm, PackageInfo pi) {
        if(pm != null && pi != null) {
            ApplicationInfo ai = pi.applicationInfo;
            String packageName = pi.packageName;
            String name = ai.loadLabel(pm).toString();
            Drawable icon = ai.loadIcon(pm);
            String packagePath = ai.sourceDir;
            String versionName = pi.versionName;
            int versionCode = pi.versionCode;
            boolean isSystem = (1 & ai.flags) != 0;
            return new AppUtils.AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem);
        } else {
            return null;
        }
    }

    public static List<AppUtils.AppInfo> getAppsInfo(Context context) {
        ArrayList list = new ArrayList();
        PackageManager pm = context.getPackageManager();
        List installedPackages = pm.getInstalledPackages(0);
        Iterator var4 = installedPackages.iterator();

        while(var4.hasNext()) {
            PackageInfo pi = (PackageInfo)var4.next();
            AppUtils.AppInfo ai = getBean(pm, pi);
            if(ai != null) {
                list.add(ai);
            }
        }

        return list;
    }

    public static boolean cleanAppData(Context context, String... dirPaths) {
        File[] dirs = new File[dirPaths.length];
        int i = 0;
        String[] var4 = dirPaths;
        int var5 = dirPaths.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String dirPath = var4[var6];
            dirs[i++] = new File(dirPath);
        }

        return cleanAppData(dirs);
    }

    public static boolean cleanAppData(File... dirs) {
        boolean isSuccess = CleanUtils.cleanInternalCache();
        isSuccess &= CleanUtils.cleanInternalDbs();
        isSuccess &= CleanUtils.cleanInternalSP();
        isSuccess &= CleanUtils.cleanInternalFiles();
        isSuccess &= CleanUtils.cleanExternalCache();
        File[] var2 = dirs;
        int var3 = dirs.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            File dir = var2[var4];
            isSuccess &= CleanUtils.cleanCustomCache(dir);
        }

        return isSuccess;
    }

    public static class AppInfo {
        private String name;
        private Drawable icon;
        private String packageName;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public Drawable getIcon() {
            return this.icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return this.isSystem;
        }

        public void setSystem(boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public void setPackageName(String packagName) {
            this.packageName = packagName;
        }

        public String getPackagePath() {
            return this.packagePath;
        }

        public void setPackagePath(String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return this.versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return this.versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public AppInfo(String packageName, String name, Drawable icon, String packagePath, String versionName, int versionCode, boolean isSystem) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
        }

        public String toString() {
            return "App包名：" + this.getPackageName() + "\nApp名称：" + this.getName() + "\nApp图标：" + this.getIcon() + "\nApp路径：" + this.getPackagePath() + "\nApp版本号：" + this.getVersionName() + "\nApp版本码：" + this.getVersionCode() + "\n是否系统App：" + this.isSystem();
        }
    }
}
