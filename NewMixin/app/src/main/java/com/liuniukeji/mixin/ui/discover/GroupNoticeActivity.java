package com.liuniukeji.mixin.ui.discover;

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
import com.liuniukeji.mixin.net.CommonResult;
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
 * 群组通知
 */

public class GroupNoticeActivity extends AppCompatActivity implements GroupNoticeContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    GroupNoticeContract.Presenter presenter;

    private NoticeAdapter mAdapter;
    private List<GroupNoticeInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice);
        ButterKnife.bind(this);
        headTitleTv.setText("群组通知");

        presenter = new GroupNoticePresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new NoticeAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);

        //获取列表数据
        presenter.getNoticeList(p);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getNoticeList(p);
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
                presenter.getNoticeList(p);
            }
        }, rvList);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //详情主页跳转
//                Intent intent = new Intent();
//                intent.putExtra("userId", dataList.get(position).getId());
//                intent.setClass(GroupNoticeActivity.this, FriendProfileActivity.class);
//                startActivity(intent);

            }
        });

    }

    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void liftData(List<GroupNoticeInfo> infoList) {
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
    public void liftData(CommonResult commonResult) {
        if (null != commonResult) {
            String info = commonResult.getInfo();
            int status = commonResult.getStatus();
            if (EmptyUtils.isNotEmpty(info)) {
                ToastUtils.showShortToast(info);
            }
            if (status == 1) {
                //操作成功则刷新数据
                //获取列表数据
                p = 1;
                presenter.getNoticeList(p);
            }
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

    class NoticeAdapter extends BaseQuickAdapter<GroupNoticeInfo, BaseViewHolder> {

        public NoticeAdapter(@Nullable List<GroupNoticeInfo> data) {
            super(R.layout.group_notice_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, GroupNoticeInfo item) {

            ImageView iv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(iv, item.getPhoto_path());

            helper.setText(R.id.name_tv, item.getReal_name());

            helper.setText(R.id.content_tv, "申请加入群组：" + item.getName());
            final String id = item.getId();

            //同意
            TextView agreeTv = helper.getView(R.id.agree_tv);
            agreeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 1同意 2拒绝
                    presenter.auditMember(id, "1");
                }
            });

            //拒绝
            TextView refuseTv = helper.getView(R.id.refuse_tv);
            refuseTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.auditMember(id, "2");
                }
            });

        }
    }


}
