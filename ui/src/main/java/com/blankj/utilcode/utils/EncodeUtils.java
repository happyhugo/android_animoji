//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.os.Build.VERSION;
import android.text.Html;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncodeUtils {
    private EncodeUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static String urlEncode(String input) {
        return urlEncode(input, "UTF-8");
    }

    public static String urlEncode(String input, String charset) {
        try {
            return URLEncoder.encode(input, charset);
        } catch (UnsupportedEncodingException var3) {
            return input;
        }
    }

    public static String urlDecode(String input) {
        return urlDecode(input, "UTF-8");
    }

    public static String urlDecode(String input, String charset) {
        try {
            return URLDecoder.decode(input, charset);
        } catch (UnsupportedEncodingException var3) {
            return input;
        }
    }

    public static byte[] base64Encode(String input) {
        return base64Encode(input.getBytes());
    }

    public static byte[] base64Encode(byte[] input) {
        return Base64.encode(input, 2);
    }

    public static String base64Encode2String(byte[] input) {
        return Base64.encodeToString(input, 2);
    }

    public static byte[] base64Decode(String input) {
        return Base64.decode(input, 2);
    }

    public static byte[] base64Decode(byte[] input) {
        return Base64.decode(input, 2);
    }

    public static byte[] base64UrlSafeEncode(String input) {
        return Base64.encode(input.getBytes(), 8);
    }

    public static String htmlEncode(CharSequence input) {
        if(VERSION.SDK_INT >= 16) {
            return Html.escapeHtml(input);
        } else {
            StringBuilder out = new StringBuilder();
            int i = 0;

            for(int len = input.length(); i < len; ++i) {
                char c = input.charAt(i);
                if(c == 60) {
                    out.append("&lt;");
                } else if(c == 62) {
                    out.append("&gt;");
                } else if(c == 38) {
                    out.append("&amp;");
                } else if(c >= '\ud800' && c <= '\udfff') {
                    if(c < '\udc00' && i + 1 < len) {
                        char d = input.charAt(i + 1);
                        if(d >= '\udc00' && d <= '\udfff') {
                            ++i;
                            int codepoint = 65536 | c - '\ud800' << 10 | d - '\udc00';
                            out.append("&#").append(codepoint).append(";");
                        }
                    }
                } else if(c <= 126 && c >= 32) {
                    if(c != 32) {
                        out.append(c);
                    } else {
                        while(i + 1 < len && input.charAt(i + 1) == 32) {
                            out.append("&nbsp;");
                            ++i;
                        }

                        out.append(' ');
                    }
                } else {
                    out.append("&#").append(c).append(";");
                }
            }

            return out.toString();
        }
    }
}
