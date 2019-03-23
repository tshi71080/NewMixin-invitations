package com.liuniukeji.mixin.ui.message;

/**
 * 分组信息
 */
public class GroupInfo {

    /**
     * "id":"0", //类型：String  必有字段  备注：群组id,"未分组"id为0
     * "name":"未分组",//类型：String  必有字段  备注：群组名称
     * "color":"#000000"  //类型：String  必有字段  备注：群组颜色
     */

    private String id;
    private String name;
    private String color;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
