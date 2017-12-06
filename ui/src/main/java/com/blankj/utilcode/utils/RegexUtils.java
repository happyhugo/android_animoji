//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
    private RegexUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isMobileSimple(CharSequence input) {
        return isMatch("^[1]\\d{10}$", input);
    }

    public static boolean isMobileExact(CharSequence input) {
        return isMatch("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$", input);
    }

    public static boolean isTel(CharSequence input) {
        return isMatch("^0\\d{2,3}[- ]?\\d{7,8}", input);
    }

    public static boolean isIDCard15(CharSequence input) {
        return isMatch("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$", input);
    }

    public static boolean isIDCard18(CharSequence input) {
        return isMatch("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$", input);
    }

    public static boolean isEmail(CharSequence input) {
        return isMatch("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", input);
    }

    public static boolean isURL(CharSequence input) {
        return isMatch("[a-zA-z]+://[^\\s]*", input);
    }

    public static boolean isZh(CharSequence input) {
        return isMatch("^[\\u4e00-\\u9fa5]+$", input);
    }

    public static boolean isUsername(CharSequence input) {
        return isMatch("^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$", input);
    }

    public static boolean isDate(CharSequence input) {
        return isMatch("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$", input);
    }

    public static boolean isIP(CharSequence input) {
        return isMatch("((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)", input);
    }

    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    public static List<String> getMatches(String regex, CharSequence input) {
        if(input == null) {
            return null;
        } else {
            ArrayList matches = new ArrayList();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(input);

            while(matcher.find()) {
                matches.add(matcher.group());
            }

            return matches;
        }
    }

    public static String[] getSplits(String input, String regex) {
        return input == null?null:input.split(regex);
    }

    public static String getReplaceFirst(String input, String regex, String replacement) {
        return input == null?null:Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    public static String getReplaceAll(String input, String regex, String replacement) {
        return input == null?null:Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }
}
