//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kiwi.tracker;

public class JNIFaceTracker {
    public JNIFaceTracker() {
    }

    public static native int init(Object var0, String var1, String var2);

    public static native int initExt(Object var0, String var1, String var2, boolean var3);

    public static native int track(byte[] var0, int var1, int var2, int var3, int var4, int var5, float[] var6, float[] var7, int[] var8, float[] var9, boolean[] var10);

    public static native void destory();

    public static native int auth(Object var0, String var1);

    static {
        System.loadLibrary("ftlib");
    }
}
