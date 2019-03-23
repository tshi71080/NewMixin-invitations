package com.liuniukeji.mixin.ui.home;

import java.util.List;

/**
 * 动态详情
 */
public class MomentDetail {

    /**
     * 动态详情
     */
    private Moment moments;


    static class Moment {

        /**
         * "id":"10", //类型：String  必有字段  备注：无
         * "member_id":"216",//类型：String  必有字段  备注：无
         * "add_time":"1531471552",//类型：String  必有字段  备注：无
         * "status":"0", //类型：String  必有字段  备注：无
         * "forward_count":"1",//类型：String  必有字段  备注：转发数量
         * "comment_count":"5", //类型：String  必有字段  备注：评论数量
         * "like_count":"1",//类型：String  必有字段  备注：喜欢（点赞）数量
         * "type":"0",//类型：String  必有字段  备注：无
         * "tag":"0",  //类型：String  必有字段  备注：无
         * "forward_id":"0",//类型：String  必有字段  备注：无
         * "is_recommend":"0", //类型：String  必有字段  备注：无
         * "lng":"mock",//类型：String  必有字段  备注：无
         * "lat":"mock", //类型：String  必有字段  备注：无
         * "school_id":"1001",//类型：String  必有字段  备注：无
         * "video_path":"mock", //类型：String  必有字段  备注：无
         * "title":"mock", //类型：String  必有字段  备注：无
         * "content":"今天的天真是太热了",//类型：String  必有字段  备注：无
         * "moments_photo": + [0] //类型：Array  必有字段  备注：无
         * "is_like":"1", //类型：String  必有字段  备注：无
         * "is_focus":"0", //类型：String  必有字段  备注：无
         * "color":"#000000", //类型：String  必有字段  备注：无
         * "real_name":"嘟嘟",   //类型：String  必有字段  备注：无
         * "photo_path":" img url",   //类型：String  必有字段  备注：无
         * "im_name":"201807120941_216",     //类型：String  必有字段  备注：无
         * "sex":"1",   //类型：String  必有字段  备注：无
         * "vip_type":"0",  //类型：String  必有字段  备注：无
         * "experience":"120",         //类型：String  必有字段  备注：无
         * "school_department_id":"2",   //类型：String  必有字段  备注：无
         * "school_class":"2008",    //类型：String  必有字段  备注：无
         * "school_name":"清华大学",   //类型：String  必有字段  备注：无
         * "school_department_name":"五道口金融学院",    //类型：String  必有字段  备注：无
         * "time_ago":"2天前"   //类型：String  必有字段  备注：无
         * video_cover_path  //类型：String  必有字段  备注：视频封面缩略图
         */

        private String id;
        private String member_id;
        private String add_time;
        private String status;
        private String forward_count;
        private String comment_count;
        private String like_count;
        private String type;
        private String tag;
        private String forward_id;
        private String is_recommend;
        private String lng;
        private String lat;
        private String school_id;
        private String video_path;
        private String title;
        private String content;

        private List<MomentPhoto> moments_photo;

        private String is_like;
        private String is_focus;
        private String color;
        private String real_name;
        private String photo_path;
        private String im_name;
        private String sex;
        private String vip_type;
        private String experience;
        private String school_department_id;
        private String school_class;
        private String school_name;
        private String school_department_name;
        private String time_ago;
        private String video_cover_path;


        static class MomentPhoto {
            /**
             * "thumb":"http://adiyun.my/ url", //类型：String  必有字段  备注：图片缩略图
             * "origin":"http://adiyun.my url" //类型：String  必有字段  备注：图片原图
             */
            private String thumb;
            private String origin;

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
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

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getForward_count() {
            return forward_count;
        }

        public void setForward_count(String forward_count) {
            this.forward_count = forward_count;
        }

        public String getComment_count() {
            return comment_count;
        }

        public void setComment_count(String comment_count) {
            this.comment_count = comment_count;
        }

        public String getLike_count() {
            return like_count;
        }

        public void setLike_count(String like_count) {
            this.like_count = like_count;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getForward_id() {
            return forward_id;
        }

        public void setForward_id(String forward_id) {
            this.forward_id = forward_id;
        }

        public String getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(String is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getSchool_id() {
            return school_id;
        }

        public void setSchool_id(String school_id) {
            this.school_id = school_id;
        }

        public String getVideo_path() {
            return video_path;
        }

        public void setVideo_path(String video_path) {
            this.video_path = video_path;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<MomentPhoto> getMoments_photo() {
            return moments_photo;
        }

        public void setMoments_photo(List<MomentPhoto> moments_photo) {
            this.moments_photo = moments_photo;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public String getIs_focus() {
            return is_focus;
        }

        public void setIs_focus(String is_focus) {
            this.is_focus = is_focus;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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

        public String getIm_name() {
            return im_name;
        }

        public void setIm_name(String im_name) {
            this.im_name = im_name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getVip_type() {
            return vip_type;
        }

        public void setVip_type(String vip_type) {
            this.vip_type = vip_type;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getSchool_department_id() {
            return school_department_id;
        }

        public void setSchool_department_id(String school_department_id) {
            this.school_department_id = school_department_id;
        }

        public String getSchool_class() {
            return school_class;
        }

        public void setSchool_class(String school_class) {
            this.school_class = school_class;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public String getSchool_department_name() {
            return school_department_name;
        }

        public void setSchool_department_name(String school_department_name) {
            this.school_department_name = school_department_name;
        }

        public String getTime_ago() {
            return time_ago;
        }

        public void setTime_ago(String time_ago) {
            this.time_ago = time_ago;
        }

        public String getVideo_cover_path() {
            return video_cover_path;
        }

        public void setVideo_cover_path(String video_cover_path) {
            this.video_cover_path = video_cover_path;
        }
    }

    /**
     * 点赞列表
     */
    private List<MomentLike> momentsLike;

    static class MomentLike {

        /**
         * "id":"3",   //类型：String  必有字段  备注：点赞列表id
         * "member_id":"215",     //类型：String  必有字段  备注：点赞人id
         * "photo_path":"mock"   //类型：String  必有字段  备注：点赞人头像
         */
        private String id;
        private String member_id;
        private String photo_path;

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

        public String getPhoto_path() {
            return photo_path;
        }

        public void setPhoto_path(String photo_path) {
            this.photo_path = photo_path;
        }
    }

    /**
     * 评论列表
     */
    private List<MomentComment> momentsComment;


    static class MomentComment {
        /**
         * "id":"1", //类型：String  必有字段  备注：评论列表id
         * "add_time":"1531703257", //类型：String  必有字段  备注：评论时间
         * "content":"",//类型：String  必有字段  备注：评论内容
         * "member_id":"215", //类型：String  必有字段  备注：评论人id
         * "real_name":"颜如玉",//类型：String  必有字段  备注：评论人真实姓名
         * "photo_path":"mock",  //类型：String  必有字段  备注：评论人头像地址
         * "sex":"2", //类型：String  必有字段  备注：评论人性别: 1 男 2 女
         * "experience":"150", //类型：String  必有字段  备注：评论人等级
         * "school_class":"2008", //类型：String  必有字段  备注：评论人年级
         * "school_department_name":"五道口金融学院",  //类型：String  必有字段  备注：评论人院系
         * "color":"#000000", //类型：String  必有字段  备注：评论人昵称颜色(会员显示, 非会员默认黑色)
         * "to_real_name":"mock",//类型：String  必有字段  备注：被回复人真实姓名
         * "vip_type":"0" /类型：String  必有字段  备注：0,普通用户；1，VIP
         */

        private String id;
        private String add_time;
        private String content;
        private String member_id;
        private String real_name;
        private String photo_path;
        private String sex;
        private String experience;
        private String school_class;
        private String school_department_name;
        private String color;
        private String to_real_name;
        private String vip_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getSchool_class() {
            return school_class;
        }

        public void setSchool_class(String school_class) {
            this.school_class = school_class;
        }

        public String getSchool_department_name() {
            return school_department_name;
        }

        public void setSchool_department_name(String school_department_name) {
            this.school_department_name = school_department_name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getTo_real_name() {
            return to_real_name;
        }

        public void setTo_real_name(String to_real_name) {
            this.to_real_name = to_real_name;
        }

        public String getVip_type() {
            return vip_type;
        }

        public void setVip_type(String vip_type) {
            this.vip_type = vip_type;
        }
    }


//---------------------------------------大类变量-------------------------------------------

    public Moment getMoments() {
        return moments;
    }

    public void setMoments(Moment moments) {
        this.moments = moments;
    }

    public List<MomentLike> getMomentsLike() {
        return momentsLike;
    }

    public void setMomentsLike(List<MomentLike> momentsLike) {
        this.momentsLike = momentsLike;
    }


    public List<MomentComment> getMomentsComment() {
        return momentsComment;
    }

    public void setMomentsComment(List<MomentComment> momentsComment) {
        this.momentsComment = momentsComment;
    }

//---------------------------------------大类变量-------------------------------------------


}
