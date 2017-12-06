//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.kiwi.tracker.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.agora.tracker.AGFaceTracker;
import com.agora.tracker.auth.AESCipher;
import com.blankj.utilcode.utils.DeviceUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.kiwi.tracker.JNIFaceTracker;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class JniMethods {
    public JniMethods() {
    }

    public static String onlineAuth(Context context, String license, String key) {
        new Exception("aaaaa").printStackTrace();
        startRequest(context, license, key);
        return "";
    }

    private static void startRequest(Context context, String license, String key) {
        JniMethods.OnlineAuthThread t = new JniMethods.OnlineAuthThread(context, license, key);
        t.start();
    }

    private static class OnlineAuthThread extends Thread {
        private String license;
        private String key;
        private Context mContext;

        public OnlineAuthThread(Context context, String license, String key) {
            this.mContext = context;
            this.license = license;
            this.key = key;
        }

        public void run() {
            this.authRequest();
        }

        private void authRequest() {
//            String path = "https://apps.kiwiapp.mobi/api/partners/auth";
//            String requestString = "";
//
//            try {
//                URL ret = new URL(path);
//                HttpsURLConnection conn = (HttpsURLConnection)ret.openConnection();
//                conn.setConnectTimeout(2000);
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("Charset", "UTF-8");
//                conn.addRequestProperty("x-access-key", this.license);
//                conn.addRequestProperty("x-bundle-id", this.mContext.getPackageName());
//                conn.addRequestProperty("device-id", DeviceUtils.getUniquePsuedoID());
//                conn.addRequestProperty("cache-control", "no-cache");
//                byte[] requestStringBytes = requestString.getBytes("UTF-8");
//                OutputStream outputStream = conn.getOutputStream();
//                outputStream.write(requestStringBytes);
//                outputStream.close();
//                int responseCode = conn.getResponseCode();
//                if(responseCode == 200) {
//                    this.updateLicense(conn);
//                } else if(responseCode == 401) {
//                    this.updateLicense(conn);
//                }
//            } catch (Exception var8) {
//                Log.e("Tracker", var8.toString());
//            }

            System.out.println("aaaaaaa:key:"+this.key);
            System.out.println("aaaaaaa:liense:"+AGFaceTracker.getLicense());
            System.out.println("aaaaaaa:decrypt:"+AESCipher.decrypt(this.key, AGFaceTracker.getLicense()));
            int ret1 = JNIFaceTracker.auth(this.mContext, "20170925#20171202#com.kiwi.tracker.testing171201");
            if(ret1 < 0) {
                Log.e("Tracker", "auth license failed,retCode:" + ret1);
            } else {
                Log.i("Tracker", "auth license success");
            }

        }

        private void updateLicense(HttpsURLConnection conn) throws Exception {
            String responseJson = this.readFromResp(conn);
            if(StringUtils.isEmpty(responseJson)) {
                Log.e("Tracker", "responseJson is empty");
            } else {
                Log.d("Tracker", "responseJson:" + responseJson);
                JSONObject result = new JSONObject(responseJson);
                String license = result.getString("license");
                if(!StringUtils.isEmpty(license)) {
                    Log.i("Tracker", "network response license:" + license);
                    AGFaceTracker.setLicense(license);
                } else {
                    Log.e("Tracker", "network response license is empty");
                }

            }
        }

        @NonNull
        private String readFromResp(HttpsURLConnection conn) throws IOException {
            StringBuffer sb = new StringBuffer();
            String readLine = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            while((readLine = responseReader.readLine()) != null) {
                sb.append(readLine).append("\n");
            }

            responseReader.close();
            return sb.toString();
        }
    }
}
