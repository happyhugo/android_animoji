//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogUtils {
    private static boolean logSwitch = true;
    private static boolean log2FileSwitch = false;
    private static char logFilter = 118;
    private static String tag = "TAG";
    private static String dir = null;

    private LogUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static void init(boolean logSwitch, boolean log2FileSwitch, char logFilter, String tag) {
        if("mounted".equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator;
        }

        LogUtils.logSwitch = logSwitch;
        LogUtils.log2FileSwitch = log2FileSwitch;
        LogUtils.logFilter = logFilter;
        LogUtils.tag = tag;
    }

    public static LogUtils.Builder getBuilder() {
        if("mounted".equals(Environment.getExternalStorageState())) {
            dir = Utils.getContext().getExternalCacheDir().getPath() + File.separator + "log" + File.separator;
        } else {
            dir = Utils.getContext().getCacheDir().getPath() + File.separator + "log" + File.separator;
        }

        return new LogUtils.Builder();
    }

    public static void v(Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'i');
    }

    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    public static void d(Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'd');
    }

    public static void d(String tag, Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'd');
    }

    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    public static void i(Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'i');
    }

    public static void i(String tag, Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'i');
    }

    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    public static void w(Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'w');
    }

    public static void w(String tag, Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'w');
    }

    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    public static void e(Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'e');
    }

    public static void e(String tag, Object msg) {
        log(tag, msg.toString(), (Throwable)null, 'e');
    }

    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    private static void log(String tag, String msg, Throwable tr, char type) {
        if(logSwitch) {
            if(101 != type || 101 != logFilter && 118 != logFilter) {
                if(119 == type && (119 == logFilter || 118 == logFilter)) {
                    Log.w(generateTag(tag), msg, tr);
                } else if(100 != type || 100 != logFilter && 118 != logFilter) {
                    if(105 == type && (100 == logFilter || 118 == logFilter)) {
                        Log.i(generateTag(tag), msg, tr);
                    }
                } else {
                    Log.d(generateTag(tag), msg, tr);
                }
            } else {
                Log.e(generateTag(tag), msg, tr);
            }

            if(log2FileSwitch) {
                log2File(type, generateTag(tag), msg + '\n' + Log.getStackTraceString(tr));
            }
        }

    }

    private static synchronized void log2File(char type, String tag, String content) {
        if(content != null) {
            Date now = new Date();
            String date = (new SimpleDateFormat("MM-dd", Locale.getDefault())).format(now);
            final String fullPath = dir + date + ".txt";
            if(FileUtils.createOrExistsFile(fullPath)) {
                String time = (new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.getDefault())).format(now);
                final String dateLogContent = time + ":" + type + ":" + tag + ":" + content + '\n';
                (new Thread(new Runnable() {
                    public void run() {
                        BufferedWriter bw = null;

                        try {
                            bw = new BufferedWriter(new FileWriter(fullPath, true));
                            bw.write(dateLogContent);
                        } catch (IOException var6) {
                            var6.printStackTrace();
                        } finally {
                            CloseUtils.closeIO(new Closeable[]{bw});
                        }

                    }
                })).start();
            }
        }
    }

    private static String generateTag(String tag) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[4];
        String format = "Tag[" + tag + "] %s[%s, %d]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        return String.format(format, new Object[]{callerClazzName, caller.getMethodName(), Integer.valueOf(caller.getLineNumber())});
    }

    public static class Builder {
        private boolean logSwitch = true;
        private boolean log2FileSwitch = false;
        private char logFilter = 118;
        private String tag = "TAG";

        public Builder() {
        }

        public LogUtils.Builder setLogSwitch(boolean logSwitch) {
            this.logSwitch = logSwitch;
            return this;
        }

        public LogUtils.Builder setLog2FileSwitch(boolean log2FileSwitch) {
            this.log2FileSwitch = log2FileSwitch;
            return this;
        }

        public LogUtils.Builder setLogFilter(char logFilter) {
            this.logFilter = logFilter;
            return this;
        }

        public LogUtils.Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public void create() {
            LogUtils.logSwitch = this.logSwitch;
            LogUtils.log2FileSwitch = this.log2FileSwitch;
            LogUtils.logFilter = this.logFilter;
            LogUtils.tag = this.tag;
        }
    }
}
