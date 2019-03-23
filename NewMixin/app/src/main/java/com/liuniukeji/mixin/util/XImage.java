package com.liuniukeji.mixin.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.currency.Utils;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.currency.Utils;

import org.xutils.image.ImageOptions;
import org.xutils.x;


/**
 * 图片加载工具类
 */

public class XImage {
    public static ImageOptions imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
//                .setRadius(DensityUtil.dip2px(5))
//                 如果ImageView的大小不是定义为wrap_content, 不要crop.
//                 加载中或错误图片的ScaleType
            .setFadeIn(true)
            .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
            .setImageScaleType(ImageView.ScaleType.FIT_XY)
            .setLoadingDrawableId(R.mipmap.pic_error)
            .setFailureDrawableId(R.mipmap.pic_fail)
            .build();
    public static ImageOptions imageOptions1 = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
//                .setRadius(DensityUtil.dip2px(5))
//                 如果ImageView的大小不是定义为wrap_content, 不要crop.
//                 加载中或错误图片的ScaleType
            .setFadeIn(true)
            .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            .setLoadingDrawableId(R.mipmap.pic_error)
            .setFailureDrawableId(R.mipmap.pic_fail)
            .build();

    public static void loadImageAvatar(ImageView view, String url) {
        x.image().bind(view, url, imageOptions);
    }

    public static void loadImageCircle(ImageView view, String url) {
        x.image().bind(view, url, imageOptions);
    }


    /**
     * 加载图片centerCrop()，会剪裁图片以适应宽高
     * x.image().bind(view, url, imageOptions1);
     *
     * @param view
     * @param url
     */
    public static void loadImage(ImageView view, String url) {
        Glide.with(Utils.getContext())
                .load(url)
                .asBitmap()
                .error(R.mipmap.pic_error)
                .placeholder(R.mipmap.pic_fail).centerCrop()
                .dontAnimate()
                //.placeholder(R.drawable.image_default).centerCrop()
                .into(view);
    }


    /**
     * 当图片大于布局时，缩放图片以适应布局，不剪裁
     * x.image().bind(view, url, imageOptions1);
     *
     * @param view
     * @param url
     */
    public static void loadImageWithfitCenter(ImageView view, String url) {
        Glide.with(Utils.getContext())
                .load(url)
                .asBitmap()
                .error(R.mipmap.pic_error)
                .placeholder(R.mipmap.pic_fail).fitCenter()
                .dontAnimate()
                //.placeholder(R.drawable.image_default).centerCrop()
                .into(view);
    }

    /**
     * 加载头像
     * x.image().bind(view, url, imageOptions1);
     *
     * @param view
     * @param url
     */
    public static void loadAvatar(ImageView view, String url) {
        String url_pic;
        if (null != url) {
            if (url.startsWith("http")) {
                url_pic = url;
            } else {
                url_pic = UrlUtils.APIHTTP + url;
            }
        } else {
            return;
        }
        Glide.with(Utils.getContext())
                .load(url_pic)
                .asBitmap()
                .error(R.mipmap.signup_icon_avatar)
                .placeholder(R.mipmap.signup_icon_avatar).fitCenter()
                .dontAnimate()
                // .placeholder(R.drawable.image_default).centerCrop()
                .into(view);
    }


    /**
     * 加载封面
     *
     * @param view
     * @param url
     */
    public static void loadCover(ImageView view, String url) {
        String url_pic;
        if (null != url) {
            if (url.startsWith("http")) {
                url_pic = url;
            } else {
                url_pic = UrlUtils.APIHTTP + url;
            }
        } else {
            return;
        }
        Glide.with(Utils.getContext())
                .load(url_pic)
                .asBitmap()
                .error(R.mipmap.base_map)
                .placeholder(R.mipmap.base_map).fitCenter()
                .dontAnimate()
                // .placeholder(R.drawable.image_default).centerCrop()
                .into(view);
    }


}
