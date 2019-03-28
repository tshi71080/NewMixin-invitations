package com.hyphenate.chatui.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hyphenate.chatui.ui.AddContactActivity;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.discover.CreateGroupActivity;
import com.liuniukeji.mixin.ui.main.MainActivity;

/**
 * 自定义popupWindow
 * 弹出的下拉框
 *
 * @author wwj
 */
public class AddPopWindow extends PopupWindow {
    private View conentView;

    public AddPopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.add_popup_dialog, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 50);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        LinearLayout addFriendLayout = (LinearLayout) conentView
                .findViewById(R.id.add_friend_layout);//添加好友
        LinearLayout create_group_layout = (LinearLayout) conentView
                .findViewById(R.id.create_group_layout);//发起群聊
        LinearLayout QcadLayout = (LinearLayout) conentView
                .findViewById(R.id.saosao_layout);//扫一扫

        addFriendLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AddPopWindow.this.dismiss();
                context.startActivity(new Intent(context, AddContactActivity.class));
            }
        });
        create_group_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AddPopWindow.this.dismiss();
                context.startActivity(new Intent(context, CreateGroupActivity.class));
            }
        });
        QcadLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //((MainActivity) context).saosao();
                AddPopWindow.this.dismiss();
                ((MainActivity) context).scanQr();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
        } else {
            this.dismiss();
        }
    }
}
