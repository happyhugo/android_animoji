//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.os.Build.VERSION;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class EmptyUtils {
    private EmptyUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isEmpty(Object obj) {
        return obj == null?true:(obj instanceof String && obj.toString().length() == 0?true:(obj.getClass().isArray() && Array.getLength(obj) == 0?true:(obj instanceof Collection && ((Collection)obj).isEmpty()?true:(obj instanceof Map && ((Map)obj).isEmpty()?true:(obj instanceof SparseArray && ((SparseArray)obj).size() == 0?true:(obj instanceof SparseBooleanArray && ((SparseBooleanArray)obj).size() == 0?true:(obj instanceof SparseIntArray && ((SparseIntArray)obj).size() == 0?true:VERSION.SDK_INT >= 18 && obj instanceof SparseLongArray && ((SparseLongArray)obj).size() == 0)))))));
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
