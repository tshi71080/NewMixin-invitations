package com.liuniukeji.mixin.ui.discover;

/**
 * 附近的人
 */
public class PeopleNearbyInfo {

    /**
     * "id":"215",//类型：String  必有字段  备注：附近的人id
     * "real_name":"颜如玉",//类型：String  必有字段  备注：附近的人真实姓名
     * "signature":"mock",//类型：String  必有字段  备注：附近的人个性签名
     * "photo_path":"mock",//类型：String  必有字段  备注：附近的人头像
     * "im_name":"201807120941_215", //类型：String  必有字段  备注：附近的人环信id
     * "sex":"2",//类型：String  必有字段  备注：附近的人性别:0 保密 1 男 2 女
     * "experience":"120",//类型：String  必有字段  备注：附近的人经验值
     * "lng":"115.1234455",//类型：String  必有字段  备注：附近的人经度
     * "lat":"38.2", //类型：String  必有字段  备注：附近的人纬度
     * "distance":"1km" //类型：String  必有字段  备注：距离
     * "vip_type":"0" //类型：String  必有字段  备注：0,普通用户；1，VIP
     */

    private String id;
    private String real_name;
    private String signature;
    private String photo_path;
    private String im_name;
    private String sex;
    private String experience;
    private String lng;
    private String lat;
    private String distance;
    private String vip_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getIm_name() {
        return im_name;
    }

    public void setIm_name(String im_name) {
        this.im_name = im_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }
}
