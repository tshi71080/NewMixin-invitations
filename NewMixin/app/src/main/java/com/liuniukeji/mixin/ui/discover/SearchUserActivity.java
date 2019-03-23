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
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索用户结果
 */

public class SearchUserActivity extends AppCompatActivity implements SearchUserContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    SearchUserContract.Presenter presenter;

    private FriendAdapter mAdapter;
    private List<SearchUser> dataList;

    int p = 1;
    int mCurrentCounter = 0;

    String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);

        headTitleTv.setText("搜索结果");

        presenter = new SearchUserPresenter(this);
        presenter.attachView(this);

        keyword = getIntent().getStringExtra("keyword");

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

        //获取列表数据
        presenter.getSearchUser(p, keyword);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getSearchUser(p, keyword);

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
                presenter.getSearchUser(p, keyword);


            }
        }, rvList);


        //点击事件处理
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("userId", dataList.get(position).getId());
                intent.setClass(SearchUserActivity.this, FriendProfileActivity.class);
                startActivity(intent);
            }
        });

    }


    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void liftData(List<SearchUser> infoList) {
        swipeLayout.setRefreshing(false);
        if (null != infoList && infoList.size() > 0) {
            dataList.clear();
            dataList.addAll(infoList);
            mAdapter.setNewData(dataList);
        } else {
            mAdapter.setEmptyView(R.layout.empty_layout);
        }
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
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


    class FriendAdapter extends BaseQuickAdapter<SearchUser, BaseViewHolder> {

        public FriendAdapter(@Nullable List<SearchUser> data) {
            super(R.layout.phone_friend_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final SearchUser item) {

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
