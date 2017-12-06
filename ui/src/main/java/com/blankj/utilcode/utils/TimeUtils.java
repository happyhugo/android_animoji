//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.annotation.SuppressLint;
import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.ConstUtils.TimeUnit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String[] CHINESE_ZODIAC = new String[]{"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
    private static final String[] ZODIAC = new String[]{"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    private static final int[] ZODIAC_FLAGS = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 24, 23, 22};

    private TimeUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static String millis2String(long millis) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())).format(new Date(millis));
    }

    public static String millis2String(long millis, String pattern) {
        return (new SimpleDateFormat(pattern, Locale.getDefault())).format(new Date(millis));
    }

    public static long string2Millis(String time) {
        return string2Millis(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static long string2Millis(String time, String pattern) {
        try {
            return (new SimpleDateFormat(pattern, Locale.getDefault())).parse(time).getTime();
        } catch (ParseException var3) {
            var3.printStackTrace();
            return -1L;
        }
    }

    public static Date string2Date(String time) {
        return string2Date(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date string2Date(String time, String pattern) {
        return new Date(string2Millis(time, pattern));
    }

    public static String date2String(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String date2String(Date date, String pattern) {
        return (new SimpleDateFormat(pattern, Locale.getDefault())).format(date);
    }

    public static long date2Millis(Date date) {
        return date.getTime();
    }

    public static Date millis2Date(long millis) {
        return new Date(millis);
    }

    public static long getTimeSpan(String time0, String time1, TimeUnit unit) {
        return getTimeSpan(time0, time1, unit, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getTimeSpan(String time0, String time1, TimeUnit unit, String pattern) {
        return ConvertUtils.millis2TimeSpan(Math.abs(string2Millis(time0, pattern) - string2Millis(time1, pattern)), unit);
    }

    public static long getTimeSpan(Date date0, Date date1, TimeUnit unit) {
        return ConvertUtils.millis2TimeSpan(Math.abs(date2Millis(date0) - date2Millis(date1)), unit);
    }

    public static long getTimeSpan(long millis0, long millis1, TimeUnit unit) {
        return ConvertUtils.millis2TimeSpan(Math.abs(millis0 - millis1), unit);
    }

    public static String getFitTimeSpan(String time0, String time1, int precision) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(string2Millis(time0, "yyyy-MM-dd HH:mm:ss") - string2Millis(time1, "yyyy-MM-dd HH:mm:ss")), precision);
    }

    public static String getFitTimeSpan(String time0, String time1, int precision, String pattern) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(string2Millis(time0, pattern) - string2Millis(time1, pattern)), precision);
    }

    public static String getFitTimeSpan(Date date0, Date date1, int precision) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(date2Millis(date0) - date2Millis(date1)), precision);
    }

    public static String getFitTimeSpan(long millis0, long millis1, int precision) {
        return ConvertUtils.millis2FitTimeSpan(Math.abs(millis0 - millis1), precision);
    }

    public static long getNowTimeMills() {
        return System.currentTimeMillis();
    }

    public static String getNowTimeString() {
        return millis2String(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getNowTimeString(String pattern) {
        return millis2String(System.currentTimeMillis(), pattern);
    }

    public static Date getNowTimeDate() {
        return new Date();
    }

    public static long getTimeSpanByNow(String time, TimeUnit unit) {
        return getTimeSpan(getNowTimeString(), time, unit, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getTimeSpanByNow(String time, TimeUnit unit, String pattern) {
        return getTimeSpan(getNowTimeString(), time, unit, pattern);
    }

    public static long getTimeSpanByNow(Date date, TimeUnit unit) {
        return getTimeSpan(new Date(), date, unit);
    }

    public static long getTimeSpanByNow(long millis, TimeUnit unit) {
        return getTimeSpan(System.currentTimeMillis(), millis, unit);
    }

    public static String getFitTimeSpanByNow(String time, int precision) {
        return getFitTimeSpan(getNowTimeString(), time, precision, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getFitTimeSpanByNow(String time, int precision, String pattern) {
        return getFitTimeSpan(getNowTimeString(), time, precision, pattern);
    }

    public static String getFitTimeSpanByNow(Date date, int precision) {
        return getFitTimeSpan(getNowTimeDate(), date, precision);
    }

    public static String getFitTimeSpanByNow(long millis, int precision) {
        return getFitTimeSpan(System.currentTimeMillis(), millis, precision);
    }

    public static String getFriendlyTimeSpanByNow(String time) {
        return getFriendlyTimeSpanByNow(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getFriendlyTimeSpanByNow(String time, String pattern) {
        return getFriendlyTimeSpanByNow(string2Millis(time, pattern));
    }

    public static String getFriendlyTimeSpanByNow(Date date) {
        return getFriendlyTimeSpanByNow(date.getTime());
    }

    @SuppressLint({"DefaultLocale"})
    public static String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long span = now - millis;
        if(span < 0L) {
            return String.format("%tc", new Object[]{Long.valueOf(millis)});
        } else if(span < 1000L) {
            return "刚刚";
        } else if(span < 60000L) {
            return String.format("%d秒前", new Object[]{Long.valueOf(span / 1000L)});
        } else if(span < 3600000L) {
            return String.format("%d分钟前", new Object[]{Long.valueOf(span / 60000L)});
        } else {
            long wee = now / 86400000L * 86400000L;
            return millis >= wee?String.format("今天%tR", new Object[]{Long.valueOf(millis)}):(millis >= wee - 86400000L?String.format("昨天%tR", new Object[]{Long.valueOf(millis)}):String.format("%tF", new Object[]{Long.valueOf(millis)}));
        }
    }

    public static boolean isSameDay(String time) {
        return isSameDay(string2Millis(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static boolean isSameDay(String time, String pattern) {
        return isSameDay(string2Millis(time, pattern));
    }

    public static boolean isSameDay(Date date) {
        return isSameDay(date.getTime());
    }

    public static boolean isSameDay(long millis) {
        long wee = System.currentTimeMillis() / 86400000L * 86400000L;
        return millis >= wee && millis < wee + 86400000L;
    }

    public static boolean isLeapYear(String time) {
        return isLeapYear(string2Date(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static boolean isLeapYear(String time, String pattern) {
        return isLeapYear(string2Date(time, pattern));
    }

    public static boolean isLeapYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(1);
        return isLeapYear(year);
    }

    public static boolean isLeapYear(long millis) {
        return isLeapYear(millis2Date(millis));
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static String getWeek(String time) {
        return getWeek(string2Date(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static String getWeek(String time, String pattern) {
        return getWeek(string2Date(time, pattern));
    }

    public static String getWeek(Date date) {
        return (new SimpleDateFormat("EEEE", Locale.getDefault())).format(date);
    }

    public static String getWeek(long millis) {
        return getWeek(new Date(millis));
    }

    public static int getWeekIndex(String time) {
        return getWeekIndex(string2Date(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static int getWeekIndex(String time, String pattern) {
        return getWeekIndex(string2Date(time, pattern));
    }

    public static int getWeekIndex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(7);
    }

    public static int getWeekIndex(long millis) {
        return getWeekIndex(millis2Date(millis));
    }

    public static int getWeekOfMonth(String time) {
        return getWeekOfMonth(string2Date(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static int getWeekOfMonth(String time, String pattern) {
        return getWeekOfMonth(string2Date(time, pattern));
    }

    public static int getWeekOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(4);
    }

    public static int getWeekOfMonth(long millis) {
        return getWeekOfMonth(millis2Date(millis));
    }

    public static int getWeekOfYear(String time) {
        return getWeekOfYear(string2Date(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static int getWeekOfYear(String time, String pattern) {
        return getWeekOfYear(string2Date(time, pattern));
    }

    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(3);
    }

    public static int getWeekOfYear(long millis) {
        return getWeekOfYear(millis2Date(millis));
    }

    public static String getChineseZodiac(String time) {
        return getChineseZodiac(string2Date(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static String getChineseZodiac(String time, String pattern) {
        return getChineseZodiac(string2Date(time, pattern));
    }

    public static String getChineseZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return CHINESE_ZODIAC[cal.get(1) % 12];
    }

    public static String getChineseZodiac(long millis) {
        return getChineseZodiac(millis2Date(millis));
    }

    public static String getChineseZodiac(int year) {
        return CHINESE_ZODIAC[year % 12];
    }

    public static String getZodiac(String time) {
        return getZodiac(string2Date(time, "yyyy-MM-dd HH:mm:ss"));
    }

    public static String getZodiac(String time, String pattern) {
        return getZodiac(string2Date(time, pattern));
    }

    public static String getZodiac(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(2) + 1;
        int day = cal.get(5);
        return getZodiac(month, day);
    }

    public static String getZodiac(long millis) {
        return getZodiac(millis2Date(millis));
    }

    public static String getZodiac(int month, int day) {
        return ZODIAC[day >= ZODIAC_FLAGS[month - 1]?month - 1:(month + 10) % 12];
    }
}
