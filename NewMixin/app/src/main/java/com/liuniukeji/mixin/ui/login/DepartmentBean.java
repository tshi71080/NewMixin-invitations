package com.liuniukeji.mixin.ui.login;

import java.io.Serializable;

/**
 * 学院信息
 */
public class DepartmentBean implements Serializable{

    /**
     * "id":"2501",//类型：String  必有字段  备注：院系id
     * "school_id":"2011",//类型：String  必有字段  备注：学校id
     * "name":"法学院"//类型：String  必有字段  备注：院系名称
     **/

    private String id;
    private String school_id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}