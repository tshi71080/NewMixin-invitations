package com.liuniukeji.mixin.ui.discover;

/**
 * 群组通知
 */
public class GroupNoticeInfo {

    /**
     * "id":"2",  //类型：String  必有字段  备注：申请id
     * "real_name":"颜如玉",//类型：String  必有字段  备注：真实姓名
     * "photo_path":"mock", //类型：String  必有字段  备注：头像地址
     * "sex":"2",//类型：String  必有字段  备注：性别:0 保密 1 男 2 女
     * "experience":"100", //类型：String  必有字段  备注：等级
     * "note":"嘟嘟嘟",   //类型：String  必有字段  备注：申请理由
     * "group_im_id":"20180706_1453",  //类型：String  必有字段  备注：群组环信id
     * "member_im_name":"mock", //类型：String  必有字段  备注：用户环信id
     * "name":"嘟嘟" //类型：String  必有字段  备注：群名称
     */

    private String id;
    private String real_name;
    private String photo_path;
    private String sex;
    private String experience;
    private String note;
    private String group_im_id;
    private String member_im_name;
    private String name;


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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
