//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import java.io.File;

public class IntentUtils {
    private IntentUtils() {
        throw new UnsupportedOperationException("u can\'t fuck me...");
    }

    public static Intent getInstallAppIntent(String filePath) {
        return getInstallAppIntent(FileUtils.getFileByPath(filePath));
    }

    public static Intent getInstallAppIntent(File file) {
        if(file == null) {
            return null;
        } else {
            Intent intent = new Intent("android.intent.action.VIEW");
            String type;
            if(VERSION.SDK_INT < 23) {
                type = "application/vnd.android.package-archive";
            } else {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file));
            }

            if(VERSION.SDK_INT >= 22) {
                intent.setFlags(1);
                Uri contentUri = FileProvider.getUriForFile(Utils.getContext(), "com.your.package.fileProvider", file);
                intent.setDataAndType(contentUri, type);
            }

            intent.setDataAndType(Uri.fromFile(file), type);
            return intent.addFlags(268435456);
        }
    }

    public static Intent getUninstallAppIntent(String packageName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(268435456);
    }

    public static Intent getLaunchAppIntent(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    public static Intent getAppDetailsSettingsIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(268435456);
    }

    public static Intent getShareTextIntent(String content) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", content);
        return intent.setFlags(268435456);
    }

    public static Intent getShareImageIntent(String content, String imagePath) {
        return getShareImageIntent(content, FileUtils.getFileByPath(imagePath));
    }

    public static Intent getShareImageIntent(String content, File image) {
        return !FileUtils.isFileExists(image)?null:getShareImageIntent(content, Uri.fromFile(image));
    }

    public static Intent getShareImageIntent(String content, Uri uri) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", content);
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.setType("image/*");
        return intent.setFlags(268435456);
    }

    public static Intent getComponentIntent(String packageName, String className) {
        return getComponentIntent(packageName, className, (Bundle)null);
    }

    public static Intent getComponentIntent(String packageName, String className, Bundle bundle) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if(bundle != null) {
            intent.putExtras(bundle);
        }

        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return intent.addFlags(268435456);
    }

    public static Intent getShutdownIntent() {
        Intent intent = new Intent("android.intent.action.ACTION_SHUTDOWN");
        return intent.addFlags(268435456);
    }

    public static Intent getCaptureIntent(Uri outUri) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", outUri);
        return intent.addFlags(268435457);
    }
}
