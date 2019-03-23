package com.liuniukeji.mixin.ui.discover;

import java.io.File;

/**
 * 群组
 */
public class GroupBean {
    /**
     * im_id  文本 	必填 	群组环信id
     * name  文本 选填 	群组名称, 新建时必传
     * description 文本 	选填 	群组简介
     * logo  文件 	选填 	群组头像, 新建时必传
     * membersonly 	文本 	选填 	加入群组是否需要群主或者群管理员审批，1 是 ， 0 否; 新建时必传
     * lng  文本 	选填 	群组经度, 新建时获取用户经纬度
     * lat 文本 	选填 	群组纬度, 新建时获取用户经纬度
     * password 	文本 	选填 	密码
     * is_pwd 	文本 	选填 	是否开启群密码: 0不开启 1开启; 新建时必传
     * reminder 	文本 	选填 	是否屏蔽群消息: 1不屏蔽 2屏蔽 ;
     *
     *  changeType 修改的类型： 1群头像 2群名称 3群简介 4 群密码 5屏蔽群消息 6群审批
     */

    private String im_id;
    private String name;
    private String description;
    private File logo;
    private String membersonly;
    private String lng;
    private String lat;
    private String password;
    private String is_pwd;
    private String reminder;
    private int changeType;


    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
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

    public File getLogo() {
        return logo;
    }

    public void setLogo(File logo) {
        this.logo = logo;
    }

    public String getMembersonly() {
        return membersonly;
    }

    public void setMembersonly(String membersonly) {
        this.membersonly = membersonly;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIs_pwd() {
        return is_pwd;
    }

    public void setIs_pwd(String is_pwd) {
        this.is_pwd = is_pwd;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }
}
