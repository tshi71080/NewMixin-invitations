package com.liuniukeji.mixin.ui.mine;

/**
 * 粉丝信息
 */
public class FansInfo {

    /**
     * "id":"216",//类型：String  必有字段  备注：粉丝id
     * "real_name":"嘟嘟", //类型：String  必有字段  备注：真实姓名
     * "photo_path":"img url",//类型：String  必有字段  备注：头像地址
     * "type":"2",//类型：String  必有字段  备注：1管理员、2用户，3老师
     * "signature":"哈哈哈",//类型：String  必有字段  备注：个性签名
     * "isfriend":"0" //类型：String  必有字段  备注：是否互为关注:0不是 1是
     * "sex":"1",  //类型：String  必有字段  备注：性别: 0 保密 1 男 2 女
     * "vip_type":"1"//类型：String  必有字段  备注：0,普通用户；1，VIP
     * "experience":"150",//类型：String  必有字段  备注：用户等级
     */

    private String id;
    private String real_name;
    private String photo_path;
    private String type;
    private String signature;
    private String isfriend;
    private String sex;
    private String vip_type;
    private String experience;

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

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(String isfriend) {
        this.isfriend = isfriend;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
