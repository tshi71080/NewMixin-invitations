package com.liuniukeji.mixin.ui.discover;

/**
 *校友录院系人员
 */
public class AlumnMember {

    /**
     * "id":"215",//类型：String  必有字段  备注：我的id
     * "real_name":"颜如玉", //类型：String  必有字段  备注：我的真实姓名
     * "photo_path":"mock", //类型：String  必有字段  备注：我的头像地址
     * "signature":"mock",  //类型：String  必有字段  备注：我的个性签名
     * "im_name":"mock", //类型：String  必有字段  备注：我的通信账号
     */

    private String id;
    private String real_name;
    private String photo_path;
    private String signature;
    private String im_name;


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
}
