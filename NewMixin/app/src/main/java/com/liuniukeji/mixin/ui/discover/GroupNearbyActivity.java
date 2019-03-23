package com.liuniukeji.mixin.ui.discover;

import android.content.Intent;
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
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 附近的组
 */
public class GroupNearbyActivity extends AppCompatActivity implements GroupNearbyContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    GroupNearbyContract.Presenter presenter;
    private NearbyAdapter mAdapter;
    private List<GroupNearbyInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;

    String lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_nearby);
        ButterKnife.bind(this);

        headTitleTv.setText("附近的组");

        presenter = new GroupNearbyPresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new NearbyAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);

        //获取列表数据
        lat=AccountUtils.getLocationInfo(this).getLat();
        lng=AccountUtils.getLocationInfo(this).getLng();

        presenter.getGroupNearbyList(p, lng, lat);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getGroupNearbyList(p, lng, lat);
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
                presenter.getGroupNearbyList(p, lng, lat);

            }
        }, rvList);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupNearbyInfo nearbyInfo = mAdapter.getItem(position);
                Intent intent = new Intent();

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("GroupNearbyInfo", nearbyInfo);
//                intent.putExtras(bundle);

                intent.putExtra("groupId", nearbyInfo.getId());
                intent.putExtra("groupImId",nearbyInfo.getIm_id());

                intent.setClass(GroupNearbyActivity.this, GroupDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void liftData(List<GroupNearbyInfo> infoList) {
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
        if (this.dataList != null && p == 1) {
            this.dataList.clear();
            mAdapter.setNewData(this.dataList);
            mAdapter.setEmptyView(R.layout.empty_layout);
        } else if (this.dataList != null && mAdapter != null && p > 1) {
            mAdapter.loadMoreEnd();
        }
    }

    class NearbyAdapter extends BaseQuickAdapter<GroupNearbyInfo, BaseViewHolder> {

        public NearbyAdapter(@Nullable List<GroupNearbyInfo> data) {
            super(R.layout.visitor_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, GroupNearbyInfo item) {

            helper.setText(R.id.name_tv, item.getName());
            helper.setText(R.id.signature_tv, item.getDescription());
            //这里复用访客的布局，本来显示日期的地方显示距离
            helper.setText(R.id.date_tv, item.getDistance());
            ImageView iv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(iv, item.getLogo());
            ImageView levelIconIv = helper.getView(R.id.level_icon_iv);
            TextView levelNumTv = helper.getView(R.id.level_tv);
        }
    }


}
