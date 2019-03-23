package com.liuniukeji.mixin.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

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
public class FileUtils {
    private static final String TAG = "FileUtil";
    private static final String FOLDER = "HiMouth";
    private static final String IMG = "img";
    private static final String FOLDER_SEPARATOR = "/";
    private static final char EXTENSION_SEPARATOR = '.';
    static public String SDPATH = getSDPath();
    static public String SDPATHIMG = getSDPathImg();
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
    static public String getSDPathImg() {
        File sdDir = null;
        File imageDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取根目录
            String path = sdDir.getPath()  + File.separator+IMG+File.separator;
            imageDir = new File(path);
            if (!imageDir.exists()) {
                // 若不存在，创建目录，可以在应用启动的时候创建
                imageDir.mkdirs();
            }
        }
        if (imageDir == null){

            return null;
        }
        return sdDir.getPath() +  File.separator+IMG+File.separator;
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
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    //判断文件是否存在
    public static boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
    public static void deleteDirWihtFile(File dir) {

        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
//            else if (file.isDirectory())
//                deleteDirWihtFile(file); // 递规的方式删除文件夹

        }
//        dir.delete();// 删除目录本身
    }
    public static void deleteExistFile(String fileName){
        File file = new File(SDPATH);
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
    /**
     * 读取本地图片
     * @param path 图片路径
     * */
    public static Bitmap getDiskBitmap(String path) {
        Bitmap bitmap = null;
        if(TextUtils.isEmpty(path)) {
            return bitmap;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                bitmap = BitmapFactory.decodeFile(path, opt);
            }
        } catch (Exception e) {
        }

        return bitmap;
    }
    /**
     * 保存图片到本地 第一个参数为需要保存的bitmap
     * */
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
            File f = new File(SDPATHIMG, picName + ".jpg");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return  picName + ".jpg";
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }
    static public File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

        }
        return dir;
    }
    //flie：要删除的文件夹的所在位置
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
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
    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /*
  * 旋转图片
  * @param angle
  * @param bitmap
  * @return Bitmap
  */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
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
    //获取相册图片路径
    public static String getRealPathFromURI(Context context,Uri contentUri) {

        String[] filePathColumns = {MediaStore.MediaColumns.DATA};

        ContentResolver contentResolver = context.getContentResolver();

        Cursor c = contentResolver.query(contentUri,filePathColumns,null,null,null);

        if(c !=null) {
            c.moveToFirst();
            int intcolumnIndex = c.getColumnIndex(filePathColumns[0]);
            String pathImg = c.getString(intcolumnIndex);
            c.close();
//            pics.add(0,pathImg);
//            mMPicAdapter.setData(pics);
            return  pathImg;
        }
        return  "";
    }
}
