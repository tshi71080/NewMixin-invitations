package com.liuniukeji.mixin.ui.discover;

/**
 * 校友录定位到我
 */
public class AlumnMe {

    /**
     * "id":"215",//类型：String  必有字段  备注：我的id
     * "real_name":"颜如玉", //类型：String  必有字段  备注：我的真实姓名
     * "photo_path":"mock", //类型：String  必有字段  备注：我的头像地址
     * "signature":"mock",  //类型：String  必有字段  备注：我的个性签名
     * "im_name":"mock", //类型：String  必有字段  备注：我的通信账号
     * "school_department_id":"2",//类型：String  必有字段  备注：我的院系id
     * "school_class":"2008"//类型：String  必有字段  备注：我的年级键值
     */

    private String id;
    private String real_name;
    private String photo_path;
    private String signature;
    private String im_name;
    private String school_department_id;
    private String school_class;

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIm_name() {
        return im_name;
    }

    public void setIm_name(String im_name) {
        this.im_name = im_name;
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
}
