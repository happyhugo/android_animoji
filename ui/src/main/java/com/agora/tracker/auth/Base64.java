//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.agora.tracker.auth;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64 {
    private static final Encoder encoder = new Base64Encoder();

    public Base64() {
    }

    public static byte[] encode(byte[] data) {
        if(data == null) {
            return new byte[0];
        } else {
            int len = (data.length + 2) / 3 * 4;
            ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);

            try {
                encoder.encode(data, 0, data.length, bOut);
            } catch (IOException var4) {
                throw new RuntimeException("exception encoding base64 string: " + var4);
            }

            return bOut.toByteArray();
        }
    }

    public static int encode(byte[] data, OutputStream out) throws IOException {
        return encoder.encode(data, 0, data.length, out);
    }

    public static int encode(byte[] data, int off, int length, OutputStream out) throws IOException {
        return encoder.encode(data, off, length, out);
    }

    public static byte[] decode(byte[] data) {
        int len = data.length / 4 * 3;
        ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);

        try {
            encoder.decode(data, 0, data.length, bOut);
        } catch (IOException var4) {
            throw new RuntimeException("exception decoding base64 string: " + var4);
        }

        return bOut.toByteArray();
    }

    public static byte[] decode(String data) {
        if(TextUtils.isEmpty(data)) {
            return new byte[0];
        } else {
            int len = data.length() / 4 * 3;
            ByteArrayOutputStream bOut = new ByteArrayOutputStream(len);

            try {
                encoder.decode(data, bOut);
            } catch (IOException var4) {
                throw new RuntimeException("exception decoding base64 string: " + var4);
            }

            return bOut.toByteArray();
        }
    }

    public static int decode(String data, OutputStream out) throws IOException {
        return encoder.decode(data, out);
    }
}
