package com.liuniukeji.mixin.ui.login;

import java.io.Serializable;

/**
 * 年级信息
 */
public class GradeBean implements Serializable{

    /**
     * "key":"2018",//类型：String  必有字段  备注：键值
     * "value":"2018年"//类型：String  必有字段  备注：名称
     **/

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}