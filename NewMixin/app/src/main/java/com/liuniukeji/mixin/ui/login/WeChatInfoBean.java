package com.liuniukeji.mixin.ui.login;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description
 * @Author (LiShiyang / 845719506 @ qq.com)
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date ${Date}
 * @CreateBy Android Studio
 */
public class WeChatInfoBean {
    private int errCode;

    private String openid;

    private int sex;

    private String nickname;

    private String headimgurl;

    private String province;

    private String language;

    private String country;

    private String unionid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getSex() {
        return (sex == 0) ? "男" : "女";
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "WeChatInfo{" +
                "errCode='" + errCode + '\'' +
                ", openid='" + openid + '\'' +
                ", sex=" + sex +
                ", nickname='" + nickname + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", province='" + province + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
