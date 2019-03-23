package com.liuniukeji.mixin.ui.multiimage.addpic;

import java.io.Serializable;

/**
 * Created by llt on 2017/4/5.
 */
//http://blog.csdn.net/lazyer_dog/article/details/51672070   Serializable的作用
//http://blog.csdn.net/nicholas_nick/article/details/51011622  序列化和反序列化的区别
//http://blog.csdn.net/jeek_job/article/details/52333878 实现Serializable自动生成serialVersionUID

public class ImageItem implements Serializable {
    public String imageId;
    public String thumbnailPath;
    public String sourcePath;
    public boolean isSelected = false;
    private static final long serialVersionUID = -3235406399492683924L;

    private String community_image_id;
    private int user_id;
    private String community_image_url;

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getCommunity_image_url() {
        return community_image_url;
    }

    public void setCommunity_image_url(String community_image_url) {
        this.community_image_url = community_image_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCommunity_image_id() {
        return community_image_id;
    }

    public void setCommunity_image_id(String community_image_id) {
        this.community_image_id = community_image_id;
    }
}
