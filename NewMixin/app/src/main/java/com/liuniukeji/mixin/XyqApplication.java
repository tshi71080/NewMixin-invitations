package com.liuniukeji.mixin;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Vibrator;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.danikula.videocache.HttpProxyCacheServer;
import com.hyphenate.chatui.DemoHelper;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.loacation.LocationService;
import com.liuniukeji.mixin.ui.login.ConnectBean;
import com.liuniukeji.mixin.ui.login.ConnectGroupBean;
import com.liuniukeji.mixin.util.PicassoImageLoader;
import com.liuniukeji.mixin.util.currency.LogUtils;
import com.liuniukeji.mixin.util.currency.Utils;
import com.liuniukeji.mixin.widget.MyFileNameGenerator;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.tencent.mmkv.MMKV;

import org.greenrobot.greendao.query.QueryBuilder;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;
import secure.coding.lnkj.com.lnsecure.LnSecureUtils;


/**
 * 主程序类
 */

public class XyqApplication extends Application {
    public static final String TAG = XyqApplication.class.getSimpleName();
    private static final String DEFAULT_USERID = "DEFAULT";
    static XyqApplication instance;
    public static Context context;

    private String filterValues;
    private HttpProxyCacheServer proxy;

    private ArrayList<Activity> activity_list = new ArrayList<Activity>();
    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";
    public LocationService locationService;
    public Vibrator mVibrator;

    public synchronized static XyqApplication getInstance() {
        if (null == instance) {
            instance = new XyqApplication();
        }
        return instance;
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        XyqApplication app = (XyqApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        initOkGo();
        initX();
        Utils.init(this);
        LogUtils.init(false, false, 'd', "LogUtils");
        initImagePicker();
        initJPush();
        //公司检安全测包
        LnSecureUtils.getDefault().regist(getApplicationContext(),
                "", getResources().getString(R.string.app_name), UrlUtils.APIHTTP);
        //蒲公英
        //PgyCrashManager.register(this);


//        初始化MMKV
        MMKV.initialize(getApplicationContext());
        //init demo helper
        DemoHelper.getInstance().init(context);

        // 初始化华为 HMS 推送服务
        //HMSPushHelper.getInstance().initHMSAgent(instance);
        //初始化视频录制
        initSmallVideo();
        //初始化百度定位sdk
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 将MultiDex注入到项目中
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void initOkGo() {
        OkGo.init(this);
        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                    .debug("OkGo", Level.INFO, true)
                    .setRetryCount(0)
                    //如果使用默认的 60秒,以下三行也不需要传
//                    .setConnectTimeout(Constants.TIME_OUT)  //全局的连接超时时间
//                    .setReadTimeOut(Constants.TIME_OUT)     //全局的读取超时时间
//                    .setWriteTimeOut(Constants.TIME_OUT)    //全局的写入超时时间

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())     //cookie使用内存缓存（app退出后，cookie消失）
                    //cookie持久化存储，如果cookie不过期，则一直有效
                    .setCookieStore(new PersistentCookieStore());

            //可以设置https的证书,以下几种方案根据需要自己设置,不需要不用设置
//                    .setCertificates()                            //方法一：信任所有证书
//                    .setCertificates(getAssets().open("srca.cer"))   //方法二：也可以自己设置https证书
            //方法三：传入bks证书,密码,和cer证书,支持双向加密
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))

            //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上,不需要就不要传
//                    .addCommonHeaders(headers) //设置全局公共头
            //设置全局公共参数
            // .addCommonParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initX() {
        x.Ext.init(this);
        // 是否输出debug日志, 开启debug会影响性能
        x.Ext.setDebug(false);
        if (x.isDebug()) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }


    public void addActivity(Activity _activity) {
        activity_list.add(_activity);
    }

    public void removeActivity(Activity activity) {
        activity_list.remove(activity);
    }

    public boolean checkHasActivity() {
        return activity_list.size() == 0 ? false : true;
    }

    // 获取最新activity
    public Activity getNowActivity() {
        Activity activity = null;
        if(activity_list.size()>0){
            activity = activity_list.get(activity_list.size()-1);
        }
        return activity;
    }


    public boolean isHasActivity(Class c) {
        Iterator<Activity> iterator = activity_list.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(c))
                return true;
        }
        return false;
    }

    //退出
    public void exit() {
        Iterator<Activity> iterator = activity_list.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            iterator.remove();
            activity.finish();
        }
    }



    /**
     * 初始化图片加载器
     */
    public void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        //设置图片加载器
        imagePicker.setImageLoader(new PicassoImageLoader());
        //显示拍照按钮
        imagePicker.setShowCamera(true);
        //允许裁剪（单选才有效）
        imagePicker.setCrop(true);
        //是否按矩形区域保存
        imagePicker.setSaveRectangle(false);
        //选中数量限制
        imagePicker.setSelectLimit(1);
        //裁剪框的形状
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusWidth(800);
        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);
        //保存文件的宽度。单位像素
        imagePicker.setOutPutX(1000);
        //保存文件的高度。单位像素
        imagePicker.setOutPutY(1000);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/mabeijianxi/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
        }
        // 初始化拍摄，遇到问题可选择开启此标记，以方便生成日志
        JianXiCamera.initialize(false,null);
    }

}



