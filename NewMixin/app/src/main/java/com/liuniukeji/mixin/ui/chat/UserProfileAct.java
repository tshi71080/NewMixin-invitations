package com.liuniukeji.mixin.ui.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.mine.UserInfo;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.ui.mine.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户资料
 * （聊天界面）
 */
public class UserProfileAct extends AppCompatActivity implements UserProfileContract.View{

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.profile_avatar_iv)
    CircleImageView profileAvatarIv;
    @BindView(R.id.profile_avatar_rl)
    RelativeLayout profileAvatarRl;
    @BindView(R.id.signature_tv)
    TextView signatureTv;
    @BindView(R.id.signature_content_tv)
    TextView signatureContentTv;
    @BindView(R.id.profile_signature_rl)
    RelativeLayout profileSignatureRl;
    @BindView(R.id.profile_gender_tv)
    TextView profileGenderTv;
    @BindView(R.id.profile_birthday_tv)
    TextView profileBirthdayTv;
    @BindView(R.id.profile_birthday_rl)
    RelativeLayout profileBirthdayRl;
    @BindView(R.id.profile_address_tv)
    TextView profileAddressTv;
    @BindView(R.id.profile_address_rl)
    RelativeLayout profileAddressRl;
    @BindView(R.id.profile_hobby_tv)
    TextView profileHobbyTv;
    @BindView(R.id.profile_hobby_rl)
    RelativeLayout profileHobbyRl;
    @BindView(R.id.profile_name_tv)
    TextView profileNameTv;

    UserProfileContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_act);
        ButterKnife.bind(this);

        headTitleTv.setText("用户资料");

        presenter=new UserProfilePresenter(this);
        presenter.attachView(this);


        String username = getIntent().getStringExtra("username");

        if(EmptyUtils.isNotEmpty(username)){
            presenter.getUserInfo(username);
        }


//        if (username != null) {
//            if (username.equals(EMClient.getInstance().getCurrentUser())) {
//                 //当前用户
//                EaseUserUtils.setUserNick(username, profileNameTv);
//                EaseUserUtils.setUserAvatar(this, username, profileAvatarIv);
//            } else {
//                EaseUserUtils.setUserNick(username, profileNameTv);
//                EaseUserUtils.setUserAvatar(this, username, profileAvatarIv);
//                asyncFetchUserInfo(username);
//            }
//        }

    }

    public void asyncFetchUserInfo(String username) {
        DemoHelper.getInstance().getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser user) {
                if (user != null) {
                    DemoHelper.getInstance().saveContact(user);
                    if (isFinishing()) {
                        return;
                    }
                    profileNameTv.setText(user.getNickname());
                    if (!TextUtils.isEmpty(user.getAvatar())) {
                        Glide.with(UserProfileAct.this).load(user.getAvatar()).placeholder(R.drawable.em_avatar).into(profileAvatarIv);
                    } else {
                        Glide.with(UserProfileAct.this).load(R.drawable.em_avatar).into(profileAvatarIv);
                    }
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }


    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(UserInfo info) {
        if(null!=info){
            //-----------------------保存数据聊天用户数据---------------------------

            EaseUser easeUser = new EaseUser(info.getIm_name());
            easeUser.setAvatar(info.getPhoto_path());
            easeUser.setNickname(info.getReal_name());

            DemoHelper.getInstance().saveContact(easeUser);
            //-----------------------保存数据聊天用户数据---------------------------

            //头像
            XImage.loadAvatar(profileAvatarIv,info.getPhoto_path());

            //姓名
            profileNameTv.setText(info.getReal_name());

            //个性签名
            signatureContentTv.setText(info.getSignature());

            //性别
            String gender=info.getSex().equals("1")?"男":"女";
            profileGenderTv.setText(gender);


            //生日（需要处理是否显示生日这一个设置选项）
            boolean isShow = info.getIs_show_birthday().equals("1") ? true : false;
            if (isShow) {
                //正常展示生日
                profileBirthdayTv.setText(info.getBirthday());
            } else {
                profileBirthdayTv.setText("保密");
            }

            //地址
            profileAddressTv.setText(info.getAddress());

            //兴趣爱好
            profileHobbyTv.setText(info.getInterests());
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
}
