package com.liuniukeji.mixin.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.pub.note.PubNotesActivity;
import com.liuniukeji.mixin.ui.pub.video.PubVideoActivity;
import com.lzy.imagepicker.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布界面
 * Created by huanghaibin on 2017/9/25.
 */

public class PubActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.btn_pub)
    ImageView mBtnPub;
    @BindView(R.id.ll_pub_blog)
    LinearLayout ll_pub_blog;
    @BindView(R.id.ll_pub_tweet)
    LinearLayout ll_pub_tweet;

    List<LinearLayout> mLays = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        ButterKnife.bind(this);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mLays.add(ll_pub_blog);
        mLays.add(ll_pub_tweet);
    }

    public static void show(Context context) {
        context.startActivity(new Intent(context, PubActivity.class));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mBtnPub.animate()
                .rotation(135.0f)
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .start();
        show(0);
        show(1);
    }

    @OnClick({R.id.rl_main, R.id.ll_pub_tweet, R.id.ll_pub_blog})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_main:
                dismiss();
                break;
            case R.id.ll_pub_tweet:
                //发布图文
                startActivityForResult(new Intent().setClass(this, PubNotesActivity.class),0x111);
                break;
            case R.id.ll_pub_blog:
                startActivityForResult(new Intent().setClass(this, PubVideoActivity.class),0x111);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x111&&resultCode==0x112){
            finish();
        }
    }

    private void dismiss(){
        close();
        close(0);
        close(1);
    }

    private void close() {
        mBtnPub.clearAnimation();
        mBtnPub.animate()
                .rotation(0f)
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        finish();
                    }
                })
                .start();
    }

    private void show(int position) {
        int angle = position == 0 ? 60 : 120;
        float x = (float) Math.cos(angle * (Math.PI / 180)) * Utils.dp2px(this, 200);
        float y = (float) -Math.sin(angle * (Math.PI / 180)) * Utils.dp2px(this, 200);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mLays.get(position), "translationX", 0, x);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mLays.get(position), "translationY", 0, y);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(180);
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.start();
    }

    private void close(int position) {
        int angle = position == 0 ? 60 : 120;
        float x = (float) Math.cos(angle * (Math.PI / 180)) * Utils.dp2px(this, 200);
        float y = (float) -Math.sin(angle * (Math.PI / 180)) * Utils.dp2px(this, 200);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mLays.get(position), "translationX", x, 0);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mLays.get(position), "translationY", y, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(180);
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.start();
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


}
