package com.liuniukeji.mixin.ui.mine;

/**
 * 我的回复信息
 */
public class ReplyInfo {

    /**
     * "id":"10",//类型：String  必有字段  备注：动态id
     * "video_path":"mock", //类型：String  必有字段  备注：无
     * "content":"#Ta的提问#今天的天真是太热了", //类型：String  必有字段  备注：动态内容
     * "type":"0",//类型：String  必有字段  备注：0纯文字，1,图文；2，视频；3，文章
     * "tag":"2",//类型：String  必有字段  备注：0,普通朋友圈，1，二手市场，2，学弟学妹提问
     * "comment_content":"咚咚咚咚",//类型：String  必有字段  备注：回复内容
     * "real_name":"我",//类型：String  必有字段  备注：回复人真实姓名
     * "add_time":"1531703535", //类型：String  必有字段  备注：无
     * "photo_path":"mixed", //类型：Mixed  必有字段  备注：无
     * "cover_path":"mock",//类型：String  必有字段  备注：动态图片(查看详情去掉后缀)
     * "m":"07月",//类型：String  必有字段  备注：回复月份
     * "d":"16",//类型：String  必有字段  备注：回复日期
     * "h":"09:12"//类型：String  必有字段  备注：回复时间
     */

    private String id;
    private String video_path;
    private String content;
    private String type;
    private String tag;
    private String comment_content;
    private String real_name;
    private String add_time;
    private String photo_path;
    private String cover_path;
    private String m;
    private String d;
    private String h;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public String getCover_path() {
        return cover_path;
    }

    public void setCover_path(String cover_path) {
        this.cover_path = cover_path;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }
}
