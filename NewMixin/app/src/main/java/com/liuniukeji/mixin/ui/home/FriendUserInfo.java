package com.liuniukeji.mixin.ui.home;

import java.util.List;

/**
 * 好友资料详情
 */
public class FriendUserInfo {

    /**
     "id":"216", //类型：String  必有字段  备注：对方id
     "real_name":"嘟嘟",  //类型：String  必有字段  备注：对方真实姓名
     "photo_path":"url ",  //类型：String  必有字段  备注：对方头像
     "im_name":"201807120941_216", //类型：String  必有字段  备注：对方环信id
     "sex":"1",//类型：String  必有字段  备注：对方性别: 1男 2 女
     "birthday":"1970-01-01",   //类型：String  必有字段  备注：对方生日
     "is_show_birthday":"0",  //类型：String  必有字段  备注：是否展示生日: 0不展示1展示
     "fans":"0", //类型：String  必有字段  备注：粉丝数
     "follows":"1", //类型：String  必有字段  备注：关注数
     "is_focus":"0",  //类型：String  必有字段  备注：是否关注: 0未关注 1已关注
     "is_quietly":"0",  //类型：String  必有字段  备注：是否悄悄关注: 0否 1是
     "moments":"0",  //类型：String  必有字段  备注：动态数量
     "signature":"日发售的规范",  //类型：String  必有字段  备注：个性签名
     "experience":130,  //类型：Number  必有字段  备注：等级
     "school_class":"2008",    //类型：String  必有字段  备注：年级
     "address":"mock",   //类型：String  必有字段  备注：地址

     "interests": - [   //类型：Array  必有字段  备注：无
     "漫画"            //类型：String  必有字段  备注：爱好
     ],

     "cover_path":"http://adiyun.my/",  //类型：String  必有字段  备注：主页头部封面图片
     "vip_type":"0",//类型：String  必有字段  备注：0,普通用户；1，VIP
     "school_department_name":"五道口金融学院", //类型：String  必有字段  备注：院系
     "color":"#000000"  //类型：String  必有字段  备注：昵称颜色(会员显示, 非会员默认黑色)
     */


    private String id;
    private String real_name;
    private String photo_path;
    private String im_name;
    private String sex;
    private String birthday;
    private String is_show_birthday;
    private String fans;
    private String follows;
    private String is_focus;
    private String is_quietly;
    private String moments;
    private String signature;
    private String experience;
    private String school_class;
    private String address;
    private String remark;

    private List<String> interests;

    private String cover_path;
    private String vip_type;
    private String school_department_name;
    private String color;


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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIs_show_birthday() {
        return is_show_birthday;
    }

    public void setIs_show_birthday(String is_show_birthday) {
        this.is_show_birthday = is_show_birthday;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getIs_focus() {
        return is_focus;
    }

    public void setIs_focus(String is_focus) {
        this.is_focus = is_focus;
    }

    public String getIs_quietly() {
        return is_quietly;
    }

    public void setIs_quietly(String is_quietly) {
        this.is_quietly = is_quietly;
    }

    public String getMoments() {
        return moments;
    }

    public void setMoments(String moments) {
        this.moments = moments;
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

    public String getSchool_class() {
        return school_class;
    }

    public void setSchool_class(String school_class) {
        this.school_class = school_class;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setCover_path(String cover_path) {
        this.cover_path = cover_path;
    }

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getSchool_department_name() {
        return school_department_name;
    }

    public void setSchool_department_name(String school_department_name) {
        this.school_department_name = school_department_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
