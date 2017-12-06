//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import com.blankj.utilcode.utils.ShellUtils;
import com.blankj.utilcode.utils.Utils;
import com.blankj.utilcode.utils.ShellUtils.CommandResult;
import java.io.File;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

public class DeviceUtils {
    private DeviceUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = new String[]{"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        String[] var2 = locations;
        int var3 = locations.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String location = var2[var4];
            if((new File(location + su)).exists()) {
                return true;
            }
        }

        return false;
    }

    public static int getSDKVersion() {
        return VERSION.SDK_INT;
    }

    @SuppressLint({"HardwareIds"})
    public static String getAndroidID() {
        return Secure.getString(Utils.getContext().getContentResolver(), "android_id");
    }

    public static String getMacAddress() {
        String macAddress = getMacAddressByWifiInfo();
        if(!"02:00:00:00:00:00".equals(macAddress)) {
            return macAddress;
        } else {
            macAddress = getMacAddressByNetworkInterface();
            if(!"02:00:00:00:00:00".equals(macAddress)) {
                return macAddress;
            } else {
                macAddress = getMacAddressByFile();
                return !"02:00:00:00:00:00".equals(macAddress)?macAddress:"please open wifi";
            }
        }
    }

    @SuppressLint({"HardwareIds"})
    private static String getMacAddressByWifiInfo() {
        try {
            WifiManager e = (WifiManager)Utils.getContext().getApplicationContext().getSystemService("wifi");
            if(e != null) {
                WifiInfo info = e.getConnectionInfo();
                if(info != null) {
                    return info.getMacAddress();
                }
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            ArrayList e = Collections.list(NetworkInterface.getNetworkInterfaces());
            Iterator var1 = e.iterator();

            while(var1.hasNext()) {
                NetworkInterface ni = (NetworkInterface)var1.next();
                if(ni.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if(macBytes != null && macBytes.length > 0) {
                        StringBuilder res1 = new StringBuilder();
                        byte[] var5 = macBytes;
                        int var6 = macBytes.length;

                        for(int var7 = 0; var7 < var6; ++var7) {
                            byte b = var5[var7];
                            res1.append(String.format("%02x:", new Object[]{Byte.valueOf(b)}));
                        }

                        return res1.deleteCharAt(res1.length() - 1).toString();
                    }
                }
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByFile() {
        CommandResult result = ShellUtils.execCmd("getprop wifi.interface", false);
        if(result.result == 0) {
            String name = result.successMsg;
            if(name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/" + name + "/address", false);
                if(result.result == 0 && result.successMsg != null) {
                    return result.successMsg;
                }
            }
        }

        return "02:00:00:00:00:00";
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getModel() {
        String model = Build.MODEL;
        if(model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }

        return model;
    }

    public static void shutdown() {
        ShellUtils.execCmd("reboot -p", true);
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        intent.setFlags(268435456);
        Utils.getContext().startActivity(intent);
    }

    public static void reboot() {
        ShellUtils.execCmd("reboot", true);
        Intent intent = new Intent("android.intent.action.REBOOT");
        intent.putExtra("nowait", 1);
        intent.putExtra("interval", 1);
        intent.putExtra("window", 0);
        Utils.getContext().sendBroadcast(intent);
    }

    public static void reboot(String reason) {
        PowerManager mPowerManager = (PowerManager)Utils.getContext().getSystemService("power");

        try {
            mPowerManager.reboot(reason);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static void reboot2Recovery() {
        ShellUtils.execCmd("reboot recovery", true);
    }

    public static void reboot2Bootloader() {
        ShellUtils.execCmd("reboot bootloader", true);
    }

    public static String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10;
        String serial = null;

        try {
            serial = Build.class.getField("SERIAL").get((Object)null).toString();
            return (new UUID((long)m_szDevIDShort.hashCode(), (long)serial.hashCode())).toString();
        } catch (Exception var3) {
            serial = "serial";
            return (new UUID((long)m_szDevIDShort.hashCode(), (long)serial.hashCode())).toString();
        }
    }
}
