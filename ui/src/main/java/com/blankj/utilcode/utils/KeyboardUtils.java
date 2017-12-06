//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.blankj.utilcode.utils.Utils;

public class KeyboardUtils {
    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if(view == null) {
            view = new View(activity);
        }

        InputMethodManager imm = (InputMethodManager)activity.getSystemService("input_method");
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void clickBlankArea2HideSoftInput() {
        Log.d("tips", "U should copy the following code.");
    }

    public static void showSoftInput(EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager)Utils.getContext().getSystemService("input_method");
        imm.showSoftInput(edit, 0);
    }

    public static void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager)Utils.getContext().getSystemService("input_method");
        imm.toggleSoftInput(2, 0);
    }
}
