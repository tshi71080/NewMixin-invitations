package com.liuniukeji.mixin.ui.message;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.ui.discover.CreateGroupActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 分组列表
 */

public class GroupListActivity extends AppCompatActivity implements GroupListContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.head_add_ly)
    LinearLayout head_add_ly;

    GroupListContract.Presenter presenter;

    private GroupAdapter mAdapter;
    private List<GroupInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ButterKnife.bind(this);
        headTitleTv.setText("分组列表");

        presenter = new GroupListPresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new GroupAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);

        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        //数据一次返回不需要加载更多
        mAdapter.setEnableLoadMore(false);
        //新建好友分组按钮
        head_add_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(GroupListActivity.this, CreateGroupActivity.class));
            }
        });

        //获取列表数据
        presenter.getGroupList();

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                presenter.getGroupList();
            }
        });
        //数据一次返回不需要加载更多
        //--------------------------------------------
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("type", 2);
                intent.putExtra("id", dataList.get(position).getId());
                intent.putExtra("name", dataList.get(position).getName());
                intent.putExtra("color", dataList.get(position).getColor());
                intent.setClass(GroupListActivity.this, EditGroupActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void liftGroupData(List<GroupInfo> infoList) {
        swipeLayout.setRefreshing(false);
        if (null != infoList && infoList.size() > 0) {
            dataList.clear();
            dataList.addAll(infoList);
            mAdapter.setNewData(dataList);
        } else {
            mAdapter.setEmptyView(R.layout.empty_layout);
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

    class GroupAdapter extends BaseQuickAdapter<GroupInfo, BaseViewHolder> {

        public GroupAdapter(@Nullable List<GroupInfo> data) {
            super(R.layout.group_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, GroupInfo item) {

            TextView colorTv = helper.getView(R.id.tv_group_color);
            TextView nameTv = helper.getView(R.id.tv_group_name);

            //设置分组颜色
            if (EmptyUtils.isNotEmpty(item.getColor())) {
                colorTv.setBackgroundColor(Color.parseColor(item.getColor()));
            }
            //设置分组名称
            nameTv.setText(item.getName());

        }
    }


}
