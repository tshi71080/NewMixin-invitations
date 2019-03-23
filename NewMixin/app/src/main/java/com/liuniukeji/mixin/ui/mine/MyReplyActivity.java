package com.liuniukeji.mixin.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的回复
 */
public class MyReplyActivity extends AppCompatActivity implements MyReplyContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    MyReplyContract.Presenter presenter;

    private ReplyAdapter mAdapter;
    private List<ReplyInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reply);
        ButterKnife.bind(this);
        headTitleTv.setText("我的回复");

        presenter = new MyReplyPresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        //rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new ReplyAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(false);


        //获取列表数据
        presenter.getReplyList(p);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getReplyList(p);
            }
        });

        //设置加载更多监听
//        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                if (mCurrentCounter < Constants.COUNT_SIZE * p) {
//                    mAdapter.loadMoreEnd();
//                    return;
//                }
//                p++;
//                presenter.getReplyList(p);
//            }
//        }, rvList);

    }


    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(List<ReplyInfo> infoList) {
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

    class ReplyAdapter extends BaseQuickAdapter<ReplyInfo, BaseViewHolder> {

        public ReplyAdapter(@Nullable List<ReplyInfo> data) {
            super(R.layout.reply_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ReplyInfo item) {

            helper.setText(R.id.day_tv, item.getD());
            helper.setText(R.id.month_tv, item.getM());
            helper.setText(R.id.time_tv, item.getH());
            ImageView iv = helper.getView(R.id.tweet_pic_iv);
            if(EmptyUtils.isNotEmpty(item.getCover_path())){
                XImage.loadImage(iv, item.getCover_path());
                iv.setVisibility(View.VISIBLE);
            }else{
                iv.setVisibility(View.GONE);
            }

            helper.setText(R.id.tweet_content_tv, item.getContent());
            helper.setText(R.id.tweet_comment_tv, "我："+item.getComment_content());

        }
    }


}
