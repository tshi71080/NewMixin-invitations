package com.liuniukeji.mixin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;


/**
 * Created by zjh  工具类
 */
public class SettingPrefUtil {

  //是否第一次登录
  public static Boolean getisFirstIn(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getBoolean("isFirstIn", true);
  }
  public static void setisFirstIn(Context context, Boolean bool) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putBoolean("isFirstIn", bool).apply();
  }
  //登录状态
  public static Boolean getisLogin(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getBoolean("isLogin", false);
  }
  public static void setisLogin(Context context, Boolean bool) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putBoolean("isLogin", bool).apply();
  }
  //用户信息
 /* public static User getUserBeen(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//    LLog.d("用戶信息",prefs.getString("userbeen", ""));
    String userString = prefs.getString("userbeen", "");
    if ("".equals(userString)){
      return null;
    }
    return JSON.parseObject(userString,User.class);
  }*/
  public static void setUserBeenUser(Context context, String user) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("userbeen", user).apply();
  }
  //登录ID
  public static void setLoginUid(Context context, String LoginUid) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("LoginUid", LoginUid).apply();
  }
  public static String getLoginUid(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("LoginUid", "");
  }
  //用户token
  public static void setUserToken(Context context, String token) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("usertoken", token).apply();
  }
  public static String getUserToken(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("usertoken", "");
  }
  //数据传递ID
  public static void setId(Context context, String id) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("Id", id).apply();
  }
  public static String getId(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("Id", "");
  }
  //数据传递TripID
  public static void setTripID(Context context, String id) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("TripID", id).apply();
  }
  public static String getTripID(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("TripID", "");
  }
  //数据传递拜访类型
  public static void setVisitType(Context context, String id) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("VisitType", id).apply();
  }
  public static String getVisitType(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("VisitType", "");
  }
  //数据传递拜访状态
  public static void setVisitStatus(Context context, String id) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("visit_status", id).apply();
  }
  public static String getVisitStatus(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("visit_status", "");
  }
  //日期
  public static void setDate(Context context, String id) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("Date", id).apply();
  }
  public static String getDate(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("Date", "");
  }
  //属性选择识别码
  public static void setKey(Context context, int content){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putInt("attrKey", content).apply();
  }
  public static int getKey(Context context){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getInt("attrKey", 0);
  }
  //界面显示标题
  public static void setTitle(Context context, String content){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("title", content).apply();
  }
  public static String getTitle(Context context){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("title","");
  }
  //刷新状态
  public static Boolean getisRefresh(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getBoolean("Refresh", false);
  }
  public static void setisRefresh(Context context, Boolean bool) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putBoolean("Refresh", bool).apply();
  }
  //需求刷新状态
  public static Boolean getLocationisRefresh(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getBoolean("LocationRefresh", false);
  }
  public static void setLocationisRefresh(Context context, Boolean bool) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putBoolean("LocationRefresh", bool).apply();
  }
/*  //Latitude
  public static LatLng getLatlng(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    LatLng latLng=new LatLng(Double.parseDouble(prefs.getString("Latitude","0.0")), Double.parseDouble(prefs.getString("Longitude","0.0")));
    return latLng;
  }
  public static void setLatlng(Context context, LatLng bool) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("Latitude", bool.latitude+"").apply();
    prefs.edit().putString("Longitude", bool.longitude+"").apply();
  }*/

  //下载进度记录
  public static void setDownProgress(Context context, String filePath , int progress){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putInt(filePath, progress).apply();
  }

  public static int getDownProgress(Context context, String filePath){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getInt(filePath,0);
  }

  //修改头像或背景图
  public static void setUpdatePhotoType(Context context, String type){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString("updatephototype", type).apply();
  }

  public static String getUpdatePhotoType(Context context){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString("updatephototype","1");
  }

/*  //属性选择识别码
  public static void setLangType(Context context, String key){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    prefs.edit().putString(Constants.LANGUAGE_TYPE, key).apply();
  }
  public static String getLangType(Context context){
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    return prefs.getString(Constants.LANGUAGE_TYPE, getSystemLanguageType(context));
  }*/

  public static String getSystemLanguageType(Context context){
    Locale locale = context.getResources().getConfiguration().locale;
    String language = locale.getLanguage();
    return language;
  }
}
