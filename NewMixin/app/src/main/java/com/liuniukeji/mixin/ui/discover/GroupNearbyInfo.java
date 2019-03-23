package com.liuniukeji.mixin.ui.discover;

import java.io.Serializable;

/**
 * 附近的组
 */
public class GroupNearbyInfo implements Serializable{

    /**
     * "id":"1",  //类型：String  必有字段  备注：群组id
     * "name":"嘟嘟",//类型：String  必有字段  备注：群组名称
     * "description":"你说嘴巴嘟嘟",  //类型：String  必有字段  备注：群组描述
     * "logo":"img url",//类型：String  必有字段  备注：群组头像
     * "im_id":"20180706_1453",//类型：String  必有字段  备注：群组环信id
     * "membersonly":"0", //类型：String  必有字段  备注：加群是否需要群主审批:1 是 0 否
     * "is_pwd":"1", //类型：String  必有字段  备注：是否开启群密码: 0不开启 1开启
     * "lng":"115.1", //类型：String  必有字段  备注：群组经度
     * "lat":"38.2",  //类型：String  必有字段  备注：群组纬度
     * "distance":"0km"  //类型：String  必有字段  备注：距离
     */

    private String id;
    private String name;
    private String description;
    private String logo;
    private String im_id;
    private String membersonly;
    private String is_pwd;
    private String lng;
    private String lat;
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }

    public String getMembersonly() {
        return membersonly;
    }

    public void setMembersonly(String membersonly) {
        this.membersonly = membersonly;
    }

    public String getIs_pwd() {
        return is_pwd;
    }

    public void setIs_pwd(String is_pwd) {
        this.is_pwd = is_pwd;
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
}
