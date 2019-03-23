package com.liuniukeji.mixin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.conference.ConferenceActivity;
import com.hyphenate.chatui.ui.VideoCallActivity;
import com.hyphenate.chatui.ui.VoiceCallActivity;
import com.hyphenate.util.EasyUtils;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 欢迎页
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;
    private static final int sleepTime = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        showAnimation();
    }

    @SuppressWarnings("all")
    private void showAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
//        AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);
        animation.setDuration(3000);
        img.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                UserBean user = AccountUtils.getUser(getApplicationContext());
//                if (null != user) {
//                    //进首页
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }

//                else {
//                    //登录
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }


                //-----------------------------------登录处理-----------------------------------
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (DemoHelper.getInstance().isLoggedIn()) {
                            // auto login mode, make sure all group and conversation is loaed before enter the main screen
                            long start = System.currentTimeMillis();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            EMClient.getInstance().groupManager().loadAllGroups();
                            long costTime = System.currentTimeMillis() - start;
                            //wait

//                            if (sleepTime - costTime > 0) {
//                                try {
//                                    Thread.sleep(sleepTime - costTime);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }


                            String topActivityName = EasyUtils.getTopActivityName(EMClient.getInstance().getContext());
                            if (topActivityName != null && (topActivityName.equals(VideoCallActivity.class.getName())
                                    || topActivityName.equals(VoiceCallActivity.class.getName()) ||
                                    topActivityName.equals(ConferenceActivity.class.getName()))) {

                                // nop
                                // avoid main screen overlap Calling Activity
                            } else {
                                //enter main screen
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            }
                            finish();
                        } else {
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                            }
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }).start();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //-----------------------------------登录处理-----------------------------------

    }

//    @Override
//    @SuppressWarnings("all")
//    protected void onStart() {
//        super.onStart();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (DemoHelper.getInstance().isLoggedIn()) {
//                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
//                    long start = System.currentTimeMillis();
//                    EMClient.getInstance().chatManager().loadAllConversations();
//                    EMClient.getInstance().groupManager().loadAllGroups();
//                    long costTime = System.currentTimeMillis() - start;
//                    //wait
//                    if (sleepTime - costTime > 0) {
//                        try {
//                            Thread.sleep(sleepTime - costTime);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    String topActivityName = EasyUtils.getTopActivityName(EMClient.getInstance().getContext());
//                    if (topActivityName != null && (topActivityName.equals(VideoCallActivity.class.getName())
//                            || topActivityName.equals(VoiceCallActivity.class.getName()) ||
//                            topActivityName.equals(ConferenceActivity.class.getName()))) {
//
//                        // nop
//                        // avoid main screen overlap Calling Activity
//                    } else {
//                        //enter main screen
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    }
//                    finish();
//                } else {
//                    try {
//                        Thread.sleep(sleepTime);
//                    } catch (InterruptedException e) {
//                    }
//                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    finish();
//                }
//            }
//        }).start();
//    }

}
