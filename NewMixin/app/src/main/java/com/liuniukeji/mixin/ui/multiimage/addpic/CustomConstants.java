package com.liuniukeji.mixin.ui.multiimage.addpic;

/**
 * Created by llt on 2017/4/5.
 * 关于图片，定义的静态字符串
 */

public class CustomConstants {
    public static final String APPLICATION_NAME = "myApp";
    //单次最多发送图片数
    public static int MAX_IMAGE_SIZE = 6;
    //首选项:临时图片
    public static final String PREF_TEMP_IMAGES = "pref_temp_images";
    //相册中图片对象集合
    public static final String EXTRA_IMAGE_LIST = "image_list";
    //相册名称
    public static final String EXTRA_BUCKET_NAME = "buck_name";
    //可添加的图片数量
    public static final String EXTRA_CAN_ADD_IMAGE_SIZE = "can_add_image_size";
    //当前选择的照片位置
    public static final String EXTRA_CURRENT_IMG_POSITION = "current_img_position";
}
