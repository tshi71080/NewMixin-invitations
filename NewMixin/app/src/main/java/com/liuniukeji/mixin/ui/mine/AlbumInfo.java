package com.liuniukeji.mixin.ui.mine;

/**
 * 相册信息
 */
public class AlbumInfo {

    /**
     * "photo_path":"url",//类型：String  必有字段  备注：图片地址
     * "type":"1",//类型：String  必有字段  备注：动态类型: 1,图文；2，视频；3，文章
     * "video_path":"url",//类型：String  必有字段  备注：视频地址
     * "photo_path_thumb":"img url",//类型：String  必有字段  备注：图片缩略图地址
     * "video_path_thumb":"img url" //类型：String  必有字段  备注：视频缩略图地址
     */

    private String photo_path;
    private String type;
    private String video_path;
    private String photo_path_thumb;
    private String video_path_thumb;

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

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getPhoto_path_thumb() {
        return photo_path_thumb;
    }

    public void setPhoto_path_thumb(String photo_path_thumb) {
        this.photo_path_thumb = photo_path_thumb;
    }

    public String getVideo_path_thumb() {
        return video_path_thumb;
    }

    public void setVideo_path_thumb(String video_path_thumb) {
        this.video_path_thumb = video_path_thumb;
    }
}
