package com.liuniukeji.mixin.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

/**
 * 定位工具类
 */
public class LocationUtils {

    private static final long REFRESH_TIME = 5000L;
    private static final float METER_POSITION = 0.0f;
    private static ILocationListener mLocationListener;
    private static LocationListener listener = new MyLocationListener();

    private static class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (mLocationListener != null) {
                mLocationListener.onSuccessLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * GPS获取定位方式
     */
    public static Location getGPSLocation(@NonNull Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return location;
    }

    /**
     * network获取定位方式
     */
    public static Location getNetWorkLocation(Context context) {
        Location location = null;
        LocationManager manager = getLocationManager(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    /**
     * 获取最好的定位方式
     */
    public static Location getBestLocation(Context context, Criteria criteria) {
        Location location;
        LocationManager manager = getLocationManager(context);
        if (criteria == null) {
            criteria = new Criteria();
        }
        String provider = manager.getBestProvider(criteria, true);
        if (TextUtils.isEmpty(provider)) {

            location = getNetWorkLocation(context);
        } else {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            location = manager.getLastKnownLocation(provider);
        }
        return location;
    }

    /**
     * 定位监听
     */
    public static void addLocationListener(Context context, String provider, ILocationListener locationListener) {

        addLocationListener(context, provider, REFRESH_TIME, METER_POSITION, locationListener);
    }

    /**
     * 定位监听
     */
    public static void addLocationListener(Context context, String provider, long time,
                                           float meter, ILocationListener locationListener) {
        if (locationListener != null) {
            mLocationListener = locationListener;
        }
        if (listener == null) {
            listener = new MyLocationListener();
        }
        LocationManager manager = getLocationManager(context);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(provider, time, meter, listener);
    }

    /**
     * 取消定位监听
     */
    public static void unRegisterListener(Context context) {
        if (listener != null) {
            LocationManager manager = getLocationManager(context);
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            manager.removeUpdates(listener);
        }
    }

    private static LocationManager getLocationManager(@NonNull Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 自定义接口
     */
    public interface ILocationListener {
        void onSuccessLocation(Location location);
    }


}
