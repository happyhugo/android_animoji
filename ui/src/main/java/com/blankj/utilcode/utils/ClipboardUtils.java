//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import com.blankj.utilcode.utils.Utils;

public class ClipboardUtils {
    private ClipboardUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static void copyText(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager)Utils.getContext().getSystemService("clipboard");
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    public static CharSequence getText() {
        ClipboardManager clipboard = (ClipboardManager)Utils.getContext().getSystemService("clipboard");
        ClipData clip = clipboard.getPrimaryClip();
        return clip != null && clip.getItemCount() > 0?clip.getItemAt(0).coerceToText(Utils.getContext()):null;
    }

    public static void copyUri(Uri uri) {
        ClipboardManager clipboard = (ClipboardManager)Utils.getContext().getSystemService("clipboard");
        clipboard.setPrimaryClip(ClipData.newUri(Utils.getContext().getContentResolver(), "uri", uri));
    }

    public static Uri getUri() {
        ClipboardManager clipboard = (ClipboardManager)Utils.getContext().getSystemService("clipboard");
        ClipData clip = clipboard.getPrimaryClip();
        return clip != null && clip.getItemCount() > 0?clip.getItemAt(0).getUri():null;
    }

    public static void copyIntent(Intent intent) {
        ClipboardManager clipboard = (ClipboardManager)Utils.getContext().getSystemService("clipboard");
        clipboard.setPrimaryClip(ClipData.newIntent("intent", intent));
    }

    public static Intent getIntent() {
        ClipboardManager clipboard = (ClipboardManager)Utils.getContext().getSystemService("clipboard");
        ClipData clip = clipboard.getPrimaryClip();
        return clip != null && clip.getItemCount() > 0?clip.getItemAt(0).getIntent():null;
    }
}
