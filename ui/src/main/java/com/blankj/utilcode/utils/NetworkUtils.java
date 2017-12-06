//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ShellUtils;
import com.blankj.utilcode.utils.Utils;
import com.blankj.utilcode.utils.ShellUtils.CommandResult;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NetworkUtils {
    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;

    private NetworkUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static void openWirelessSettings() {
        if(VERSION.SDK_INT > 10) {
            Utils.getContext().startActivity((new Intent("android.settings.WIRELESS_SETTINGS")).setFlags(268435456));
        } else {
            Utils.getContext().startActivity((new Intent("android.settings.SETTINGS")).setFlags(268435456));
        }

    }

    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager)Utils.getContext().getSystemService("connectivity");
        return cm.getActiveNetworkInfo();
    }

    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static boolean isAvailableByPing() {
        CommandResult result = ShellUtils.execCmd("ping -c 1 -w 1 223.5.5.5", false);
        boolean ret = result.result == 0;
        if(result.errorMsg != null) {
            LogUtils.d("isAvailableByPing errorMsg", result.errorMsg);
        }

        if(result.successMsg != null) {
            LogUtils.d("isAvailableByPing successMsg", result.successMsg);
        }

        return ret;
    }

    public static boolean getDataEnabled() {
        try {
            TelephonyManager e = (TelephonyManager)Utils.getContext().getSystemService("phone");
            Method getMobileDataEnabledMethod = e.getClass().getDeclaredMethod("getDataEnabled", new Class[0]);
            if(null != getMobileDataEnabledMethod) {
                return ((Boolean)getMobileDataEnabledMethod.invoke(e, new Object[0])).booleanValue();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return false;
    }

    public static void setDataEnabled(boolean enabled) {
        try {
            TelephonyManager e = (TelephonyManager)Utils.getContext().getSystemService("phone");
            Method setMobileDataEnabledMethod = e.getClass().getDeclaredMethod("setDataEnabled", new Class[]{Boolean.TYPE});
            if(null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(e, new Object[]{Boolean.valueOf(enabled)});
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static boolean is4G() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.getSubtype() == 13;
    }

    public static boolean getWifiEnabled() {
        WifiManager wifiManager = (WifiManager)Utils.getContext().getApplicationContext().getSystemService("wifi");
        return wifiManager.isWifiEnabled();
    }

    public static void setWifiEnabled(boolean enabled) {
        WifiManager wifiManager = (WifiManager)Utils.getContext().getApplicationContext().getSystemService("wifi");
        if(enabled) {
            if(!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else if(wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

    }

    public static boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager)Utils.getContext().getSystemService("connectivity");
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getType() == 1;
    }

    public static boolean isWifiAvailable() {
        return getWifiEnabled() && isAvailableByPing();
    }

    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        return tm != null?tm.getNetworkOperatorName():null;
    }

    public static NetworkUtils.NetworkType getNetworkType() {
        NetworkUtils.NetworkType netType = NetworkUtils.NetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo();
        if(info != null && info.isAvailable()) {
            if(info.getType() == 1) {
                netType = NetworkUtils.NetworkType.NETWORK_WIFI;
            } else if(info.getType() == 0) {
                switch(info.getSubtype()) {
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                    case 16:
                        netType = NetworkUtils.NetworkType.NETWORK_2G;
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                    case 17:
                        netType = NetworkUtils.NetworkType.NETWORK_3G;
                        break;
                    case 13:
                    case 18:
                        netType = NetworkUtils.NetworkType.NETWORK_4G;
                        break;
                    default:
                        String subtypeName = info.getSubtypeName();
                        if(!subtypeName.equalsIgnoreCase("TD-SCDMA") && !subtypeName.equalsIgnoreCase("WCDMA") && !subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetworkUtils.NetworkType.NETWORK_UNKNOWN;
                        } else {
                            netType = NetworkUtils.NetworkType.NETWORK_3G;
                        }
                }
            } else {
                netType = NetworkUtils.NetworkType.NETWORK_UNKNOWN;
            }
        }

        return netType;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            while(true) {
                NetworkInterface ni;
                do {
                    if(!e.hasMoreElements()) {
                        return null;
                    }

                    ni = (NetworkInterface)e.nextElement();
                } while(!ni.isUp());

                Enumeration addresses = ni.getInetAddresses();

                while(addresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)addresses.nextElement();
                    if(!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(58) < 0;
                        if(useIPv4) {
                            if(isIPv4) {
                                return hostAddress;
                            }
                        } else if(!isIPv4) {
                            int index = hostAddress.indexOf(37);
                            return index < 0?hostAddress.toUpperCase():hostAddress.substring(0, index).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static String getDomainAddress(final String domain) {
        try {
            ExecutorService e = Executors.newCachedThreadPool();
            Future fs = e.submit(new Callable() {
                public String call() throws Exception {
                    try {
                        InetAddress inetAddress = InetAddress.getByName(domain);
                        return inetAddress.getHostAddress();
                    } catch (UnknownHostException var3) {
                        var3.printStackTrace();
                        return null;
                    }
                }
            });
            return (String)fs.get();
        } catch (ExecutionException | InterruptedException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static enum NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO;

        private NetworkType() {
        }
    }
}
