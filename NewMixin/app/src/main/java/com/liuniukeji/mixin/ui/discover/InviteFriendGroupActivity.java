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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.ui.home.FriendProfileActivity;
import com.liuniukeji.mixin.ui.mine.AttentionInfo;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.CommonResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 邀请朋友进群
 */
public class InviteFriendGroupActivity extends AppCompatActivity implements
        InviteFriendGroupContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    InviteFriendGroupContract.Presenter presenter;

    private AttentionAdapter mAdapter;
    private List<AttentionInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;

    String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        ButterKnife.bind(this);

        headTitleTv.setText("邀请好友");
        presenter = new InviteFriendGroupPresenter(this);
        presenter.attachView(this);


        groupId = getIntent().getStringExtra("groupId");

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new AttentionAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);


        //获取列表数据
        presenter.getAttentionList(p);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getAttentionList(p);
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
                presenter.getAttentionList(p);
            }
        }, rvList);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //好友详情主页跳转
                Intent intent = new Intent();
                intent.putExtra("userId", dataList.get(position).getId());
                intent.setClass(InviteFriendGroupActivity.this, FriendProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(List<AttentionInfo> infoList) {

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
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        }
        if (null != mAdapter) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
       public void liftData(CommonResult result) {
        if (result.getStatus() == 1) {
            //邀请成功，结束页面返回
            ToastUtils.showShortToast(result.getInfo());
            finish();
        }
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


    class AttentionAdapter extends BaseQuickAdapter<AttentionInfo, BaseViewHolder> {

        public AttentionAdapter(@Nullable List<AttentionInfo> data) {
            super(R.layout.invite_friend_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final AttentionInfo item) {

            helper.setText(R.id.name_tv, item.getReal_name());
            TextView nameTv = helper.getView(R.id.name_tv);

            helper.setText(R.id.signature_tv, item.getSignature());
            helper.setText(R.id.level_tv, item.getExperience());

            ImageView iv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(iv, item.getPhoto_path());
            ImageView levelIconIv = helper.getView(R.id.level_icon_iv);
            TextView levelNumTv = helper.getView(R.id.level_tv);

            //邀请好友
            TextView inviteTv = helper.getView(R.id.invite_tv);

            inviteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //邀请好友进群操作
                    presenter.inviteFriend(item.getId(), groupId);
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

        }
    }


}
