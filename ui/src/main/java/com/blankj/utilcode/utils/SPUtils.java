//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

public class SPUtils {
    private SharedPreferences sp;
    private Editor editor;

    public SPUtils(String spName) {
        this.sp = Utils.getContext().getSharedPreferences(spName, 0);
        this.editor = this.sp.edit();
        this.editor.apply();
    }

    public void putString(String key, String value) {
        this.editor.putString(key, value).apply();
    }

    public String getString(String key) {
        return this.getString(key, (String)null);
    }

    public String getString(String key, String defaultValue) {
        return this.sp.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        this.editor.putInt(key, value).apply();
    }

    public int getInt(String key) {
        return this.getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return this.sp.getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        this.editor.putLong(key, value).apply();
    }

    public long getLong(String key) {
        return this.getLong(key, -1L);
    }

    public long getLong(String key, long defaultValue) {
        return this.sp.getLong(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        this.editor.putFloat(key, value).apply();
    }

    public float getFloat(String key) {
        return this.getFloat(key, -1.0F);
    }

    public float getFloat(String key, float defaultValue) {
        return this.sp.getFloat(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        this.editor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.sp.getBoolean(key, defaultValue);
    }

    public Map<String, ?> getAll() {
        return this.sp.getAll();
    }

    public void remove(String key) {
        this.editor.remove(key).apply();
    }

    public boolean contains(String key) {
        return this.sp.contains(key);
    }

    public void clear() {
        this.editor.clear().apply();
    }
}
