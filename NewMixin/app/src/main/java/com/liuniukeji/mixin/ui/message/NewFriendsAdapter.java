package com.liuniukeji.mixin.ui.message;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.XImage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Copyright (c) 山东六牛网络科技有限公司 https://liuniukeji.com
 *
 * @Description
 * @Author (LiShiyang / 845719506 @ qq.com)
 * @Copyright Copyright (c) 山东六牛网络科技有限公司 保留所有版权(https://www.liuniukeji.com)
 * @Date ${Date}
 * @CreateBy Android Studio
 */
public class NewFriendsAdapter extends BaseQuickAdapter<NewFriendsBean,BaseViewHolder> {
    public NewFriendsAdapter(@Nullable List<NewFriendsBean> data) {
        super(R.layout.item_new_friend_require, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewFriendsBean item) {
        CircleImageView headView = helper.getView(R.id.avatar_iv);
        XImage.loadAvatar(headView,item.getPhoto_path());
        helper.setText(R.id.tv_username,item.getReal_name());
        TextView tipView = helper.getView(R.id.btn_add_friend);
        switch (item.getStatus()){
            case "0":
                tipView.setText("同意");
                tipView.setBackgroundResource(R.drawable.bg_add_friend_btn);
                tipView.setTextColor(Color.WHITE);
                tipView.setEnabled(true);
                break;
            case "1":
                tipView.setText("已同意");
                tipView.setTextColor(Color.GRAY);
                tipView.setBackgroundColor(Color.WHITE);
                tipView.setEnabled(false);
                break;
        }
        helper.addOnClickListener(R.id.btn_add_friend);
    }
}
