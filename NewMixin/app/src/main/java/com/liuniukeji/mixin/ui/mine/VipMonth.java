package com.liuniukeji.mixin.ui.mine;

/**
 * 用户实名认证信息
 */
public class VipMonth {

    /**
     * "id":"3",//类型：String  必有字段  备注：会员列表id
     * "name":"1个月",//类型：String  必有字段  备注：会员名称
     * "day":"30", //类型：String  必有字段  备注：天数
     * "price":"0.00",//类型：String  必有字段  备注：无
     * "points":"20",//类型：String  必有字段  备注：学分价格
     * "sort":"0" //类型：String  必有字段  备注：排序
     */

    private String id;
    private String name;
    private String day;
    private String price;
    private String points;
    private String sort;


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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
