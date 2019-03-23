package com.liuniukeji.mixin.util;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.liuniukeji.mixin.XyqApplication;


public class PreferenceUtils {
    private static SharedPreferences mSharedPreferences;

    private static synchronized SharedPreferences getPreferneces() {
        if (mSharedPreferences == null) {
            // mSharedPreferences = App.context.getSharedPreferences(
            // PREFERENCE_NAME, Context.MODE_PRIVATE);
            mSharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(XyqApplication.getInstance().context);
        }
        return mSharedPreferences;
    }

    /**
     * 打印所有
     */
    public static void print() {
        System.out.println(getPreferneces().getAll());
    }

    /**
     * 清空保存在默认SharePreference下的所有数据
     */
    public static void clear() {
        String phone = PreferenceUtils.getString("phone");
        getPreferneces().edit().clear().commit();
        PreferenceUtils.putString("phone", phone);
    }

    /**
     * 保存字符串
     *
     * @return
     */
    public static void putString(String key, String value) {
        getPreferneces().edit().putString(key, value).commit();
    }

    /**
     * 读取字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return getPreferneces().getString(key, "");

    }

    public static void putCookie(String value) {
        putString("cookie", value);
    }

    public static String getCookie() {
        return getString("cookie");
    }

    /**
     * 保存整型值
     *
     * @return
     */
    public static void putInt(String key, int value) {
        getPreferneces().edit().putInt(key, value).commit();
    }

    /**
     * 读取整型值
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return getPreferneces().getInt(key, 0);
    }

    public static int getIntDefault(String key, int defult) {
        return getPreferneces().getInt(key, defult);
    }

    /**
     * 保存布尔值
     *
     * @return
     */
    public static void putBoolean(String key, Boolean value) {
        getPreferneces().edit().putBoolean(key, value).commit();
    }

    public static void putLong(String key, long value) {
        getPreferneces().edit().putLong(key, value).commit();
    }

    public static long getLong(String key) {
        return getPreferneces().getLong(key, 0);
    }

    /**
     * t 读取布尔值
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferneces().getBoolean(key, defValue);

    }

    /**
     * 移除字段
     *
     * @return
     */
    public static void removeString(String key) {
        getPreferneces().edit().remove(key).commit();
    }



    public static int getAuthenticationStatus() {
        return getIntDefault("authenticationStatus", 4);
    }

    public static void setAuthenticationStatus(int authenticationStatus) {
        putInt("authenticationStatus", authenticationStatus);
    }
}
