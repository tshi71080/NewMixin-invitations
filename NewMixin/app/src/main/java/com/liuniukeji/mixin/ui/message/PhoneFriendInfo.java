package com.liuniukeji.mixin.ui.message;

/**
 * 手机通讯录好友信息
 */
public class PhoneFriendInfo {

    /**
     * "id":"215",//类型：String  必有字段  备注：用户id
     * "real_name":"颜如玉",//类型：String  必有字段  备注：用户真实姓名
     * "photo_path":"mock",//类型：String  必有字段  备注：用户头像
     * "phone":"18315765138",//类型：String  必有字段  备注：用户手机号
     * "disabled":"0",  //类型：String  必有字段  备注：用户状态 -1 删除 0正常 1禁用
     * "sex":"2",  //类型：String  必有字段  备注：性别: 1 男 2 女
     * "signature":"mock",//类型：String  必有字段  备注：个性签名
     * "experience":"170",//类型：String  必有字段  备注：等级
     * "is_focus":"1", //类型：String  必有字段  备注：是否关注: 1已关注 0未关注
     * "vip_type":"1"//类型：String  必有字段  备注：0,普通用户；1，VIP
     */

    private String id;
    private String real_name;
    private String photo_path;
    private String phone;
    private String disabled;
    private String sex;
    private String signature;
    private String experience;
    private String is_focus;
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

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getIs_focus() {
        return is_focus;
    }

    public void setIs_focus(String is_focus) {
        this.is_focus = is_focus;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }
}
