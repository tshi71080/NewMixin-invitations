package com.liuniukeji.mixin.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description 日期操作辅助类
 * @Author wanghaijun QQ:1819005139
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date 2018/1/15
 * @CreateBy Android Studio
 * @ModifiedBy // 修改作者, 联系方式, 修改日期 [无修改作者, 可为空]
 */
public class DateHelper {
    private final static String TAG = "DateHelper";

    /**
     * 指定日期加上指定天数后的日期
     *
     * @param dateStr 指定日期
     * @param num     增加的天数,如果为负数则为相减
     * @return
     */
    public static String addDay(String dateStr, int num) {
        String result = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(dateStr);
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.DATE, num);
            result = format.format(cl.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 指定日期减去指定天数后的日期，
     * 如果该日期在今天的日期之前返回今天日期，
     * 如果在今天日期之后正常返回相减结果
     *
     * @param dateStr 指定日期
     * @param num     相减的天数
     * @return
     */
    public static String specialSubDay(String dateStr, int num) {
        String result = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date date = format.parse(dateStr);
            Calendar cl = Calendar.getInstance();
            cl.setTime(date);
            cl.add(Calendar.DATE, -num);//相减
            int b = cl.compareTo(Calendar.getInstance());//与当前日期相比较
            if (b > 0) {
                result = format.format(cl.getTime());
            } else {
                result = format.format(new Date());//返回当前日期
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 当前日期加上指定天数后的日期
     *
     * @param num 增加的天数
     * @return
     */
    public static String nowAddDay(int num) {
        String result = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DATE, num);
            Date d = ca.getTime();
            result = format.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /***计算两个日期相隔的天数**/
    public static int daysBetweenDate(String firstDateStr, String secondDateStr) {
        int days = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date firstDate = df.parse(firstDateStr);
            Date secondDate = df.parse(secondDateStr);
            int result = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
            days = Math.abs(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /***计算距当前日期相隔的天数**/
    public static int daysCurrentDate(String dateStr) {
        int days = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date firstDate = df.parse(dateStr);
            String s = df.format(new Date());
            Date secondDate = df.parse(s);
            int result = (int) ((secondDate.getTime() - firstDate.getTime()) / (24 * 60 * 60 * 1000));
            //days = Math.abs(result);
            days = result;//返回实际值
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /***计算提醒日期距当前日期相隔的天数*/
    public static int alarmDaysCurrentDate(String dateStr) {
        int days = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date firstDate = df.parse(dateStr);
            Date secondDate = new Date();
            int result = (int) ((firstDate.getTime() - secondDate.getTime()) / (24 * 60 * 60 * 1000));
            days = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }


    /***将时间转换为时间戳**/
    public static String dateToStamp(String s) {
        String res;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /***将时间转换为时间戳**/
    public static Long dateToStampLong(String s) {
        Long res;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res = date.getTime();
        return res;
    }

    /***将时间转换为时间戳**/
    public static Long dateToStampLongSpecial(String s) {
        Long res;
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res = date.getTime();
        return res;
    }

    /*** 将时间戳转换为时间**/
    public static String stampToDate(String s) {
        String res;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*** 将时间戳转换为时间**/
    public static String stampToymd(Long s) {
        String res;
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*** 将当前ymd格式的日期转换为时间戳*/
    public static long nowToYmdStamp() {
        long res = 0;
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date();
            String dateStr = simpleDateFormat.format(date);

            Date newDate = simpleDateFormat.parse(dateStr);
            res = newDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    /***二维码是否过期**/
    public static boolean qrCodeExpire(String s) {
        boolean isExpire = false;
        //输入日期
        Long res1;
//        String pattern = "yyyy-MM-dd";
        String pattern = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res1 = date.getTime();
        long currentTime = System.currentTimeMillis();
        if (currentTime > res1) {
            //已经过期
            isExpire = true;
        }
        return isExpire;
    }

}
