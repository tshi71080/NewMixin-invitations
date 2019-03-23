package com.liuniukeji.mixin.ui.multiimage.lookbigimg;

import java.io.Serializable;

/**
 * Created by WRJ on 2016/10/31.
 */
public class PhotoEntity implements Serializable {

    /**需求详情和朋友圈世界圈详情   img url
     * img_url : /Uploads/Picture/2016_11_15/20161115_174523_14792031232951_5406.jpg
     */
    private String img_url;
    /**
     * 需求详情  img url
     * id : 51
     */
    private String id;
    public int bendi=0;
    /**
     * 修改个人主页背景
     * @return
     */
    private String background_image;
    /**用户修改头像
     * user_logo : http://yuepin.host3.liuniukeji.com/Uploads/75/logo/20170218_143941_14873999818267_8769.jpg
     * user_logo_thumb : http://yuepin.host3.liuniukeji.com/Uploads/75/logo/20170218_143941_14873999818267_8769_160_160.jpg
     */

    private String user_logo;
    private String user_logo_thumb;
    private String user_cover;

    public void setUser_cover(String user_cover) {
        this.user_cover = user_cover;
    }

    public String getUser_cover() {
        return user_cover;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUser_logo() {
        return user_logo;
    }

    public void setUser_logo(String user_logo) {
        this.user_logo = user_logo;
    }

    public String getUser_logo_thumb() {
        return user_logo_thumb;
    }

    public void setUser_logo_thumb(String user_logo_thumb) {
        this.user_logo_thumb = user_logo_thumb;
    }
}
