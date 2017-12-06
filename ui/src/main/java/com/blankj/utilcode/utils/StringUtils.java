//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

public class StringUtils {
    private StringUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isSpace(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if(a == b) {
            return true;
        } else {
            int length;
            if(a != null && b != null && (length = a.length()) == b.length()) {
                if(a instanceof String && b instanceof String) {
                    return a.equals(b);
                } else {
                    for(int i = 0; i < length; ++i) {
                        if(a.charAt(i) != b.charAt(i)) {
                            return false;
                        }
                    }

                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        return a == b || b != null && a.length() == b.length() && a.regionMatches(true, 0, b, 0, b.length());
    }

    public static String null2Length0(String s) {
        return s == null?"":s;
    }

    public static int length(CharSequence s) {
        return s == null?0:s.length();
    }

    public static String upperFirstLetter(String s) {
        return !isEmpty(s) && Character.isLowerCase(s.charAt(0))?(char)(s.charAt(0) - 32) + s.substring(1):s;
    }

    public static String lowerFirstLetter(String s) {
        return !isEmpty(s) && Character.isUpperCase(s.charAt(0))?(char)(s.charAt(0) + 32) + s.substring(1):s;
    }

    public static String reverse(String s) {
        int len = length(s);
        if(len <= 1) {
            return s;
        } else {
            int mid = len >> 1;
            char[] chars = s.toCharArray();

            for(int i = 0; i < mid; ++i) {
                char c = chars[i];
                chars[i] = chars[len - i - 1];
                chars[len - i - 1] = c;
            }

            return new String(chars);
        }
    }

    public static String toDBC(String s) {
        if(isEmpty(s)) {
            return s;
        } else {
            char[] chars = s.toCharArray();
            int i = 0;

            for(int len = chars.length; i < len; ++i) {
                if(chars[i] == 12288) {
                    chars[i] = 32;
                } else if('！' <= chars[i] && chars[i] <= '～') {
                    chars[i] -= 'ﻠ';
                } else {
                    chars[i] = chars[i];
                }
            }

            return new String(chars);
        }
    }

    public static String toSBC(String s) {
        if(isEmpty(s)) {
            return s;
        } else {
            char[] chars = s.toCharArray();
            int i = 0;

            for(int len = chars.length; i < len; ++i) {
                if(chars[i] == 32) {
                    chars[i] = 12288;
                } else if(33 <= chars[i] && chars[i] <= 126) {
                    chars[i] += 'ﻠ';
                } else {
                    chars[i] = chars[i];
                }
            }

            return new String(chars);
        }
    }
}
