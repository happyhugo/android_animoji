package com.wen.hugo.util;

import android.content.Context;

import com.hyphenate.easeui.EaseUI;

/**
 * Created by hugo on 11/29/17.
 */

public class EaseUIHelper {

    public static void init(Context applicationContext){
        EaseUI.getInstance().init(applicationContext, null);
    }
}
