//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.annotation.TargetApi;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

public class SDCardUtils {
    private SDCardUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isSDCardEnable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static String getSDCardPath() {
        if(!isSDCardEnable()) {
            return "sdcard unable!";
        } else {
            String cmd = "cat /proc/mounts";
            Runtime run = Runtime.getRuntime();
            BufferedReader bufferedReader = null;

            try {
                Process e = run.exec(cmd);
                bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(e.getInputStream())));

                do {
                    String lineStr;
                    if((lineStr = bufferedReader.readLine()) == null) {
                        return Environment.getExternalStorageDirectory().getPath() + File.separator;
                    }

                    if(lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                        String[] strArray = lineStr.split(" ");
                        if(strArray.length >= 5) {
                            String var6 = strArray[1].replace("/.android_secure", "") + File.separator;
                            return var6;
                        }
                    }
                } while(e.waitFor() == 0 || e.exitValue() != 1);

                return Environment.getExternalStorageDirectory().getPath() + File.separator;
            } catch (Exception var10) {
                var10.printStackTrace();
                return Environment.getExternalStorageDirectory().getPath() + File.separator;
            } finally {
                CloseUtils.closeIO(new Closeable[]{bufferedReader});
            }
        }
    }

    public static String getDataPath() {
        return !isSDCardEnable()?"sdcard unable!":Environment.getExternalStorageDirectory().getPath() + File.separator + "data" + File.separator;
    }

    @TargetApi(18)
    public static String getFreeSpace() {
        if(!isSDCardEnable()) {
            return "sdcard unable!";
        } else {
            StatFs stat = new StatFs(getSDCardPath());
            long availableBlocks = stat.getAvailableBlocksLong();
            long blockSize = stat.getBlockSizeLong();
            return ConvertUtils.byte2FitMemorySize(availableBlocks * blockSize);
        }
    }

    @TargetApi(18)
    public static String getSDCardInfo() {
        SDCardUtils.SDCardInfo sd = new SDCardUtils.SDCardInfo();
        if(!isSDCardEnable()) {
            return "sdcard unable!";
        } else {
            sd.isExist = true;
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            sd.totalBlocks = sf.getBlockCountLong();
            sd.blockByteSize = sf.getBlockSizeLong();
            sd.availableBlocks = sf.getAvailableBlocksLong();
            sd.availableBytes = sf.getAvailableBytes();
            sd.freeBlocks = sf.getFreeBlocksLong();
            sd.freeBytes = sf.getFreeBytes();
            sd.totalBytes = sf.getTotalBytes();
            return sd.toString();
        }
    }

    public static class SDCardInfo {
        boolean isExist;
        long totalBlocks;
        long freeBlocks;
        long availableBlocks;
        long blockByteSize;
        long totalBytes;
        long freeBytes;
        long availableBytes;

        public SDCardInfo() {
        }

        public String toString() {
            return "isExist=" + this.isExist + "\ntotalBlocks=" + this.totalBlocks + "\nfreeBlocks=" + this.freeBlocks + "\navailableBlocks=" + this.availableBlocks + "\nblockByteSize=" + this.blockByteSize + "\ntotalBytes=" + this.totalBytes + "\nfreeBytes=" + this.freeBytes + "\navailableBytes=" + this.availableBytes;
        }
    }
}
