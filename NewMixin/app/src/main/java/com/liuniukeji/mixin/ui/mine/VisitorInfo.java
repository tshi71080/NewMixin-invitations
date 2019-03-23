package com.liuniukeji.mixin.ui.mine;

/**
 * 访客信息
 */
public class VisitorInfo {

    /**
     * "id":"216",  //类型：String  必有字段  备注：访客id
     * "photo_path":"img url",  //类型：String  必有字段  备注：访客头像地址
     * "real_name":"嘟嘟",  //类型：String  必有字段  备注：访客真实姓名
     * "sex":"1", //类型：String  必有字段  备注：访客性别: 1 男 2 女
     * "experience":"150",//类型：String  必有字段  备注：访客等级
     * "signature":"日发售的规范", //类型：String  必有字段  备注：访客个性签名
     * "update_time":"11月09日" //类型：String  必有字段  备注：访问时间
     *  "vip_type":"1"//类型：String  必有字段  备注：0,普通用户；1，VIP
     */

    private String id;
    private String photo_path;
    private String real_name;
    private String sex;
    private String experience;
    private String signature;
    private String update_time;
    private String vip_type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }
}
