package com.liuniukeji.mixin.ui.home;

/**
 * 动态评论内容
 */
public class CommentBean {

    /**
     * "id":"1",   //类型：String  必有字段  备注：评论列表id
     * "add_time":"1531703257",    //类型：String  必有字段  备注：评论时间
     * "content":"", //类型：String  必有字段  备注：评论内容
     * "member_id":"215",   //类型：String  必有字段  备注：评论人id
     * "real_name":"颜如玉",  //类型：String  必有字段  备注：评论人真实姓名
     * "photo_path":"img url",  //类型：String  必有字段  备注：评论人头像地址
     * "sex":"2",   //类型：String  必有字段  备注：评论人性别: 1 男 2 女
     * "experience":150,   //类型：Number  必有字段  备注：评论人等级
     * "vip_type":"0",   //类型：String  必有字段  备注：0,普通用户；1，VIP
     * "school_class":"2008",   //类型：String  必有字段  备注：评论人年级
     * "school_department_name":"五道口金融学院",  //类型：String  必有字段  备注：评论人院系
     * "color":"#000000", //类型：String  必有字段  备注：评论人昵称颜色(会员显示, 非会员默认黑色)
     * "to_real_name":"mock" //类型：String  必有字段  备注：被回复人真实姓名
     */

    private String id;
    private String add_time;
    private String content;
    private String member_id;
    private String real_name;
    private String photo_path;
    private String sex;
    private String experience;
    private String vip_type;
    private String school_class;
    private String school_department_name;
    private String color;
    private String to_real_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public String getVip_type() {
        return vip_type;
    }

    public void setVip_type(String vip_type) {
        this.vip_type = vip_type;
    }

    public String getSchool_class() {
        return school_class;
    }

    public void setSchool_class(String school_class) {
        this.school_class = school_class;
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

    public String getTo_real_name() {
        return to_real_name;
    }

    public void setTo_real_name(String to_real_name) {
        this.to_real_name = to_real_name;
    }
}
