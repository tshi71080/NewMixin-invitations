package com.liuniukeji.mixin.ui.discover;

/**
 * 搜索到的用户
 */
public class SearchUser  {

    /**
     * "id":"216", //类型：String  必有字段  备注：对方id
     * "real_name":"嘟嘟", //类型：String  必有字段  备注：对方真实姓名
     * "signature":"日发售的规范", //类型：String  必有字段  备注：对方个性签名
     * "photo_path":"full url",  //类型：String  必有字段  备注：对方头像地址
     * "im_name":"mock", //类型：String  必有字段  备注：对方环信id
     * "sex":"1",   //类型：String  必有字段  备注：对方性别: 1男 2女
     * "experience":3  //类型：Number  必有字段  备注：对方等级
     * "vip_type":"1" //类型：String  必有字段  备注：0,普通用户；1，VIP
     */

    private String id;
    private String real_name;
    private String signature;
    private String photo_path;
    private String im_name;
    private String sex;
    private String experience;
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

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }
}
