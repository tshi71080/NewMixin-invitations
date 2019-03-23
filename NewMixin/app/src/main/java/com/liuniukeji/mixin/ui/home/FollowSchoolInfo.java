package com.liuniukeji.mixin.ui.home;

/**
 * 关注学校信息
 */
public class FollowSchoolInfo {

    /**
     * "id":"1002",//类型：String  必有字段  备注：学校id
     * "name":"北京大学"//类型：String  必有字段  备注：学校名称
     */

    private String id;
    private String name;

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



}
