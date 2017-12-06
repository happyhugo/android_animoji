//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCache {
    private File cacheDir;
    private String mFileCacheAddrStr;
    public static int count = 0;

    public FileCache(Context context, String fileCacheAddrStr) {
        this.mFileCacheAddrStr = fileCacheAddrStr;
        if(Environment.getExternalStorageState().equals("mounted")) {
            this.cacheDir = new File(Environment.getExternalStorageDirectory(), fileCacheAddrStr);
        } else {
            this.cacheDir = new File(context.getCacheDir().getAbsolutePath(), fileCacheAddrStr);
        }

        if(!this.cacheDir.exists()) {
            this.cacheDir.mkdirs();
        }

    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(this.cacheDir, filename);
        File p = f.getParentFile();
        if(!p.exists()) {
            p.mkdirs();
        }

        return f;
    }

    public String getmFileCacheAddrStr() {
        return this.mFileCacheAddrStr;
    }

    public void clear() {
        File[] files = this.cacheDir.listFiles();
        if(files != null) {
            File[] var2 = files;
            int var3 = files.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                File f = var2[var4];
                f.delete();
            }

        }
    }

    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        File file;
        int byteCount;
        if(oldPath.indexOf(".") == -1) {
            file = new File(newPath);
            file.mkdirs();
            String[] is = new String[0];

            try {
                is = context.getAssets().list(oldPath);
            } catch (IOException var22) {
                Log.e("ContentValues", "getAssets().list failed" + var22);
            }

            String[] fos = is;
            int e1 = is.length;

            for(byteCount = 0; byteCount < e1; ++byteCount) {
                String fileName = fos[byteCount];
                copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
            }
        } else {
            file = new File(newPath);
            if(file.exists()) {
                return;
            }

            BufferedInputStream var25 = null;
            BufferedOutputStream var26 = null;

            try {
                var25 = new BufferedInputStream(context.getAssets().open(oldPath));
                var26 = new BufferedOutputStream(new FileOutputStream(file));
                byte[] var27 = new byte[1024];
                boolean var28 = false;

                while((byteCount = var25.read(var27)) != -1) {
                    var26.write(var27, 0, byteCount);
                }

                var26.flush();
            } catch (IOException var23) {
                Log.e("ContentValues", "close,e:" + var23);
            } finally {
                if(var25 != null) {
                    try {
                        var25.close();
                    } catch (IOException var21) {
                        Log.e("ContentValues", "is close,e:" + var21);
                    }
                }

                if(var26 != null) {
                    try {
                        var26.close();
                    } catch (IOException var20) {
                        Log.e("ContentValues", "fos close,e:" + var20);
                    }
                }

            }
        }

    }
}
