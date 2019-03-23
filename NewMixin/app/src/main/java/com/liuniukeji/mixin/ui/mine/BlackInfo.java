package com.liuniukeji.mixin.ui.mine;

/**
 * 黑名单信息
 */
public class BlackInfo {

    /**
     * "id":"7", //类型：String  必有字段  备注：黑名单id
     * "to_member_id":"100", //类型：String  必有字段  备注：对方id
     * "real_name":"mock",//类型：String  必有字段  备注：对方真实姓名
     * "photo_path":"image url ",//类型：String  必有字段  备注：对方头像地址
     * "im_name":"mock" //类型：String  必有字段  备注：对方环信id
     */

    private String id;
    private String to_member_id;
    private String real_name;
    private String photo_path;
    private String im_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo_member_id() {
        return to_member_id;
    }

    public void setTo_member_id(String to_member_id) {
        this.to_member_id = to_member_id;
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
}
