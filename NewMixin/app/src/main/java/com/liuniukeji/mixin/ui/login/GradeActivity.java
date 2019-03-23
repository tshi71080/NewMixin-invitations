package com.liuniukeji.mixin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liuniukeji.mixin.R;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 年级列表
 */
public class GradeActivity extends AppCompatActivity implements
        PullLoadMoreRecyclerView.PullLoadMoreListener, GradeContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.recyclerView)
    PullLoadMoreRecyclerView recyclerView;

    List<GradeBean> list;
    GradeAdapter adapter;

    GradeContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        ButterKnife.bind(this);

        //设置标题
        headTitleTv.setText("选择年级");

        presenter = new GradePresenter(this);
        presenter.attachView(this);


        //设置是否可以下拉刷新
        recyclerView.setPullRefreshEnable(false);
        recyclerView.setPushRefreshEnable(false);
        recyclerView.setFooterViewText("拼命加载中");
        recyclerView.setOnPullLoadMoreListener(null);
        recyclerView.setLinearLayout();
        //添加默认分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        //设置刷新图标颜色
        recyclerView.setColorSchemeResources(R.color.colorAccent);
        list = new ArrayList<>();
        adapter = new GradeAdapter(list);
        recyclerView.setAdapter(adapter);

        presenter.lists();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("GradeBean", list.get(position));
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
    public void liftData(List<GradeBean> beans) {
        //if (p == 1) {
            list.clear();
       // }
        list.addAll(beans);
        adapter.notifyDataSetChanged();
        if (null != recyclerView) {
            recyclerView.setPullLoadMoreCompleted();
        }
    }

    @Override
    public void onEmpty() {
        if (null != recyclerView) {
            recyclerView.setPullLoadMoreCompleted();
        }
    }

    @Override
    public void onNetError() {
        if (null != recyclerView) {
            recyclerView.setPullLoadMoreCompleted();
        }
    }

    @Override
    public void onRefresh() {
        presenter.lists();
    }

    @Override
    public void onLoadMore() {

    }

    public class GradeAdapter extends BaseQuickAdapter<GradeBean, BaseViewHolder> {

        public GradeAdapter(List<GradeBean> data) {
            super(R.layout.school_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, GradeBean item) {
            helper.setText(R.id.tv_title, item.getValue());
        }
    }

}
