package com.liuniukeji.mixin.util;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description
 * @Author (WangJinXiang / 825055521 @ qq.com)
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date 2018年11月20日 09:15:41
 * @CreateBy Android Studio
 */

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.liuniukeji.mixin.XyqApplication;
import com.tencent.mmkv.MMKV;

public class BDLoacationHelper {
    private LocationCallBack callBack;
    private static BDLoacationHelper helper;
    private LocationClient locationClient;
    private MyBDLocationListener locationListener = new MyBDLocationListener();

    private BDLoacationHelper() {
        //第一步实例化定位核心类
        locationClient = new LocationClient(XyqApplication.getInstance(), getLocOption());
        //第二步设置位置变化回调监听
        locationClient.registerLocationListener(locationListener);
    }

    public static BDLoacationHelper getInstance() {
        if (helper == null) {
            helper = new BDLoacationHelper();
        }
        return helper;
    }

    public void start() {
//       第三步开始定位
        locationClient.start();
    }

    //一般会在Activity的OnDestroy方法调用
    public void stop() {
        if (locationClient != null) {
//            locationClient.unRegisterLocationListener(locationListener);
            locationClient.stop();
        }
    }

    private LocationClientOption getLocOption() {
        LocationClientOption option = new LocationClientOption();
        option.setAddrType("all");
        //设置定位坐标系
        option.setCoorType("bd09ll");
        //重新定位时间间隔
        option.setScanSpan(0);
        //设置是否打开gps
        option.setOpenGps(true);
        //设置定位模式
        option.setLocationNotify(true);
        //是否需要poi结果
        option.setIsNeedLocationPoiList(true);
        return option;
    }

    public class MyBDLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (callBack != null && bdLocation != null) {
                callBack.callBack(bdLocation.getAddrStr(), bdLocation.getStreetNumber(), bdLocation.getLatitude(), bdLocation.getLongitude(), bdLocation.getDistrict(), bdLocation.getStreet(), bdLocation.getCity(), bdLocation.getProvince());
            }
            //多次定位必须要调用stop方法
            MMKV.defaultMMKV().encode("lat", bdLocation.getLatitude() + "");
            MMKV.defaultMMKV().encode("lng", bdLocation.getLongitude() + "");
            MMKV.defaultMMKV().encode("city", bdLocation.getProvince() + bdLocation.getCity() + bdLocation.getDistrict());
            MMKV.defaultMMKV().encode("province", bdLocation.getProvince());
            MMKV.defaultMMKV().encode("ct", bdLocation.getCity());
            MMKV.defaultMMKV().encode("district", bdLocation.getDistrict());
            MMKV.defaultMMKV().encode("desc", bdLocation.getStreet() + "");
            MMKV.defaultMMKV().encode("code", bdLocation.getAdCode() + "");
            locationClient.stop();
        }
    }

    public interface LocationCallBack {
        void callBack(String addr, String streetnumber, double lat, double lng, String district, String street, String city, String province);
    }

    public LocationCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(LocationCallBack callBack) {
        this.callBack = callBack;
    }
}
