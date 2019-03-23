package com.liuniukeji.mixin.ui.mine.setting;

import java.io.Serializable;

/**
 * 用户实名认证信息
 */
public class AuthInfo implements Serializable {

    /**
     * "id":"216",//类型：String  必有字段  备注：用户id
     * "real_name":"嘟嘟",//类型：String  必有字段  备注：真实姓名
     * "school_id":"1001", //类型：String  必有字段  备注：学校id
     * "school_department_id":"2",//类型：String  必有字段  备注：院系id
     * "school_class":"2008", //类型：String  必有字段  备注：年级键值
     * "is_audit":"0",//类型：String  必有字段  备注：是否审核通过，0未提交，1通过,2提交审核,3审核驳回
     * "photo_front":"mock", //类型：String  必有字段  备注：学生证正面照
     * "photo_back":"mock", //类型：String  必有字段  备注：学生证反面照
     * "note":"mock",//类型：String  必有字段  备注：审核备注
     * "school_name":"清华大学",//类型：String  必有字段  备注：学校名称
     * "department_name":"五道口金融学院", //类型：String  必有字段  备注：院系名称
     * "school_class_name":"2008年"//类型：String  必有字段  备注：年级名称
     */

    private String id;
    private String real_name;
    private String school_id;
    private String school_department_id;
    private String school_class;
    private String is_audit;
    private String photo_front;
    private String photo_back;
    private String note;
    private String school_name;
    private String department_name;
    private String school_class_name;


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

    public String getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(String is_audit) {
        this.is_audit = is_audit;
    }

    public String getPhoto_front() {
        return photo_front;
    }

    public void setPhoto_front(String photo_front) {
        this.photo_front = photo_front;
    }

    public String getPhoto_back() {
        return photo_back;
    }

    public void setPhoto_back(String photo_back) {
        this.photo_back = photo_back;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getSchool_class_name() {
        return school_class_name;
    }

    public void setSchool_class_name(String school_class_name) {
        this.school_class_name = school_class_name;
    }


}
