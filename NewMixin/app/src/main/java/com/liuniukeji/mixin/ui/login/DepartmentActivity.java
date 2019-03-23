package com.liuniukeji.mixin.ui.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.widget.PtrLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 院系列表
 */

public class DepartmentActivity extends AppCompatActivity implements DepartmentContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ptr)
    PtrLayout ptr;

    DepartmentContract.Presenter presenter;
    List<DepartmentBean> lists;

    DepartmentAdapter adapter;

    int p = 1;
    int mCurrentCounter = 0;
    String keyword;
    String school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        ButterKnife.bind(this);

        headTitleTv.setText("选择院系");
        presenter = new DepartmentPresenter(this);
        presenter.attachView(this);

        lists = new ArrayList<>();
        adapter = new DepartmentAdapter(lists);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter.bindToRecyclerView(rv);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(itemDecoration);

        school_id = getIntent().getStringExtra("school_id");

        if (EmptyUtils.isNotEmpty(school_id)) {
            // presenter.lists(p, school_id, keyword);
        } else {
//            ToastUtils.showShortToast("传递学校id参数为空");
            ToastUtils.showShortToast("请先选择学校信息");
            return;
        }

        //下拉刷新设置
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                p = 1;
                presenter.lists(p, school_id, keyword);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mCurrentCounter < Constants.COUNT_SIZE * p) {
                    adapter.loadMoreEnd();
                    return;
                }
                p++;
                presenter.lists(p, school_id, keyword);

            }
        }, rv);
        ptr.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptr.autoRefresh(true);
            }
        }, 100);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("DepartmentBean", lists.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void liftData(List<DepartmentBean> beans) {

        if (ptr != null) {
            ptr.refreshComplete();
        }
        if (lists == null || beans == null || beans.size() == 0) {
            adapter.loadMoreEnd();
            //adapter.setEmptyView(R.layout.empty_layout);
            return;
        }
        if (p == 1) {
            lists.clear();
            lists.addAll(beans);
        }
        adapter.setNewData(lists);
        mCurrentCounter = adapter.getData().size();
        adapter.loadMoreComplete();
    }

    @Override
    public void onEmpty() {
        if (ptr != null) {
            ptr.refreshComplete();
        }
        if (lists != null && adapter != null && p == 1) {
            lists.clear();
            adapter.setNewData(lists);
            //adapter.setEmptyView(R.layout.empty_layout);
        } else if (lists != null && adapter != null && p > 1) {
            adapter.loadMoreEnd();
        }
    }

    @Override
    public void onNetError() {
        if (ptr != null) {
            ptr.refreshComplete();
        }
        if (lists != null && adapter != null && p == 1) {
            lists.clear();
            adapter.setNewData(lists);
            //adapter.setEmptyView(R.layout.empty_layout);
        } else if (lists != null && adapter != null && p > 1) {
            adapter.loadMoreEnd();
        }
    }

    public class DepartmentAdapter extends BaseQuickAdapter<DepartmentBean, BaseViewHolder> {

        public DepartmentAdapter(List<DepartmentBean> data) {
            super(R.layout.school_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, DepartmentBean item) {
            helper.setText(R.id.tv_title, item.getName());
        }
    }

}
