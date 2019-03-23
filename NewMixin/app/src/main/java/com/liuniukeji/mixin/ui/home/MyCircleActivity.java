package com.liuniukeji.mixin.ui.home;

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
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的圈子
 */
public class MyCircleActivity extends AppCompatActivity implements MyCircleContract.View{

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    MyCircleContract.Presenter presenter;

    private SchoolAdapter mAdapter;
    private List<FollowSchoolInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        ButterKnife.bind(this);
        headTitleTv.setText("我的圈子");

        presenter=new MyCirclePresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new SchoolAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);


        //获取列表数据
        presenter.getSchoolList(p);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getSchoolList(p);
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
                presenter.getSchoolList(p);
            }
        }, rvList);

    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftSchoolData(List<FollowSchoolInfo> infoList) {
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
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void liftData(String info) {
        ToastUtils.showShortToast(info);
        //刷新数据
        presenter.getSchoolList(p);
        EventBus.getDefault().post(Constants.ADD_CIRCLE.ON_CHANGE);

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


    class SchoolAdapter extends BaseQuickAdapter<FollowSchoolInfo, BaseViewHolder> {

        public SchoolAdapter(@Nullable List<FollowSchoolInfo> data) {
            super(R.layout.my_circle_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final FollowSchoolInfo item) {

            helper.setText(R.id.tv_title, item.getName());
            TextView tv = helper.getView(R.id.tv_remove);

            //取消关注
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.cancelFollow(item.getId(),"2");
                }
            });

        }
    }



}
