package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
 * 搜索兴趣组（群组）
 */

public class SearchGroupActivity extends AppCompatActivity implements SearchGroupContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.search_content_et)
    EditText searchContentEt;
    @BindView(R.id.search_rl)
    RelativeLayout searchRl;

    private GroupAdapter mAdapter;
    private List<SearchGroupInfo> dataList;

    SearchGroupContract.Presenter presenter;
    int p = 1;
    int mCurrentCounter = 0;

    String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);
        ButterKnife.bind(this);

        headTitleTv.setText("搜索群组");

        presenter = new SearchGroupPresenter(this);
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
        mAdapter.setEnableLoadMore(true);


        //设置下拉刷新监听
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                p = 1;
                goSearch();
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
                goSearch();

            }
        }, rvList);

        //点击事件处理
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //进入群详情
                Intent intent = new Intent();
                intent.putExtra("groupId", dataList.get(position).getId());
                intent.putExtra("groupImId", dataList.get(position).getIm_id());
                intent.setClass(SearchGroupActivity.this, GroupDetailActivity.class);
                startActivity(intent);
            }
        });

        //软键盘搜索监听
        searchContentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    p = 1;
                    goSearch();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(searchContentEt.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 搜索
     */
    private void goSearch() {
        keyword = searchContentEt.getText().toString();
        if (EmptyUtils.isNotEmpty(keyword)) {
            //获取列表数据
            presenter.searchGroup(keyword, p);
        } else {
            ToastUtils.showShortToast("搜索内容不能为空");
        }
    }

    @Override
    public void liftData(List<SearchGroupInfo> infoList) {
        if (null != swipeLayout) {
            swipeLayout.setRefreshing(false);
        }
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
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void onNetError() {
        swipeLayout.setRefreshing(false);
        mAdapter.setEmptyView(R.layout.empty_layout);
        if(null!=mAdapter){
            mAdapter.loadMoreEnd();
        }
    }

    @OnClick({R.id.head_back_ly, R.id.search_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.search_rl:
                //搜索
                p = 1;
                goSearch();
                break;
            default:
                break;
        }
    }

    class GroupAdapter extends BaseQuickAdapter<SearchGroupInfo, BaseViewHolder> {

        public GroupAdapter(@Nullable List<SearchGroupInfo> data) {
            super(R.layout.search_group_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final SearchGroupInfo item) {

            //设置名称
            helper.setText(R.id.name_tv, item.getName());
            //TextView nameTv = helper.getView(R.id.name_tv);

            //显示头像
            ImageView avatarIv = helper.getView(R.id.avatar_iv);
            XImage.loadAvatar(avatarIv, item.getLogo());

            //群简介
            TextView signatureTv = helper.getView(R.id.intro_tv);
            signatureTv.setText(item.getDescription());

        }
    }

}
