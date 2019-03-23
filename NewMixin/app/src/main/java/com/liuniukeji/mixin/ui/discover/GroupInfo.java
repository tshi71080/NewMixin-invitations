package com.liuniukeji.mixin.ui.discover;

import java.util.List;

/**
 * 群组资料详情
 */
public class GroupInfo {

    /**
     * "id":"1", //类型：String  必有字段  备注：群组id
     * "member_id":"216", //类型：String  必有字段  备注：群主id
     * "real_name":"嘟嘟",  //类型：String  必有字段  备注：群主真实姓名
     * "photo_path":"img url",   //类型：String  必有字段  备注：群主头像
     * "name":"嘟啦啦",//类型：String  必有字段  备注：群组名称
     * "description":"mock", //类型：String  必有字段  备注：群组简介
     * "disabled":"0",  //类型：String  必有字段  备注：0 正常显示 1删除
     * "logo":"img url",  //类型：String  必有字段  备注：群组头像
     * "im_id":"20180706_1453",  //类型：String  必有字段  备注：群组环信id
     * "membersonly":"1", //类型：String  必有字段  备注：加入群组是否需要群主或者群管理员审批，1 是 ， 0 否
     * "is_pwd":"1", //类型：String  必有字段  备注：是否开启群密码: 0不开启 1开启
     * "family":"1", //类型：String  必有字段  备注：群角色: -1 不是群成员 0群成员 1群主
     * "reminder":"1", //类型：String  必有字段  备注：是否屏蔽群消息 1不屏蔽 2屏蔽
     * "member_list": - [  //类型：Array  必有字段  备注：群成员列表
     * + {...}    //类型：Object  必有字段  备注：无
     *]
     * "set_password":0, //类型：Number  必有字段  备注：是否设置群密码: 0未设置 1已设置
     */

    private String id;
    private String member_id;
    private String real_name;
    private String photo_path;

    private String name;
    private String description;
    private String disabled;
    private String logo;
    private String im_id;
    private String membersonly;
    private String is_pwd;
    private String family;
    private String reminder;
    private String set_password;

    private List<Member> member_list;

    public String getSet_password() {
        return set_password;
    }

    public void setSet_password(String set_password) {
        this.set_password = set_password;
    }

    /**
     * 群成员
     */
    static class Member {
        /**
         * "group_im_id":"20180706_1453", //类型：String  必有字段  备注：群组环信id
         * "member_im_name":"mock",//类型：String  必有字段  备注：群成员环信id
         *  "type":"1",//类型：String  必有字段  备注：群内角色，0普通用户，1群主，2管理员
         * "real_name":"嘟嘟",//类型：String  必有字段  备注：群成员真实姓名
         * "photo_path":"img url",  //类型：String  必有字段  备注：群成员头像
         *  "sex":"1",//类型：String  必有字段  备注：群成员性别:0 保密 1 男 2 女
         *  "experience":"80", //类型：String  必有字段  备注：群成员经验值
         *  "signature":"哈哈哈"//类型：String  必有字段  备注：群成员个性签名
         */

        private String group_im_id;
        private String member_im_name;
        private String type;
        private String real_name;
        private String photo_path;
        private String sex;
        private String experience;
        private String signature;


        public String getGroup_im_id() {
            return group_im_id;
        }

        public void setGroup_im_id(String group_im_id) {
            this.group_im_id = group_im_id;
        }

        public String getMember_im_name() {
            return member_im_name;
        }

        public void setMember_im_name(String member_im_name) {
            this.member_im_name = member_im_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getPhoto_path() {
            return photo_path;
        }

        public void setPhoto_path(String photo_path) {
            this.photo_path = photo_path;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisabled() {
        return disabled;
    }

    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIm_id() {
        return im_id;
    }

    public void setIm_id(String im_id) {
        this.im_id = im_id;
    }

    public String getMembersonly() {
        return membersonly;
    }

    public void setMembersonly(String membersonly) {
        this.membersonly = membersonly;
    }

    public String getIs_pwd() {
        return is_pwd;
    }

    public void setIs_pwd(String is_pwd) {
        this.is_pwd = is_pwd;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public List<Member> getMember_list() {
        return member_list;
    }

    public void setMember_list(List<Member> member_list) {
        this.member_list = member_list;
    }
}
