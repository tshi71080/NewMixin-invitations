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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hyphenate.chatui.Constant;
import com.hyphenate.chatui.ui.ChatActivity;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.XImage;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 兴趣组
 */
public class InterestGroupActivity extends AppCompatActivity implements InterestGroupContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.right_ly)
    LinearLayout rightLy;
    @BindView(R.id.create_group_rl)
    RelativeLayout createGroupRl;
    @BindView(R.id.group_notice_rl)
    RelativeLayout groupNoticeRl;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    InterestGroupContract.Presenter presenter;
    @BindView(R.id.red_dot_tv)
    TextView redDotTv;

    private InterestGroupAdapter mAdapter;
    private List<InterestGroupInfo> dataList;

    int p = 1;
    int mCurrentCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_group);
        ButterKnife.bind(this);
        headTitleTv.setText("群组");

        presenter = new InterestGroupPresenter(this);
        presenter.attachView(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvList.setLayoutManager(manager);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        dataList = new ArrayList<>();
        mAdapter = new InterestGroupAdapter(dataList);
        mAdapter.bindToRecyclerView(rvList);
        mAdapter.setAutoLoadMoreSize(1);
        mAdapter.disableLoadMoreIfNotFullPage(rvList);
        swipeLayout.setEnabled(true);
        mAdapter.setEnableLoadMore(true);

        //获取通知数量
        presenter.getGroupNoticeNum();

        //获取列表数据
        presenter.getInterestGroupList(p);

        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                presenter.getInterestGroupList(p);
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
                presenter.getInterestGroupList(p);

            }
        }, rvList);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到群聊页面
                String imId = dataList.get(position).getIm_id();
                if (EmptyUtils.isNotEmpty(imId)) {
                    Intent intent = new Intent(InterestGroupActivity.this, ChatActivity.class);
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                    intent.putExtra(Constant.EXTRA_USER_ID, imId);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast("群组参数错误，无法跳转");
                }
            }
        });


        EventBus.getDefault().register(this);

    }

    @OnClick({R.id.head_back_ly, R.id.right_ly, R.id.create_group_rl, R.id.group_notice_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.right_ly:
                //搜索
                startActivity(new Intent().setClass(
                        InterestGroupActivity.this, SearchGroupActivity.class));
                break;
            case R.id.create_group_rl:
                //新建群组
                startActivity(new Intent().setClass(
                        InterestGroupActivity.this, CreateGroupActivity.class));
                break;
            case R.id.group_notice_rl:
                //群组通知
                startActivity(new Intent().setClass(
                        InterestGroupActivity.this, GroupNoticeActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getGroupNoticeNum();
    }

    @Override
    public void liftData(List<InterestGroupInfo> infoList) {
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
    public void liftData(String noticeNum) {
        //返回通知数量
        if (EmptyUtils.isNotEmpty(noticeNum) && !noticeNum.equals("0")) {
            redDotTv.setVisibility(View.VISIBLE);
            redDotTv.setText(noticeNum);
        } else {
            redDotTv.setVisibility(View.GONE);
        }
        //ToastUtils.showShortToast("返回了数量："+noticeNum);
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


    class InterestGroupAdapter extends BaseQuickAdapter<InterestGroupInfo, BaseViewHolder> {

        public InterestGroupAdapter(@Nullable List<InterestGroupInfo> data) {
            super(R.layout.interest_group_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, InterestGroupInfo item) {

            helper.setText(R.id.name_tv, item.getName());
            helper.setText(R.id.signature_tv, item.getDescription());
            ImageView iv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(iv, item.getLogo());

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.GROUP_INFO info) {

        if (info == Constants.GROUP_INFO.CREATE) {
            //刷新数据
            presenter.getInterestGroupList(1);
        }
    }


}
