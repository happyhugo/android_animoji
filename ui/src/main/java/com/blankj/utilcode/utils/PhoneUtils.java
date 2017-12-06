//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Xml;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlSerializer;

public class PhoneUtils {
    private PhoneUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isPhone() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        return tm != null && tm.getPhoneType() != 0;
    }

    @SuppressLint({"HardwareIds"})
    public static String getIMEI() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        return tm != null?tm.getDeviceId():null;
    }

    @SuppressLint({"HardwareIds"})
    public static String getIMSI() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        return tm != null?tm.getSubscriberId():null;
    }

    public static int getPhoneType() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        return tm != null?tm.getPhoneType():-1;
    }

    public static boolean isSimCardReady() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        return tm != null && tm.getSimState() == 5;
    }

    public static String getSimOperatorName() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        return tm != null?tm.getSimOperatorName():null;
    }

    public static String getSimOperatorByMnc() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        String operator = tm != null?tm.getSimOperator():null;
        if(operator == null) {
            return null;
        } else {
            byte var3 = -1;
            switch(operator.hashCode()) {
                case 49679470:
                    if(operator.equals("46000")) {
                        var3 = 0;
                    }
                    break;
                case 49679471:
                    if(operator.equals("46001")) {
                        var3 = 3;
                    }
                    break;
                case 49679472:
                    if(operator.equals("46002")) {
                        var3 = 1;
                    }
                    break;
                case 49679473:
                    if(operator.equals("46003")) {
                        var3 = 4;
                    }
                case 49679474:
                case 49679475:
                case 49679476:
                default:
                    break;
                case 49679477:
                    if(operator.equals("46007")) {
                        var3 = 2;
                    }
            }

            switch(var3) {
                case 0:
                case 1:
                case 2:
                    return "中国移动";
                case 3:
                    return "中国联通";
                case 4:
                    return "中国电信";
                default:
                    return operator;
            }
        }
    }

    public static String getPhoneStatus() {
        TelephonyManager tm = (TelephonyManager)Utils.getContext().getSystemService("phone");
        String str = "";
        str = str + "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
        str = str + "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str = str + "Line1Number = " + tm.getLine1Number() + "\n";
        str = str + "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str = str + "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str = str + "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str = str + "NetworkType = " + tm.getNetworkType() + "\n";
        str = str + "honeType = " + tm.getPhoneType() + "\n";
        str = str + "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str = str + "SimOperator = " + tm.getSimOperator() + "\n";
        str = str + "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str = str + "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
        str = str + "SimState = " + tm.getSimState() + "\n";
        str = str + "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
        str = str + "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
        return str;
    }

    public static void dial(String phoneNumber) {
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + phoneNumber));
        intent.addFlags(268435456);
        Utils.getContext().startActivity(intent);
    }

    public static void call(String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        intent.addFlags(268435456);
        Utils.getContext().startActivity(intent);
    }

    public static void sendSms(String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (StringUtils.isEmpty(phoneNumber)?"":phoneNumber));
        Intent intent = new Intent("android.intent.action.SENDTO", uri);
        intent.putExtra("sms_body", StringUtils.isEmpty(content)?"":content);
        Utils.getContext().startActivity(intent);
    }

    public static void sendSmsSilent(String phoneNumber, String content) {
        if(!StringUtils.isEmpty(content)) {
            PendingIntent sentIntent = PendingIntent.getBroadcast(Utils.getContext(), 0, new Intent(), 0);
            SmsManager smsManager = SmsManager.getDefault();
            if(content.length() >= 70) {
                ArrayList ms = smsManager.divideMessage(content);
                Iterator var5 = ms.iterator();

                while(var5.hasNext()) {
                    String str = (String)var5.next();
                    smsManager.sendTextMessage(phoneNumber, (String)null, str, sentIntent, (PendingIntent)null);
                }
            } else {
                smsManager.sendTextMessage(phoneNumber, (String)null, content, sentIntent, (PendingIntent)null);
            }

        }
    }

    public static List<HashMap<String, String>> getAllContactInfo() {
        SystemClock.sleep(3000L);
        ArrayList list = new ArrayList();
        ContentResolver resolver = Utils.getContext().getContentResolver();
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, (String)null, (String[])null, (String)null);

        while(true) {
            String contact_id;
            do {
                if(!cursor.moveToNext()) {
                    cursor.close();
                    return list;
                }

                contact_id = cursor.getString(0);
            } while(StringUtils.isEmpty(contact_id));

            Cursor c = resolver.query(date_uri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{contact_id}, (String)null);
            HashMap map = new HashMap();

            while(c.moveToNext()) {
                String data1 = c.getString(0);
                String mimetype = c.getString(1);
                if(mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                    map.put("phone", data1);
                } else if(mimetype.equals("vnd.android.cursor.item/name")) {
                    map.put("name", data1);
                }
            }

            list.add(map);
            c.close();
        }
    }

    public static void getContactNum() {
        Log.d("tips", "U should copy the following code.");
    }

    public static void getAllSMS() {
        ContentResolver resolver = Utils.getContext().getContentResolver();
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, (String)null, (String[])null, (String)null);
        int count = cursor.getCount();
        XmlSerializer xmlSerializer = Xml.newSerializer();

        try {
            xmlSerializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backupsms.xml")), "utf-8");
            xmlSerializer.startDocument("utf-8", Boolean.valueOf(true));
            xmlSerializer.startTag((String)null, "smss");

            while(cursor.moveToNext()) {
                SystemClock.sleep(1000L);
                xmlSerializer.startTag((String)null, "sms");
                xmlSerializer.startTag((String)null, "address");
                String e = cursor.getString(0);
                xmlSerializer.text(e);
                xmlSerializer.endTag((String)null, "address");
                xmlSerializer.startTag((String)null, "date");
                String date = cursor.getString(1);
                xmlSerializer.text(date);
                xmlSerializer.endTag((String)null, "date");
                xmlSerializer.startTag((String)null, "type");
                String type = cursor.getString(2);
                xmlSerializer.text(type);
                xmlSerializer.endTag((String)null, "type");
                xmlSerializer.startTag((String)null, "body");
                String body = cursor.getString(3);
                xmlSerializer.text(body);
                xmlSerializer.endTag((String)null, "body");
                xmlSerializer.endTag((String)null, "sms");
                System.out.println("address:" + e + "   date:" + date + "  type:" + type + "  body:" + body);
            }

            xmlSerializer.endTag((String)null, "smss");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }
}
