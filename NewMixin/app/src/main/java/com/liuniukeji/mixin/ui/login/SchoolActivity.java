package com.liuniukeji.mixin.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
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
 * 学校列表
 */

public class SchoolActivity extends AppCompatActivity implements
        PullLoadMoreRecyclerView.PullLoadMoreListener, SchoolContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.recyclerView)
    PullLoadMoreRecyclerView recyclerView;
    @BindView(R.id.school_search_et)
    EditText schoolSearchEt;
    @BindView(R.id.school_search_iv)
    ImageView schoolSearchIv;

    SchoolContract.Presenter presenter;
    SchoolAdapter adapter;
    int p = 1;
    String keyword;
    List<SchoolBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        ButterKnife.bind(this);
        //设置标题
        headTitleTv.setText("选择学校");

        //设置是否可以下拉刷新
        recyclerView.setPullRefreshEnable(true);
        recyclerView.setPushRefreshEnable(true);
        recyclerView.setFooterViewText("拼命加载中");
        recyclerView.setOnPullLoadMoreListener(this);
        recyclerView.setLinearLayout();
        //添加默认分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        //设置刷新图标颜色
        recyclerView.setColorSchemeResources(R.color.colorAccent);
        list = new ArrayList<>();
        adapter = new SchoolAdapter(list);
        recyclerView.setAdapter(adapter);
        presenter = new SchoolPresenter(this);
        presenter.attachView(this);

        //请求数据
        presenter.lists(1, keyword);
        //软键盘搜索监听
        schoolSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    goSearch();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(schoolSearchEt.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("SchoolBean", list.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @OnClick({R.id.head_back_ly, R.id.school_search_iv, R.id.school_search_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.school_search_iv:
            case R.id.school_search_ly:
                //搜索
                goSearch();
                break;
            default:
                break;
        }
    }

    private void goSearch() {
        //清空数据
        list.clear();
        keyword = schoolSearchEt.getText().toString();
        presenter.lists(1, keyword);
    }

    @Override
    public void liftData(List<SchoolBean> beans) {
        if (p == 1) {
            list.clear();
        }
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
//        if(null!=adapter){
//            adapter.setEmptyView();
//        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        keyword = schoolSearchEt.getText().toString();
        presenter.lists(p, keyword);
    }

    @Override
    public void onLoadMore() {
        p++;
        presenter.lists(p, keyword);
    }

    public class SchoolAdapter extends BaseQuickAdapter<SchoolBean, BaseViewHolder> {

        public SchoolAdapter(List<SchoolBean> data) {
            super(R.layout.school_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SchoolBean item) {
            helper.setText(R.id.tv_title, item.getName());
        }

    }
}
