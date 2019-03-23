package com.liuniukeji.mixin.util.common;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description 电话号码操作辅助
 * @Author wanghaijun QQ:1819005139
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date 2018/3/10
 * @CreateBy Android Studio
 * @ModifiedBy // 修改作者, 联系方式, 修改日期 [无修改作者, 可为空]
 */
public class PhoneNumHelper {

    public static String getNum(Context context, String path) {
        String result = "";
        try {
//            InputStream is=context.getResources().openRawResource(R.raw.number);
//            File file = new File("D:\\number.txt");// Text文件
//            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
//                    "number.txt");
            File file = new File(path);
            // 构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));

            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                // 使用readLine方法，一次读一行
                //System.out.println(s);
                sb.append(s);
                sb.append("\n");
            }
            result = sb.toString();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
