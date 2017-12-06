//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

public class ConstUtils {
    public static final int KB = 1024;
    public static final int MB = 1048576;
    public static final int GB = 1073741824;
    public static final int SEC = 1000;
    public static final int MIN = 60000;
    public static final int HOUR = 3600000;
    public static final int DAY = 86400000;
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    public static final String REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";
    public static final String REGEX_BLANK_LINE = "\\n\\s*\\r";
    public static final String REGEX_TENCENT_NUM = "[1-9][0-9]{4,}";
    public static final String REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)";
    public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
    public static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";
    public static final String REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$";
    public static final String REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
    public static final String REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    public static final String REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

    private ConstUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static enum TimeUnit {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY;

        private TimeUnit() {
        }
    }

    public static enum MemoryUnit {
        BYTE,
        KB,
        MB,
        GB;

        private MemoryUnit() {
        }
    }
}
