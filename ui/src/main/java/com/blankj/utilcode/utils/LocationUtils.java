//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.blankj.utilcode.utils.Utils;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {
    private static LocationUtils.OnLocationChangeListener mListener;
    private static LocationUtils.MyLocationListener myLocationListener;
    private static LocationManager mLocationManager;

    private LocationUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static boolean isGpsEnabled() {
        LocationManager lm = (LocationManager)Utils.getContext().getSystemService("location");
        return lm.isProviderEnabled("gps");
    }

    public static boolean isLocationEnabled() {
        LocationManager lm = (LocationManager)Utils.getContext().getSystemService("location");
        return lm.isProviderEnabled("network") || lm.isProviderEnabled("gps");
    }

    public static void openGpsSettings() {
        Intent intent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        intent.setFlags(268435456);
        Utils.getContext().startActivity(intent);
    }

    public static boolean register(long minTime, long minDistance, LocationUtils.OnLocationChangeListener listener) {
        if(listener == null) {
            return false;
        } else {
            mLocationManager = (LocationManager)Utils.getContext().getSystemService("location");
            mListener = listener;
            if(!isLocationEnabled()) {
                ToastUtils.showShortToastSafe("无法定位，请打开定位服务");
                return false;
            } else {
                String provider = mLocationManager.getBestProvider(getCriteria(), true);
                Location location = mLocationManager.getLastKnownLocation(provider);
                if(location != null) {
                    listener.getLastKnownLocation(location);
                }

                if(myLocationListener == null) {
                    myLocationListener = new LocationUtils.MyLocationListener();
                }

                mLocationManager.requestLocationUpdates(provider, minTime, (float)minDistance, myLocationListener);
                return true;
            }
        }
    }

    public static void unregister() {
        if(mLocationManager != null) {
            if(myLocationListener != null) {
                mLocationManager.removeUpdates(myLocationListener);
                myLocationListener = null;
            }

            mLocationManager = null;
        }

    }

    private static Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(1);
        return criteria;
    }

    public static Address getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(Utils.getContext(), Locale.getDefault());

        try {
            List e = geocoder.getFromLocation(latitude, longitude, 1);
            if(e.size() > 0) {
                return (Address)e.get(0);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return null;
    }

    public static String getCountryName(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null?"unknown":address.getCountryName();
    }

    public static String getLocality(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null?"unknown":address.getLocality();
    }

    public static String getStreet(double latitude, double longitude) {
        Address address = getAddress(latitude, longitude);
        return address == null?"unknown":address.getAddressLine(0);
    }

    public interface OnLocationChangeListener {
        void getLastKnownLocation(Location var1);

        void onLocationChanged(Location var1);

        void onStatusChanged(String var1, int var2, Bundle var3);
    }

    private static class MyLocationListener implements LocationListener {
        private MyLocationListener() {
        }

        public void onLocationChanged(Location location) {
            if(LocationUtils.mListener != null) {
                LocationUtils.mListener.onLocationChanged(location);
            }

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            if(LocationUtils.mListener != null) {
                LocationUtils.mListener.onStatusChanged(provider, status, extras);
            }

            switch(status) {
                case 0:
                    LogUtils.d("onStatusChanged", "当前GPS状态为服务区外状态");
                    break;
                case 1:
                    LogUtils.d("onStatusChanged", "当前GPS状态为暂停服务状态");
                    break;
                case 2:
                    LogUtils.d("onStatusChanged", "当前GPS状态为可见状态");
            }

        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    }
}
