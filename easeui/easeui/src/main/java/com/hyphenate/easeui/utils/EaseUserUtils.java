package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.tencent.mmkv.MMKV;

import java.util.List;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null) {
            return userProvider.getUser(username);
        }

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                Glide.with(context).load(user.getAvatar()).placeholder(R.drawable.em_avatar).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).
                        placeholder(R.drawable.em_avatar).into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.em_avatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNickname() != null) {
                textView.setText(user.getNickname());
            } else {
                textView.setText(username);
            }
        }
    }

    /**
     * 获取备注好友信息
     * @return
     */
    public static List<MyRemarkFriendBean> getRemarkBeanList(){
        try {
            String userResult = MMKV.defaultMMKV().getString("USERREMARKINFO","");
            List<MyRemarkFriendBean> list = JSON.parseArray(userResult,MyRemarkFriendBean.class);
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
