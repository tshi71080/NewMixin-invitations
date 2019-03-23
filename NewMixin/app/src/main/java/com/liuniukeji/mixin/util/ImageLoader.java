package com.liuniukeji.mixin.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.net.UrlUtils;

/**
 *
 */

public class ImageLoader {

    public static void loadHead(final Context context, ImageView view, String url) {
        try {
            if (!TextUtils.isEmpty(url) && !url.contains("http"))
                url = UrlUtils.APIHTTP + url;
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.mipmap.default_avatar)
                    .error(R.mipmap.default_avatar)
                    .into(new BitmapImageViewTarget(view) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            view.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, ImageView view, String url) {
        try {
            if (!TextUtils.isEmpty(url) && !url.contains("http"))
                url = UrlUtils.APIHTTP + url;
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadCode(Context context, ImageView view, String url) {
        try {
            if (!TextUtils.isEmpty(url) && !url.contains("http"))
                url = UrlUtils.APIHTTP + url;
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadImageRoundConer(Context context, final ImageView view, String url) {
        try {
            if (!TextUtils.isEmpty(url) && !url.contains("http"))
                url = UrlUtils.APIHTTP + url;
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            view.setImageDrawable(resource);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadHeadLocal(final Context context, ImageView view, String url) {
        try {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.default_avatar)
                    .error(R.mipmap.default_avatar)
                    .into(new BitmapImageViewTarget(view) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            view.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageLocal(final Context context, ImageView view, String url) {
        try {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.default_image)
                    .error(R.mipmap.default_image)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
