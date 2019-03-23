package com.liuniukeji.mixin.ui.mine.baseinfo;

/**
 * 用户常用信息
 */
public class CommonInfo {

    /**
     * "token":"111111", //类型：String  必有字段  备注：用户token
     * "signature":"哈哈哈",//类型：String  必有字段  备注：个性签名
     * "birthday":true, //类型：Boolean  必有字段  备注：生日
     * "is_show_birthday":"0",//类型：String  必有字段  备注：是否展示生日0不展示1展示
     * "address":"山东省临沂市",//类型：String  必有字段  备注：地址
     * "interests":"唱歌 跳舞", //类型：String  必有字段  备注：兴趣爱好以空格隔开
     * "id":"216" //类型：String  必有字段  备注：用户id
     */

    private String token;
    private String signature;
    private String birthday;
    private String is_show_birthday;
    private String address;
    private String interests;
    private String id;
    private String real_name;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

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
}
