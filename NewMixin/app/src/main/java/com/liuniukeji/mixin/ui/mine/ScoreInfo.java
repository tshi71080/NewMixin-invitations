package com.liuniukeji.mixin.ui.mine;

/**
 * 学分信息
 */
public class ScoreInfo {

    /**
     * "id":"27", //类型：String  必有字段  备注：学分列表id
     * "points":"300",//类型：String  必有字段  备注：学分数量
     * "money":"30.00",//类型：String  必有字段  备注：价格
     * "introduce":"30元购买300学分",//类型：String  必有字段  备注：说明
     * "disabled":"0",//类型：String  必有字段  备注：0正常 -1删除
     * "sort":"0"//类型：String  必有字段  备注：排序
     */

    private String id;
    private String points;
    private String money;
    private String introduce;
    private String disabled;
    private String sort;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
