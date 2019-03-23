package com.liuniukeji.mixin.ui.discover;

import java.util.List;

/**
 * 校友录
 */
public class AlumnInfo {

    /**
     * "my_school_class":"2008",  //类型：String  必有字段  备注：我的年级
     * "my_school_name":"清华大学",  //类型：String  必有字段  备注：我的学校名称
     * "school_class": [  //类型：Array  必有字段  备注：年级列表]
     * "school_department": - [//类型：Array  必有字段  备注：学校院系列表]
     */

    private String my_school_class;
    private String my_school_name;


    private List<Grade> school_class;

    private List<Department> school_department;


    static class Grade {
        /**
         * "key":"2018", //类型：String  必有字段  备注：年级键值
         * "value":"2018年" //类型：String  必有字段  备注：年级名称
         */

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

    static class Department {
        /**
         * "id":"1",  //类型：String  必有字段  备注：院系id
         * "school_id":"1001", //类型：String  必有字段  备注：学校id
         * "name":"中国语言文学系"  //类型：String  必有字段  备注：院系名称
         */

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


    public String getMy_school_class() {
        return my_school_class;
    }

    public void setMy_school_class(String my_school_class) {
        this.my_school_class = my_school_class;
    }

    public String getMy_school_name() {
        return my_school_name;
    }

    public void setMy_school_name(String my_school_name) {
        this.my_school_name = my_school_name;
    }

    public List<Grade> getSchool_class() {
        return school_class;
    }

    public void setSchool_class(List<Grade> school_class) {
        this.school_class = school_class;
    }

    public List<Department> getSchool_department() {
        return school_department;
    }

    public void setSchool_department(List<Department> school_department) {
        this.school_department = school_department;
    }
}
