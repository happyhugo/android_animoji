//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.auth;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCipher {
    private static final byte[] IV = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public AESCipher() {
    }

    public static String encrypt(String seed, String clearText) {
        if(null != clearText && clearText.length() >= 1) {
            try {
                byte[] rawkey = getRawKey(seed);
                byte[] e = encrypt(rawkey, getBytes(clearText));
                return bytesToString(Base64.encode(e));
            } catch (Exception var4) {
                var4.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String decrypt(String seed, String encrypted) {
        if(null != encrypted && encrypted.length() >= 1) {
            try {
                byte[] rawKey = getRawKey(seed);
                byte[] e = Base64.decode(encrypted);
                byte[] result = decrypt(rawKey, e);
                Log.d("tracker", "decrypt" + new String(result, "utf-8"));
                return new String(result, "utf-8");
            } catch (Exception var5) {
                var5.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(IV);
        cipher.init(1, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        IvParameterSpec iv = new IvParameterSpec(IV);
        cipher.init(2, skeySpec, iv);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    private static byte[] getRawKey(String password) {
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
        }

        digest.reset();
        return digest.digest(password.getBytes());
    }

    public static byte[] getBytes(String string) {
        byte[] bytes = new byte[0];
        if(TextUtils.isEmpty(string)) {
            return bytes;
        } else {
            try {
                bytes = string.getBytes("UTF-8");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }

            return bytes;
        }
    }

    public static String bytesToString(byte[] bytes) {
        String string = "";
        if(bytes == null) {
            return null;
        } else {
            try {
                string = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }

            return string;
        }
    }
}
