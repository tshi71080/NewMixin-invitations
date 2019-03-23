package com.liuniukeji.mixin.ui.discover;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.ui.home.FriendProfileActivity;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.ui.mine.MyVisitorActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.util.AccountUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群成员列表
 */

public class GroupMembersActivity extends AppCompatActivity implements GroupMembersContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    GroupMembersContract.Presenter presenter;

    private MemberAdapter mAdapter;
    private List<GroupMembers> dataList;
    private List<String> muteList = Collections.synchronizedList(new ArrayList<String>());

    int p = 1;
    int mCurrentCounter = 0;
    String im_id;
    private List<String> adminList = Collections.synchronizedList(new ArrayList<String>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        ButterKnife.bind(this);
        headTitleTv.setText("群成员");

        presenter = new GroupMembersPresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new MemberAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);

        im_id = getIntent().getStringExtra("im_id");

        getData();

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                getData();
            }
        });

        //设置加载更多监听
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurrentCounter < Constants.COUNT_SIZE * p) {
                    mAdapter.loadMoreEnd();
                    return;
                }
                p++;
                getData();
            }
        }, rvList);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //好友详情主页跳转
                Intent intent = new Intent();
                intent.putExtra("userId", dataList.get(position).getMember_id());
                intent.setClass(GroupMembersActivity.this, FriendProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (EmptyUtils.isNotEmpty(im_id)) {
            //获取列表数据
            presenter.getGroupMemberList(p, im_id);
        }
    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(List<GroupMembers> infoList) {
        if (this.dataList == null || infoList == null || infoList.size() == 0) {
            if (p == 1) {
                mAdapter.setEmptyView(R.layout.empty_layout);
                swipeLayout.setRefreshing(false);
            }
            return;
        }
        if (p == 1) {
            swipeLayout.setRefreshing(false);
            this.dataList.clear();
            this.dataList.addAll(infoList);
            getMuteList();
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        }else{
            if (null != swipeLayout) {
                swipeLayout.setRefreshing(false);
            }
            this.dataList.addAll(infoList);
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        }

    }

    //获取被禁言的成员列表
    private void getMuteList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    muteList.clear();
                    muteList.addAll(EMClient.getInstance().groupManager().fetchGroupMuteList(im_id, 0, 200).keySet());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void liftData(CommonResult result) {
        if (EmptyUtils.isNotEmpty(result.getInfo())) {
            ToastUtils.showShortToast(result.getInfo());
        }
        if (result.getStatus() == 1) {
            //移除成功，刷新页面数据
            p = 1;
            getData();
        }
    }

    //添加管理员以及移出管理员操作成功
    @Override
    public void operateSucess() {
        p = 1;
        presenter.getGroupMemberList(p,im_id);
    }

    @Override
    public void onEmpty() {
        if (this.dataList != null && p == 1) {
            this.dataList.clear();
            mAdapter.setNewData(this.dataList);
            mAdapter.setEmptyView(R.layout.empty_layout);
        } else if (this.dataList != null && mAdapter != null && p > 1) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onNetError() {
        if (null != mAdapter) {
            mAdapter.loadMoreEnd();
        }
    }

    class MemberAdapter extends BaseQuickAdapter<GroupMembers, BaseViewHolder> {

        public MemberAdapter(@Nullable List<GroupMembers> data) {
            super(R.layout.member_list_item, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, final GroupMembers item) {

            helper.setText(R.id.name_tv, item.getReal_name());
            TextView nameTv = helper.getView(R.id.name_tv);

            TextView isCanTalk = helper.getView(R.id.talk_tv);
            TextView addToAdmin = helper.getView(R.id.set_to_admin);

            helper.setText(R.id.signature_tv, item.getSignature());
            helper.setText(R.id.level_tv, item.getExperience());

            ImageView iv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(iv, item.getPhoto_path());
            ImageView levelIconIv = helper.getView(R.id.level_icon_iv);
            TextView levelNumTv = helper.getView(R.id.level_tv);

            TextView removeTv = helper.getView(R.id.remove_tv);

            //移除好友操作
            final String member_im_name = item.getMember_im_name();
            final String group_im_id = item.getGroup_im_id();

            UserBean userBean = AccountUtils.getUser(GroupMembersActivity.this);
            boolean b1 = userBean.getIm_name().equals(member_im_name);

            //初始化环信的群组
            EMGroup group = EMClient.getInstance().groupManager().getGroup(group_im_id);
            boolean b2 = isCurrentOwner(group);
            boolean currentUserisOwner = isGroupOwner(group,item.getMember_im_name());
            adminList = group.getAdminList();
            boolean isAdmin = isCurrentAdmin(group);
            //不是自己并且不是群主才显示移除按钮
            if (!b1&&!currentUserisOwner) {
                removeTv.setVisibility(View.VISIBLE);
                //isCanTalk.setVisibility(View.VISIBLE);
                //isCanTalk.setEnabled(true);
            }else{
                removeTv.setVisibility(View.GONE);
            }
            //登录账号是群主并且不是群主自己
            if(b2&&!b1){
                addToAdmin.setVisibility(View.VISIBLE);
                //判断是否是管理员(0普通用户，1群主，2管理员)
                if(item.getType().equals("2")){
                    addToAdmin.setText("取消管理员");
                    addToAdmin.setTextColor(getResources().getColor(R.color.boy_level_color));
                    addToAdmin.setBackgroundResource(R.drawable.is_admin_bg);
                }else{
                    addToAdmin.setText("设为管理员");
                    addToAdmin.setTextColor(getResources().getColor(R.color.gray));
                    addToAdmin.setBackgroundResource(R.drawable.cant_talk_bg);
                }
            }else{
                addToAdmin.setVisibility(View.GONE);
            }

            //禁言中
            if(isInMuteList(item.getMember_im_name())){
                isCanTalk.setText("解禁");
                isCanTalk.setTextColor(getResources().getColor(R.color.gray));
                isCanTalk.setBackgroundResource(R.drawable.cant_talk_bg);
            }else{
                isCanTalk.setText("禁言");
                isCanTalk.setTextColor(getResources().getColor(R.color.color_yellow));
                isCanTalk.setBackgroundResource(R.drawable.can_talk_bg);
            }

            removeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.removeMember(group_im_id, member_im_name);
                }
            });

            //处理性别显示图标&会员身份标识
            String type = item.getVip_type();
            switch (item.getSex()) {
                case "1":
                    //show boy icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));
                    //显示默认蓝色
                    nameTv.setTextColor(Color.parseColor("#2AABF8"));
                    break;
                case "2":
                    //show girl icon
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_female_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.girl_level_color));
                    //显示默认红色
                    nameTv.setTextColor(Color.parseColor("#FE5E7C"));
                    break;
                default:
                    if (EmptyUtils.isNotEmpty(type) && type.equals("1")) {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_vip));
                    } else {
                        levelIconIv.setBackground(getResources().getDrawable(R.mipmap.common_icon_lv_male_n));
                    }
                    levelNumTv.setTextColor(getResources().getColor(R.color.boy_level_color));

                    //显示默认蓝色
                    nameTv.setTextColor(Color.parseColor("#2AABF8"));

                    break;
            }
            //单个群成员禁言
            isCanTalk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                //禁言中点击解除禁言
                                if(isInMuteList(item.getMember_im_name())){
                                    List<String> list = new ArrayList<String>();
                                    list.add(item.getMember_im_name());
                                    EMClient.getInstance().groupManager().unMuteGroupMembers(im_id, list);
                                }else{
                                    List<String> muteMembers = new ArrayList<String>();
                                    muteMembers.add(item.getMember_im_name());
                                    EMClient.getInstance().groupManager().muteGroupMembers(im_id, muteMembers, 20 * 60 * 1000);
                                }
                            }catch (HyphenateException e){
                                e.printStackTrace();
                            }finally {
                                getMuteList();
                            }

                        }
                    }).start();

                }
            });
            //添加或者移出管理员权限
            addToAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getType().equals("2")){
                        presenter.removeFromAdmin(group_im_id,member_im_name);
                    }else{
                        presenter.addToAdmin(group_im_id,member_im_name);
                    }

                }
            });

        }

    }

    //判断是否在禁言列表
    boolean isInMuteList(String id) {
        synchronized (muteList) {
            if (id != null && !id.isEmpty()) {
                for (String item : muteList) {
                    if (id.equals(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //是否是管理员
    boolean isCurrentAdmin(EMGroup group) {
        synchronized (adminList) {
            String currentUser = EMClient.getInstance().getCurrentUser();
            for (String admin : adminList) {
                if (currentUser.equals(admin)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isCurrentOwner(EMGroup group) {
        String owner = group.getOwner();
        if (owner == null || owner.isEmpty()) {
            return false;
        }
        return owner.equals(EMClient.getInstance().getCurrentUser());
    }

    boolean isGroupOwner(EMGroup group,String userName) {
        String owner = group.getOwner();
        if (owner == null || owner.isEmpty()) {
            return false;
        }
        return owner.equals(userName);
    }


}
