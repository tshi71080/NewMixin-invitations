package com.liuniukeji.mixin.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 学分明细
 */

public class ScoreDetailActivity extends AppCompatActivity implements ScoreDetailContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    private ScoreDetailAdapter mAdapter;
    private List<ScoreDetail.ScoreDetailBean> dataList;

    int p = 1;

    ScoreDetailContract.Presenter presenter;
    int mCurrentCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);
        ButterKnife.bind(this);
        headTitleTv.setText("学分明细");

        presenter = new ScoreDetailPresenter(this);
        presenter.attachView(this);

        //swipeLayout.setColorSchemeColors(Color.rgb(255, 198, 65));

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new ScoreDetailAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);


        //获取列表数据
        presenter.getScoreDetailList();

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getScoreDetailList();
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
                presenter.getScoreDetailList();
            }
        }, rvList);


    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(ScoreDetail scoreDetail) {
        if (this.dataList == null || scoreDetail.getList() == null || scoreDetail.getList().size() == 0) {
            if (p == 1) {
                mAdapter.setEmptyView(R.layout.empty_layout);
                swipeLayout.setRefreshing(false);
            }
            return;
        }
        if (p == 1) {
            swipeLayout.setRefreshing(false);
            this.dataList.clear();
            this.dataList.addAll(scoreDetail.getList());
            mAdapter.setNewData(this.dataList);
            mCurrentCounter = mAdapter.getData().size();
        }
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }

    @Override
    public void onRefresh() {
        p = 1;
        presenter.getScoreDetailList();
    }


    class ScoreDetailAdapter extends BaseQuickAdapter<ScoreDetail.ScoreDetailBean, BaseViewHolder> {

        public ScoreDetailAdapter(@Nullable List<ScoreDetail.ScoreDetailBean> data) {
            super(R.layout.score_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ScoreDetail.ScoreDetailBean item) {
            //描述
            helper.setText(R.id.title_tv, item.getDesc());
            //日期
            helper.setText(R.id.date_tv, item.getTime());
            //变动学分
            helper.setText(R.id.score_tv, item.getChange_num());
        }
    }


}
