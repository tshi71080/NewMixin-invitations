package com.liuniukeji.mixin.ui.login;

import java.io.File;
import java.io.Serializable;

/**
 * 注册所需信息
 */
public class RegisterInfo implements Serializable {

    /**
     * "school_id":"1001", //类型：String  必有字段  备注：学校id
     * "school_department_id":"",//类型：String  必有字段  备注：院系id
     * "school_class":"" //类型：String  必有字段  备注：年级键值
     * "real_name":"" //类型：String  必有字段  备注：真实姓名
     * "sex":"" //类型：String  必有字段  备注：性别: 1男 2女
     * "phone":"" //类型：String  必有字段  备注：手机号
     * "code":"" //类型：String  必有字段  备注：验证码
     * "password":"" //类型：String  必有字段  备注：密码
     * "invite_code":"" //类型：String  可选  备注：邀请码
     * "lng":"" //类型：String  可选  备注：经度
     * "lat":"" //类型：String  可选  备注：纬度
     * photo_path //文件   用户头像
     **/

    private String school_id;
    private String school_department_id;
    private String school_class;
    private String real_name;
    private String sex;
    private String phone;
    private String code;
    private String password;
    private String invite_code;
    private String lng;
    private String lat;
    private File photo_path;
    private String area_code;


    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_department_id() {
        return school_department_id;
    }

    public void setSchool_department_id(String school_department_id) {
        this.school_department_id = school_department_id;
    }

    public String getSchool_class() {
        return school_class;
    }

    public void setSchool_class(String school_class) {
        this.school_class = school_class;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
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

    public File getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(File photo_path) {
        this.photo_path = photo_path;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }
}