package com.liuniukeji.mixin.util.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description 电话相关操作辅助
 * @Author wanghaijun QQ:1819005139
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date 2018/1/24
 * @CreateBy Android Studio
 * @ModifiedBy // 修改作者, 联系方式, 修改日期 [无修改作者, 可为空]
 */
public class PhoneHelper {

    /*** 调用拨号功能**/
    public static void call(Context context, String phone) {
        try {
            if (EmptyUtils.isNotEmpty(phone)) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    String[] permissions = {Manifest.permission.CALL_PHONE};
                    ActivityCompat.requestPermissions((Activity) context, permissions, 0);
                    return;
                }
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
