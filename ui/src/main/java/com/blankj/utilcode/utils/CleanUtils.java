//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import java.io.File;

public class CleanUtils {
    private CleanUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean cleanInternalCache() {
        return FileUtils.deleteFilesInDir(Utils.getContext().getCacheDir());
    }

    public static boolean cleanInternalFiles() {
        return FileUtils.deleteFilesInDir(Utils.getContext().getFilesDir());
    }

    public static boolean cleanInternalDbs() {
        return FileUtils.deleteFilesInDir(Utils.getContext().getFilesDir().getParent() + File.separator + "databases");
    }

    public static boolean cleanInternalDbByName(String dbName) {
        return Utils.getContext().deleteDatabase(dbName);
    }

    public static boolean cleanInternalSP() {
        return FileUtils.deleteFilesInDir(Utils.getContext().getFilesDir().getParent() + File.separator + "shared_prefs");
    }

    public static boolean cleanExternalCache() {
        return SDCardUtils.isSDCardEnable() && FileUtils.deleteFilesInDir(Utils.getContext().getExternalCacheDir());
    }

    public static boolean cleanCustomCache(String dirPath) {
        return FileUtils.deleteFilesInDir(dirPath);
    }

    public static boolean cleanCustomCache(File dir) {
        return FileUtils.deleteFilesInDir(dir);
    }
}
