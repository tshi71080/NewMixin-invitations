package com.liuniukeji.mixin.ui.mine;

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
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 黑名单
 */
public class BlackListActivity extends AppCompatActivity implements BlackListContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    BlackListContract.Presenter presenter;

    private BlackAdapter mAdapter;
    private List<BlackInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        ButterKnife.bind(this);
        headTitleTv.setText("黑名单");

        presenter = new BlackListPresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new BlackAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);


        //获取列表数据
        presenter.getBlackList(p);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getBlackList(p);
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
                presenter.getBlackList(p);
            }
        }, rvList);

    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(List<BlackInfo> blackInfoList) {
        if(null!=blackInfoList&&blackInfoList.size()>0){
            swipeLayout.setRefreshing(false);
            dataList.clear();
            dataList.addAll(blackInfoList);
            mAdapter.notifyDataSetChanged();
            mCurrentCounter = mAdapter.getData().size();
            //Log.e("BBBBB","返回数据1");
        }else{
            dataList.clear();
            mAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
            mAdapter.setEmptyView(R.layout.empty_layout);
            //Log.e("BBBBB","返回数据2");

        }
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void liftData(String info) {
        if (EmptyUtils.isNotEmpty(info)) {
            ToastUtils.showShortToast(info);
            //重新加载列表数据
            //Log.e("BBBBB","返回数据3");
            p=1;
            presenter.getBlackList(p);
        }
    }

    @Override
    public void onEmpty() {
        mAdapter.loadMoreEnd();
        mAdapter.setEmptyView(R.layout.empty_layout);
    }

    @Override
    public void onNetError() {
        mAdapter.loadMoreEnd();
        mAdapter.setEmptyView(R.layout.empty_layout);
    }


    class BlackAdapter extends BaseQuickAdapter<BlackInfo, BaseViewHolder> {

        public BlackAdapter(@Nullable List<BlackInfo> data) {
            super(R.layout.black_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BlackInfo item) {

            helper.setText(R.id.name_tv, item.getReal_name());
            ImageView iv = helper.getView(R.id.avatar_iv);
            TextView tv = helper.getView(R.id.remove_tv);
            XImage.loadAvatar(iv, item.getPhoto_path());

            final String id=item.getTo_member_id();

            //移除黑名单
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToastUtils.showShortToast("移除");
                    presenter.cancelBlack(id,"cancel");
                }
            });

        }
    }


}
