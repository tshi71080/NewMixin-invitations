package com.liuniukeji.mixin.ui.discover;

/**
 * 新建群组返回的结果数据
 */
public class GroupBackBean {
    /**
     * im_id  文本 	必填 	群组环信id
     * name  文本 选填 	群组名称, 新建时必传
     * description 文本 	选填 	群组简介
     * logo  文本 	选填 	群组头像, 新建时必传
     * membersonly 	文本 	选填 	加入群组是否需要群主或者群管理员审批，1 是 ， 0 否; 新建时必传
     * lng  文本 	选填 	群组经度, 新建时获取用户经纬度
     * lat 文本 	选填 	群组纬度, 新建时获取用户经纬度
     * password 	文本 	选填 	密码
     * is_pwd 	文本 	选填 	是否开启群密码: 0不开启 1开启; 新建时必传
     * reminder 	文本 	选填 	是否屏蔽群消息: 1不屏蔽 2屏蔽 ;
     *
     *  extension 是否群拓展信息更新
     *  change_type  1群头像 2群名称
     */

    private String im_id;
    private String name;
    private String description;
    private String logo;
    private String membersonly;
    private String lng;
    private String lat;
    private String password;
    private String is_pwd;
    private String reminder;

    private boolean extension;
    private int change_type;


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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
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

    public boolean isExtension() {
        return extension;
    }

    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    public int getChange_type() {
        return change_type;
    }

    public void setChange_type(int change_type) {
        this.change_type = change_type;
    }
}
