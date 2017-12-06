//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrashUtils implements UncaughtExceptionHandler {
    private static volatile CrashUtils mInstance;
    private UncaughtExceptionHandler mHandler;
    private boolean mInitialized;
    private String crashDir;
    private String versionName;
    private int versionCode;

    private CrashUtils() {
    }

    public static CrashUtils getInstance() {
        if(mInstance == null) {
            Class var0 = CrashUtils.class;
            synchronized(CrashUtils.class) {
                if(mInstance == null) {
                    mInstance = new CrashUtils();
                }
            }
        }

        return mInstance;
    }

    public boolean init() {
        if(this.mInitialized) {
            return true;
        } else {
            File e;
            if("mounted".equals(Environment.getExternalStorageState())) {
                e = Utils.getContext().getExternalCacheDir();
                if(e == null) {
                    return false;
                }

                this.crashDir = e.getPath() + File.separator + "crash" + File.separator;
            } else {
                e = Utils.getContext().getCacheDir();
                if(e == null) {
                    return false;
                }

                this.crashDir = e.getPath() + File.separator + "crash" + File.separator;
            }

            try {
                PackageInfo e1 = Utils.getContext().getPackageManager().getPackageInfo(Utils.getContext().getPackageName(), 0);
                this.versionName = e1.versionName;
                this.versionCode = e1.versionCode;
            } catch (NameNotFoundException var2) {
                var2.printStackTrace();
                return false;
            }

            this.mHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(this);
            return this.mInitialized = true;
        }
    }

    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = (new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date());
        final String fullPath = this.crashDir + now + ".txt";
        if(FileUtils.createOrExistsFile(fullPath)) {
            (new Thread(new Runnable() {
                public void run() {
                    PrintWriter pw = null;

                    try {
                        pw = new PrintWriter(new FileWriter(fullPath, false));
                        pw.write(CrashUtils.this.getCrashHead());
                        throwable.printStackTrace(pw);

                        for(Throwable e = throwable.getCause(); e != null; e = e.getCause()) {
                            e.printStackTrace(pw);
                        }
                    } catch (IOException var6) {
                        var6.printStackTrace();
                    } finally {
                        CloseUtils.closeIO(new Closeable[]{pw});
                    }

                }
            })).start();
            if(this.mHandler != null) {
                this.mHandler.uncaughtException(thread, throwable);
            }

        }
    }

    private String getCrashHead() {
        return "\n************* Crash Log Head ****************\nDevice Manufacturer: " + Build.MANUFACTURER + "\nDevice Model       : " + Build.MODEL + "\nAndroid Version    : " + VERSION.RELEASE + "\nAndroid SDK        : " + VERSION.SDK_INT + "\nApp VersionName    : " + this.versionName + "\nApp VersionCode    : " + this.versionCode + "\n************* Crash Log Head ****************\n\n";
    }
}
