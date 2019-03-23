package com.liuniukeji.mixin.ui.loacation;

/**
 * 位置信息
 */
public class LocationInfo {

    private String lat;
    private String lng;

    public LocationInfo() {
    }

    public LocationInfo(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
