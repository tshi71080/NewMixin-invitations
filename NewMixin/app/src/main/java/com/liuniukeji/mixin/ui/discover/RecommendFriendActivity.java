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
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.home.FriendProfileActivity;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 推荐好友
 */

public class RecommendFriendActivity extends AppCompatActivity implements RecommendContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private FriendAdapter mAdapter;
    private List<RecommendUser> dataList;

    RecommendContract.Presenter presenter;
    int p = 1;
    int mCurrentCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_friend);
        ButterKnife.bind(this);
        headTitleTv.setText("推荐好友");

        presenter = new RecommendPresenter(this);
        presenter.attachView(this);


        //获取列表数据
        presenter.getRecommendUser();

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new FriendAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);


        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                //获取列表数据
                presenter.getRecommendUser();

            }
        });

        //点击跳转
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //好友详情主页跳转
                Intent intent = new Intent();
                intent.putExtra("userId", dataList.get(position).getId());
                intent.setClass(RecommendFriendActivity.this, FriendProfileActivity.class);
                startActivity(intent);

            }
        });

    }


    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(List<RecommendUser> infoList) {

        if (this.dataList == null || infoList == null || infoList.size() == 0) {
            if (p == 1) {
                mAdapter.setEmptyView(R.layout.empty_layout);
                mAdapter.notifyDataSetChanged();
                if (null != swipeLayout) {
                    swipeLayout.setRefreshing(false);
                }
            }
            if (null != mAdapter) {
                mAdapter.loadMoreEnd();
            }
            return;
        }
        if (p == 1) {
            if (null != swipeLayout) {
                swipeLayout.setRefreshing(false);
            }
            this.dataList.clear();
            this.dataList.addAll(infoList);
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        } else {
            if (null != swipeLayout) {
                swipeLayout.setRefreshing(false);
            }
            this.dataList.addAll(infoList);
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        }


    }

    @Override
    public void onEmpty() {
        swipeLayout.setRefreshing(false);
        mAdapter.setEmptyView(R.layout.empty_layout);
    }

    @Override
    public void onNetError() {
        swipeLayout.setRefreshing(false);
        mAdapter.setEmptyView(R.layout.empty_layout);
    }


    class FriendAdapter extends BaseQuickAdapter<RecommendUser, BaseViewHolder> {

        public FriendAdapter(@Nullable List<RecommendUser> data) {
            super(R.layout.phone_friend_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final RecommendUser item) {

            //设置昵称
            helper.setText(R.id.user_name_tv, item.getReal_name());
            TextView nameTv = helper.getView(R.id.user_name_tv);
//            if (EmptyUtils.isNotEmpty(item.getColor())) {
//                //设置昵称字体颜色
//                nameTv.setTextColor(Color.parseColor(item.getColor()));
//            }

            //显示头像
            ImageView avatarIv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(avatarIv, item.getPhoto_path());

            //用户等级
            ImageView levelIconIv = helper.getView(R.id.level_icon_iv);
            TextView levelNumTv = helper.getView(R.id.level_num_tv);

            //签名
            TextView signatureTv = helper.getView(R.id.signature_tv);
            signatureTv.setText(item.getSignature());

            levelNumTv.setText(item.getExperience());
            //String type = item.getVip_type();
            String type = "3";
            switch (item.getSex()) {
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

            //处理关注状态
            TextView followTv = helper.getView(R.id.follow_state_tv);
            //0没关注 1已关注
            //final boolean isFocus = item.getIs_focus().equals("1") ? true : false;
            final boolean isFocus = false;
            followTv.setVisibility(View.GONE);
            if (isFocus) {
                //灰色
                followTv.setTextColor(Color.parseColor("#ACACAC"));
                followTv.setText("已关注");
                followTv.setBackground(getResources().getDrawable(R.drawable.attention_nor_bg));
            } else {
                //黄色
                followTv.setTextColor(Color.parseColor("#FFD71B"));
                followTv.setText("关注");
                followTv.setBackground(getResources().getDrawable(R.drawable.attention_pre_bg));

            }

            //添加or取消关注
            followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFocus) {
                        //取消关注操作
                    } else {
                        //添加关注操作
                    }
                }
            });

        }
    }


}
