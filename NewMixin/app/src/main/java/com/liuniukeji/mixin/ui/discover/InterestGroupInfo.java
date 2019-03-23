package com.liuniukeji.mixin.ui.discover;

/**
 * 兴趣组
 */
public class InterestGroupInfo {

    /**
     * id":"1", //类型：String  必有字段  备注：群组id
     * "name":"嘟嘟嘟啦啦啦",//类型：String  必有字段  备注：群组名称
     * "logo":"img url",//类型：String  必有字段  备注：群组头像
     * "description":"mock", //类型：String  必有字段  备注：群组简介
     * "im_id":"20180706_1453" //类型：String  必有字段  备注：群组环信ID
     */

    private String id;
    private String name;
    private String logo;
    private String description;
    private String im_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }
}
