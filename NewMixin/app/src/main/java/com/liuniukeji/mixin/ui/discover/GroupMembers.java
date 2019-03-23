package com.liuniukeji.mixin.ui.discover;

/**
 * 群组成员列表
 */
public class GroupMembers {

    /**
     * "group_im_id":"20180706_1453", //类型：String  必有字段  备注：群组环信id
     * "member_im_name":"mock",//类型：String  必有字段  备注：群成员环信id
     * "member_id":"mock",//类型：String  必有字段  备注：群成员用户id
     * "type":"1",  //类型：String  必有字段  备注：群内角色，0普通用户，1群主，2管理员
     * "real_name":"嘟嘟", //类型：String  必有字段  备注：群成员真实姓名
     * "photo_path":"img url", //类型：String  必有字段  备注：群成员头像
     * "sex":"1", //类型：String  必有字段  备注：群成员性别:0 保密 1 男 2 女
     * "experience":"80",  //类型：String  必有字段  备注：群成员等级
     * "signature":"哈哈哈",  //类型：String  必有字段  备注：群成员个性签名
     * "vip_type":"0"   //类型：String  必有字段  备注：0,普通用户；1，VIP
     */

    private String group_im_id;
    private String member_im_name;
    private String member_id;
    private String type;
    private String real_name;
    private String photo_path;
    private String sex;
    private String experience;
    private String signature;
    private String vip_type;


    public String getGroup_im_id() {
        return group_im_id;
    }

    public void setGroup_im_id(String group_im_id) {
        this.group_im_id = group_im_id;
    }

    public String getMember_im_name() {
        return member_im_name;
    }

    public void setMember_im_name(String member_im_name) {
        this.member_im_name = member_im_name;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }
}
