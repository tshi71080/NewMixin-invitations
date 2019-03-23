package com.liuniukeji.mixin.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatui.Constant;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.ui.ChatActivity;
import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.hyphenate.easeui.domain.EaseUser;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.mine.AlbumFragment;
import com.liuniukeji.mixin.ui.mine.MomentFragment;
import com.liuniukeji.mixin.ui.mine.UserInfo;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.SizeUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.mine.AlbumFragment;
import com.liuniukeji.mixin.ui.mine.MomentFragment;
import com.liuniukeji.mixin.util.AccountUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 好友资料详情
 */
public class FriendProfileActivity extends AppCompatActivity implements FriendProfileContract.View {

    @BindView(R.id.back_ly)
    LinearLayout backLy;
    @BindView(R.id.more_ly)
    LinearLayout moreLy;
    @BindView(R.id.follow_tv)
    TextView followTv;
    @BindView(R.id.fans_tv)
    TextView fansTv;
    @BindView(R.id.moment_tv)
    TextView momentTv;
    @BindView(R.id.avatar_iv)
    CircleImageView avatarIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.level_icon_iv)
    ImageView levelIconIv;
    @BindView(R.id.level_num_tv)
    TextView levelNumTv;
    @BindView(R.id.department_tv)
    TextView departmentTv;
    @BindView(R.id.signature_tv)
    TextView signatureTv;
    @BindView(R.id.birthday_tv)
    TextView birthdayTv;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.hobby_rcv)
    RecyclerView hobbyRcv;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.module_tb)
    TabLayout moduleTb;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.add_follow_tv)
    TextView addFollowTv;
    @BindView(R.id.go_chat_tv)
    TextView goChatTv;
    @BindView(R.id.cover_iv)
    ImageView coverIv;
    @BindView(R.id.bottom_ly)
    LinearLayout bottomLy;
    @BindView(R.id.tv_friend_nickname)
    TextView nicknameView;
    @BindView(R.id.rl_setfriend_remark)
    RelativeLayout rl_setfriend_remark;

    List<Fragment> fragmentList;

    MyAdapter pagedApter;

    String userId;

    FriendProfileContract.Presenter presenter;

    boolean isFocus;
    String imName;
    private String username = "";
    private FriendUserInfo muserInfo;

    public static final int CHANGE_REMARK_CODE = 0X0002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);

        presenter = new FriendProfilePresenter(this);
        presenter.attachView(this);

        //登录的用户
        String id = AccountUtils.getUser(this).getId();
        userId = getIntent().getStringExtra("userId");
        username = getIntent().getStringExtra("userName");
        if (null != userId && userId.equals(id)) {
            //隐藏底部操作栏
            bottomLy.setVisibility(View.GONE);
            //隐藏右上角按钮
            moreLy.setVisibility(View.GONE);
        }
        if (EmptyUtils.isNotEmpty(userId)||EmptyUtils.isNotEmpty(username)) {
            //加载头部信息
            presenter.getFriendInfo(userId,username);

        }
    }

    /**
     * 顶部基本信息赋值
     */
    private void setData(FriendUserInfo user) {

        //-------------------------加载模块切换--------------------------
        fragmentList = new ArrayList<>();
        fragmentList.add(MomentFragment.newInstance(user.getId()));
        fragmentList.add(AlbumFragment.newInstance(user.getId()));
        String[] title = {"动态", "相册"};
        pagedApter = new MyAdapter(getSupportFragmentManager(), fragmentList, Arrays.asList(title));
        vp.setAdapter(pagedApter);
        moduleTb.setupWithViewPager(vp);
        setUpIndicatorWidth(moduleTb);
        //-------------------------加载模块切换--------------------------

        if (null != user) {
            if(user.getIs_focus().equals("1")){
                rl_setfriend_remark.setVisibility(View.VISIBLE);
            }else{
                rl_setfriend_remark.setVisibility(View.GONE);
            }
            imName = user.getIm_name();
            nicknameView.setText(user.getRemark());
            nameTv.setText(user.getReal_name());
            //关注
            followTv.setText(user.getFollows());
            fansTv.setText(user.getFans());
            momentTv.setText(user.getMoments());
            //mineScoreTv.setText(user.getPoints());

            //年级/系别
            String school_class=user.getSchool_class();
            String depart_name=user.getSchool_department_name();
            departmentTv.setText(school_class+"届/"+depart_name);

            //封面加载
            XImage.loadCover(coverIv, user.getCover_path());

            //个性签名
            signatureTv.setText(user.getSignature());

            //处理性别显示图标&会员身份标识
            levelNumTv.setText(user.getExperience());
            String type = user.getVip_type();
            switch (user.getSex()) {
                case "1":
                    //show boy icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
                case "2":
                    //show girl icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.girl_level_color));
                    break;
                default:
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    break;
            }

            //用户头像展示
            String logUrl = user.getPhoto_path();
            if (EmptyUtils.isNotEmpty(logUrl)) {
                XImage.loadAvatar(avatarIv, logUrl);
            }

            //生日
            boolean isShow = user.getIs_show_birthday().equals("1") ? true : false;
            if (isShow) {
                //正常展示生日
                birthdayTv.setText(user.getBirthday());
            } else {
                birthdayTv.setText("保密");
            }

            //地址
            addressTv.setText(user.getAddress());
            //兴趣爱好
            //--------------------------------兴趣爱好----------------------------------
            LinearLayoutManager ms = new LinearLayoutManager(this);
            //  布局方式为横向布局
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);

            // 添加设置好的布局样式
            hobbyRcv.setLayoutManager(ms);


            //兴趣爱好数据
            List<String> list = user.getInterests();

            if (null != list && list.size() > 0) {
                HobbyAdapter adapter = new HobbyAdapter(list);
                hobbyRcv.setAdapter(adapter);
            }
            //--------------------------------兴趣爱好----------------------------------

            //是否关注
            //0没关注 1已关注
            isFocus = user.getIs_focus().equals("1") ? true : false;
            if (isFocus) {
                //是好友隐藏添加好友的按钮
                goChatTv.setVisibility(View.VISIBLE);
                addFollowTv.setVisibility(View.GONE);
                //addFollowTv.setText("已关注");
            } else {
                //addFollowTv.setText("关注");
                //不是好友显示添加好友
                goChatTv.setVisibility(View.GONE);
                addFollowTv.setVisibility(View.VISIBLE);
            }

            //添加or取消关注
            addFollowTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //禁止再次点击
                    addFollowTv.setClickable(false);
                    if (EmptyUtils.isNotEmpty(muserInfo)) {
                        if(EmptyUtils.isNotEmpty(userId)){
                            presenter.addFriend(userId);
                        }else{
                            presenter.addFriend(muserInfo.getId());
                        }

                        /*if (isFocus) {
                            //取消关注操作
                            if(EmptyUtils.isNotEmpty(userId)){
                                presenter.addOrCancelFocus(userId, "2");
                            }else{
                                presenter.addOrCancelFocus(muserInfo.getId(), "2");
                            }

                        } else {
                            //添加关注操作
                            if(EmptyUtils.isNotEmpty(userId)){
                                presenter.addOrCancelFocus(userId, "1");
                            }else{
                                presenter.addOrCancelFocus(muserInfo.getId(), "1");
                            }
                        }*/
                    }
                }
            });
        }
    }

    public class HobbyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public HobbyAdapter(List<String> data) {
            super(R.layout.hobby_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final String item) {
            helper.setText(R.id.tv_content, item);
        }
    }

    /**
     * 设置TAB中间的距离
     *
     * @param tabLayout
     */
    private void setUpIndicatorWidth(TabLayout tabLayout) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i == 0) {
                View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)
                        tab.getLayoutParams();
                p.setMargins(0, 0, SizeUtils.dp2px(this.getBaseContext(), 120.0f), 0);
                tab.requestLayout();
            }
        }
    }

    @OnClick({R.id.back_ly, R.id.more_ly, R.id.go_chat_tv,R.id.rl_setfriend_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_ly:
                finish();
                break;
            case R.id.more_ly:
                MorePopWindow popWindow = new MorePopWindow(this);
                popWindow.showPopupWindow(moreLy);

                break;
            case R.id.go_chat_tv:
                //点击聊天的时候预先保存用户的信息
                if(DemoHelper.getInstance().getContactList()!=null){
                    Map<String,EaseUser> map = DemoHelper.getInstance().getContactList();
                    EaseUser easeUser = map.get(imName);
                    if(easeUser==null){
                        EaseUser user = new EaseUser(imName);
                        if(EmptyUtils.isNotEmpty(muserInfo.getRemark())){
                            user.setNickname(muserInfo.getRemark());
                        }else{
                            user.setNickname(muserInfo.getReal_name());
                        }
                        user.setAvatar(muserInfo.getPhoto_path());
                        map.put(imName,user);
                        List<EaseUser> easeUserList = new ArrayList<>();
                        for(EaseUser user1:map.values()){
                            easeUserList.add(user1);
                        }
                        DemoHelper.getInstance().updateContactList(easeUserList);
                    }
                }
                //聊天页面跳转
                if (EmptyUtils.isNotEmpty(imName)) {
                    startActivity(new Intent(FriendProfileActivity.this,
                            ChatActivity.class).putExtra("userId", imName));
                    finish();
                } else {
                    ToastUtils.showShortToast("参数错误，无法跳转");
                }
                break;
                //修改好友的备注
            case R.id.rl_setfriend_remark:
                Intent intent = new Intent(this,SetFriendMarkActivity.class);
                intent.putExtra("im_name",muserInfo.getIm_name());
                intent.putExtra("friendUserId",muserInfo.getId());
                intent.putExtra("remarkName",muserInfo.getRemark());
                startActivityForResult(intent,CHANGE_REMARK_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(FriendUserInfo userInfo) {
        muserInfo = userInfo;
        setData(muserInfo);
    }

    @Override
    public void liftData(String info) {
        //添加关注接口返回信息
        addFollowTv.setClickable(true);
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
            if (info.contains("成功")) {
                if (isFocus) {
                    addFollowTv.setText("关注");
                    isFocus = false;
                } else {
                    isFocus = true;
                    addFollowTv.setText("已关注");
                }
            }
        }
    }

    @Override
    public void liftAddBlack(String info) {
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
            EventBus.getDefault().post(Constants.GROUP_INFO.MEMBER);
            finish();
        }
    }

    @Override
    public void setMarkSuccess() {

    }

    @Override
    public void getRemarklist(List<MyRemarkFriendBean> remarkFriendBeanList) {

    }

    @Override
    public void applySucess() {
        presenter.getFriendInfo(userId,username);
    }

    @Override
    public void deleteSuccess() {
        presenter.getFriendInfo(userId,username);
        //成功删除好友通知本地删除聊天记录
        EMClient.getInstance().chatManager().deleteConversation(muserInfo.getIm_name(), true);
        EventBus.getDefault().post(Constants.GROUP_INFO.MEMBER);
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }

    public class MyAdapter extends FragmentPagerAdapter {
        List<Fragment> frgList;
        List<String> stringList;

        public MyAdapter(FragmentManager fm, List<Fragment> fragments, List<String> strings) {
            super(fm);
            frgList = fragments;
            stringList = strings;
        }

        public void setCommentCount(String string) {
            this.stringList.set(1, string);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return frgList.get(position);
        }

        @Override
        public int getCount() {
            return frgList == null ? 0 : frgList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return stringList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }


    class MorePopWindow extends PopupWindow {
        private View conentView;

        public MorePopWindow(final Activity context) {
            final LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.more_popup_dialog, null);
            int h = context.getWindowManager().getDefaultDisplay().getHeight();
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            // 设置SelectPicPopupWindow的View
            this.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(w / 3 + 50);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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

            //举报
            LinearLayout reportLy = (LinearLayout) conentView
                    .findViewById(R.id.report_ly);


            //拉黑
            LinearLayout blackLy = (LinearLayout) conentView
                    .findViewById(R.id.black_ly);



            //删除好友
            LinearLayout llDelete = conentView.findViewById(R.id.ll_delete);
            LinearLayout deleteFriend = conentView
                    .findViewById(R.id.delete_friend);
            if(isFocus){
                llDelete.setVisibility(View.VISIBLE);
            }else{
                llDelete.setVisibility(View.GONE);
            }


            reportLy.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    MorePopWindow.this.dismiss();
                    //举报
                    Intent intent=new Intent();
                    intent.putExtra("member_id",userId);
                    intent.setClass(context, ReportActivity.class);
                    context.startActivity(intent);
                }
            });

            blackLy.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    MorePopWindow.this.dismiss();
                    //拉黑
                    if(EmptyUtils.isNotEmpty(userId)){
                        presenter.addBlack(userId, "add");
                    }else if(EmptyUtils.isNotEmpty(muserInfo.getId())){
                        presenter.addBlack(muserInfo.getId(), "add");
                    }else{
                        ToastUtils.showShortToast("参数错误，无法操作");
                    }
                }
            });

            deleteFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MorePopWindow.this.dismiss();
                    if(EmptyUtils.isNotEmpty(userId)){
                        presenter.deleteFriend(userId);
                    }else{
                        presenter.deleteFriend(muserInfo.getId());
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHANGE_REMARK_CODE){
            presenter.getFriendInfo(userId,username);
        }
    }
}
