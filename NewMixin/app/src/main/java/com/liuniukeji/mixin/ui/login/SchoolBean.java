package com.liuniukeji.mixin.ui.login;

import java.io.Serializable;

/**
 * 学校信息
 */
public class SchoolBean implements Serializable{

    /**
     * "id":"1001", //类型：String  必有字段  备注：学校id
     * "province":"北京",//类型：String  必有字段  备注：省份
     * "name":"清华大学" //类型：String  必有字段  备注：学校名称
     **/

    private String id;
    private String province;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}