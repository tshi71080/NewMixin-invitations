package com.liuniukeji.mixin.util;

import java.util.List;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.liuniukeji.mixin.util.currency.ToastUtils;

public class GPSService extends Service {

    // 位置的管理者
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;
    private SharedPreferences sp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences("config", MODE_PRIVATE);
// 1.获取位置的管理者
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
// 2.获取定位方式
// 2.1获取所有的定位方式，true:返回可用的定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String string : providers) {
            System.out.println(string + "3");
        }
// 2.2获取最佳的定位方式
        Criteria criteria = new Criteria();
// 设置可以定位海拔,true：表示可以定位海拔
        criteria.setAltitudeRequired(true);// 只有gps可以定位海拔
// criteria ： 设置定位属性
// enabledOnly ： 如果定位可以就返回
        String bestProvider = locationManager.getBestProvider(criteria, true);
        System.out.println("GPSService 最佳的定位方式:" + bestProvider);
// 3.定位
        myLocationListener = new MyLocationListener();
// provider : 定位方式
// minTime ：定位的最小时间间隔
// minDistance：最小的定位距离
// listener ： LocationListener监听


        locationManager.requestLocationUpdates(bestProvider, 0, 0,
                myLocationListener);
    }

    // 4.创建一个定位的监听
    private class MyLocationListener implements LocationListener {
        // 定位位置发生变化的时候调用
// location ： 当前的位置
        @Override
        public void onLocationChanged(Location location) {
            System.out.println(location + "oooooooooooooooooo");
// 5.获取经纬度
            location.getAccuracy();// 获取精确的位置
            location.getAltitude();// 获取海拔
            double latitude = location.getLatitude();// 获取纬度，平行
            double longitude = location.getLongitude();// 获取经度
//保存经纬度坐标
            Editor edit = sp.edit();
            edit.putString("longitude", longitude + "");
            edit.putString("latitude", latitude + "");
            edit.commit();


            ToastUtils.showShortToast("lat:"+latitude+"//"+longitude);
// Timer timer = new Timer();
// //task : 定时执行的任务，
// //when:延迟的时间，延迟多长时间执行定时任务
// //period ： 每次执行的间隔时间
// timer.schedule(task, when, 1000*60*30)
        }

        // 定位状态发生变化的时候调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // 定位可用的时候调用
        @Override
        public void onProviderEnabled(String provider) {

        }

        // 定位不可用的时候调用
        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //5.取消定位
        //在高版本中已经无效了，因为高版本中规定必须由用户自己去打开和关闭gps
        //locationManager.removeUpdates(myLocationListener);

    }

}
