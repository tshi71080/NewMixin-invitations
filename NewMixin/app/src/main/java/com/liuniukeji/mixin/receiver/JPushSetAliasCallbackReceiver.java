package com.liuniukeji.mixin.receiver;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 极光推送设置别名回调接收器
 *
 * @author TianZhihao
 * Created on 2018/2/28.
 */

public class JPushSetAliasCallbackReceiver extends JPushMessageReceiver {
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {

        switch (jPushMessage.getSequence()) {
            /**
             * 设置别名
             */
            case 1111:
                Log.d("AliasReceiver", "onAliasOperatorResult: " + "error_code:" +
                        jPushMessage.getErrorCode() + "Alias:" + jPushMessage.getAlias());
                break;
            /**
             * 删除别名
             */
            case 1112:
                Log.d("AliasReceiver", "onAliasOperatorResult: " +
                        jPushMessage.getErrorCode() + jPushMessage.getAlias());
                break;
            case 2222:
                Log.d("AliasReceiver", "onAliasOperatorResult: ");
                break;
            default:
                break;
        }
    }
}
