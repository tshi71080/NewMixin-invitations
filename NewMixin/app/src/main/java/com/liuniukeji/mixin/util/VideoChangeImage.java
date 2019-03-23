package com.liuniukeji.mixin.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;


import com.liuniukeji.mixin.util.currency.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/7/27.
 * 视频获取bitmap 图片,保存图片为文件并返回文件路径
 */
public class VideoChangeImage {
    /**
     * 获取视频文件截图 *
     * * @param path 视频文件的路径
     * * @return Bitmap 返回获取的Bitmap
     */
    public static Bitmap getVideoThumb(String path, long lengthMs) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        if (lengthMs > 3000) {
            return media.getFrameAtTime(3000000);
        } else {
            return media.getFrameAtTime();
        }
    }

    /**
     * Bitmap保存成File *
     * * @param bitmap input bitmap
     * * @param name output file's name
     * * @return String output file's path
     */
    public static String bitmap2File(Bitmap bitmap, String name) {
        FileUtils.createOrExistsDir(Constants.IMAGE_PATH);
        File f = new File(Constants.IMAGE_PATH + name + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            return null;
        }
        return f.getAbsolutePath();
    }

}
