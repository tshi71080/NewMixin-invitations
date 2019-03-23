package com.liuniukeji.mixin.ui.discover;

/**
 * 兴趣组
 */
public class SearchGroupInfo {

    /**
     "id":"1",//类型：String  必有字段  备注：群组id
     "member_id":"216",  //类型：String  必有字段  备注：群主id
     "name":"嘟嘟嘟啦啦啦", //类型：String  必有字段  备注：群组名称
     "description":"mock", //类型：String  必有字段  备注：群组简介
     "lng":"115.1123", //类型：String  必有字段  备注：群组经度
     "lat":"38.201",   //类型：String  必有字段  备注：群组纬度
     "logo":"img url",   //类型：String  必有字段  备注：群组头像
     "type":"0", //类型：String  必有字段  备注：0 公开群 1私有群
     "im_id":"20180706_1453"  //类型：String  必有字段  备注：群组环信ID
     */

    private String id;
    private String member_id;
    private String name;
    private String description;
    private String lng;
    private String lat;
    private String logo;
    private String type;
    private String im_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }
}
