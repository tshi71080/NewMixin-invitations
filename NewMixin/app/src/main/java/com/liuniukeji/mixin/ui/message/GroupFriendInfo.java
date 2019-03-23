package com.liuniukeji.mixin.ui.message;

import java.util.List;

/**
 * 分组及组下好友信息
 */
public class GroupFriendInfo {

    /**
     * "id":"0", //类型：String  必有字段  备注：群组id,"未分组"id为0
     * "name":"未分组",//类型：String  必有字段  备注：群组名称
     * "color":"#000000"  //类型：String  必有字段  备注：群组颜色
     * follow_list:   //类型：Array  必有字段  备注：分组下好友
     */

    private String id;
    private String name;
    private String color;
    private List<Follow> follow_list;

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

    public List<Follow> getFollow_list() {
        return follow_list;
    }

    public void setFollow_list(List<Follow> follow_list) {
        this.follow_list = follow_list;
    }

    static class Follow{
        /**
         *"id":"215",//类型：String  必有字段  备注：关注者id
         "real_name":"颜如玉", //类型：String  必有字段  备注：真实姓名
         "photo_path":"mock", //类型：String  必有字段  备注：头像地址
         "type":"2",//类型：String  必有字段  备注：1管理员、2用户，3老师
         "signature":"mock", //类型：String  必有字段  备注：个性签名
         "im_name":"f9624ddb48d030dd0e0d95752b215e51", //类型：String  必有字段  备注：环信id
         "is_quietly":"0", //类型：String  必有字段  备注：是否悄悄关注: 0不是 1是
         "isfriend":"0" //类型：String  必有字段  备注：是否互为关注:0不是 1是
         */

        private String id;
        private String real_name;
        private String photo_path;
        private String type;
        private String signature;
        private String im_name;
        private String is_quietly;
        private String isfriend;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getIm_name() {
            return im_name;
        }

        public void setIm_name(String im_name) {
            this.im_name = im_name;
        }

        public String getIs_quietly() {
            return is_quietly;
        }

        public void setIs_quietly(String is_quietly) {
            this.is_quietly = is_quietly;
        }

        public String getIsfriend() {
            return isfriend;
        }

        public void setIsfriend(String isfriend) {
            this.isfriend = isfriend;
        }
    }

}
