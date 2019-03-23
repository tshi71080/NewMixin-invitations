package com.liuniukeji.mixin.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.liuniukeji.mixin.util.currency.ToastUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangyf on 2016/10/24.
 */
public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final String FOLDER = "pinyou";
    private static final String FOLDER_SEPARATOR = "/";
    private static final char EXTENSION_SEPARATOR = '.';

    static public String SDPATH = getSDPath();

    static public String getSDPath() {
        File sdDir = null;
        File imageDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
            String path = sdDir.getPath() + File.separator + FOLDER + File.separator;
            imageDir = new File(path);
            if (!imageDir.exists()) {
                // 若不存在，创建目录，可以在应用启动的时候创建
                imageDir.mkdirs();
            }
        }
        if (imageDir == null){

            return null;
        }
        return sdDir.getPath() + File.separator + FOLDER + File.separator;
    }

    static public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        if (file.exists()){
            return true;
        }
        deleteExistFile(fileName);
        return false;
    }

    public static void deleteExistFile(String fileName){
        File file = new File(SDPATH + fileName);
        if (file.isDirectory()){

            for (File element : file.listFiles()) {
                if (element.isFile())
                    element.delete(); // 删除所有文件
            }
        }else{
            file.delete();
        }
    }

    public static String getFileName(String id, String attem_path){
        if (null != attem_path && !"".equals(attem_path)){

            return id + "_"+attem_path.substring(attem_path.lastIndexOf("/") + 1);
        }
        return "";
    }

    public static List<String> getDownedDataList(List<String> pathList, String path) {

        if (null == pathList){
            pathList = new ArrayList<>();

        }
        try {

            File dirFile = new File(path);
            //如果dir对应的文件不存在，或者不是一个目录，则退出
            if (!dirFile.exists() || !dirFile.isDirectory()) {
                return pathList;
            }
            //删除文件夹下的所有文件(包括子目录)
            File[] files = dirFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    pathList.add(files[i].getAbsolutePath());
                }else {
                    getDownedDataList(pathList,files[i].getPath());
                }
            }
            return pathList;

        }catch (Exception e){
            Log.e("filepath","file--list---Exception--e-->>"+e);
        }

        return pathList;

    }
    static public String saveBitmapPath(Bitmap bm) {
        getSDPath();
        String ranDom= String.valueOf( (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String picName = formatter.format(curDate)+ranDom;
        if (bm == null || TextUtils.isEmpty(picName))
            return "";
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            File f = new File(SDPATH, picName + ".jpg");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return SDPATH + picName + ".jpg";
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    public static void saveBitmap(Context context,Bitmap bm) {
        getSDPath();
        String ranDom= String.valueOf( (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String picName = formatter.format(curDate)+ranDom;
        if (bm == null || TextUtils.isEmpty(picName))
            return;
        try {
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }
            String fileName = picName + ".jpg";
            File f = new File(SDPATH, fileName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            ToastUtils.showLongToast("保存图片成功");

            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        f.getAbsolutePath(), fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新                                                             /storage/emulated/0/pinyou/201901183649.jpg
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(f.getAbsolutePath())));
            Log.e("UUU",Uri.parse(f.getAbsolutePath()).getPath());
        } catch (FileNotFoundException e) {
            ToastUtils.showLongToast("保存图片失败");
        } catch (IOException e) {
            ToastUtils.showLongToast("保存图片失败");
        }


    }


    static public File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

        }
        return dir;
    }

    public static String readAssertResource(Context context, String strAssertFileName) {
        AssetManager assetManager = context.getAssets();
        String strResponse = "";
        try {
            InputStream ims = assetManager.open(strAssertFileName);
            strResponse = getStringFromInputStream(ims);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    private static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(a_is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }
}
