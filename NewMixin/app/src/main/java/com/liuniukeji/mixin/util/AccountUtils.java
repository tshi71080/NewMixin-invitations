package com.liuniukeji.mixin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.liuniukeji.mixin.ui.loacation.LocationInfo;
import com.liuniukeji.mixin.ui.login.ConnectFriendBean;
import com.liuniukeji.mixin.ui.login.ConnectGroupBean;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.tencent.mmkv.MMKV;

import java.util.List;

/**
 * 用户账号工具类
 */

public class AccountUtils {
    /**
     * 获取用户实体
     */
    @Nullable
    public static UserBean getUser(Context context) {
        try {
            String userResult = MMKV.defaultMMKV().getString(Constants.USERINFO,"");
            Log.d("getUser", "getUser: " + userResult);
            UserBean userModel = JSON.parseObject(userResult, UserBean.class);
            return userModel;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static boolean isLogin(Context context) {
        try {
            UserBean user = getUser(context);
            if (user != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUserToken(Context context) {
        try {
            UserBean user = getUser(context);
            return user.getToken();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }


    public static boolean saveUserCache(Context context, UserBean user) {
        try {
            String userResult = JSON.toJSONString(user);
            MMKV.defaultMMKV().putString(Constants.USERINFO,userResult);
            //ACache.get(context).put(Constants.USERINFO, userResult);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }



    public static boolean saveUserConnect( List<ConnectGroupBean> groupBeanList) {
        try {
            String userResult = JSON.toJSONString(groupBeanList);
            MMKV.defaultMMKV().putString(Constants.USERCONNECTINFOGROUP,userResult);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 保存位置信息
     */
    public static boolean saveLocationInfo(Context context, String lat, String lng) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.LOCATION_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lat", lat);
        editor.putString("lng", lng);
        return editor.commit();
    }

    /**
     * 获取位置信息
     */
    public static LocationInfo getLocationInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.LOCATION_INFO, Context.MODE_PRIVATE);
        String lat = "";
        String lng = "";
        if (preferences != null) {
            lat = preferences.getString("lat", "");
            lng = preferences.getString("lng", "");
        }
        return new LocationInfo(lat, lng);
    }

    public static List<ConnectGroupBean> getUserConnectGroup(){

        try {
            String userResult = MMKV.defaultMMKV().getString(Constants.USERCONNECTINFOGROUP,"");
            List<ConnectGroupBean> list = JSON.parseArray(userResult,ConnectGroupBean.class);
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static boolean saveUserConnectfriend( List<ConnectFriendBean> friendsBeanList) {
        try {
            String userResult = JSON.toJSONString(friendsBeanList);
            MMKV.defaultMMKV().putString(Constants.USERCONNECTINFOFRIEND,userResult);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static List<ConnectFriendBean> getUserConnectFriend(){

        try {
            String userResult = MMKV.defaultMMKV().getString(Constants.USERCONNECTINFOFRIEND,"");
            List<ConnectFriendBean> list = JSON.parseArray(userResult,ConnectFriendBean.class);
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    public static boolean clearUserCache(Context context) {
        MMKV.defaultMMKV().remove(Constants.USERINFO);
        return false;
    }

    public static boolean clearUserConnect() {
        MMKV.defaultMMKV().remove(Constants.USERCONNECTINFOGROUP);
        return true;
    }

    public static boolean clearUserConnectFriend() {
        MMKV.defaultMMKV().remove(Constants.USERCONNECTINFOFRIEND);
        return true;
    }

    /**
     * 获取用户类型
     */
    public static int getUserType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.USERTYPE, Context.MODE_PRIVATE);
        int type = -1;
        if (preferences != null) {
            type = preferences.getInt("type", -1);
        }
        return type;
    }

    public static boolean saveUserType(Context context, int type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USERTYPE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("type", type);
        return editor.commit();
    }

}
