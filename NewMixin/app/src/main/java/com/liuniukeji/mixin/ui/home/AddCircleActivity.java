package com.liuniukeji.mixin.ui.home;

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
import com.liuniukeji.mixin.ui.login.SchoolBean;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.ui.login.SchoolBean;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加圈子（学校）
 */
public class AddCircleActivity extends AppCompatActivity implements
        PullLoadMoreRecyclerView.PullLoadMoreListener, AddSchoolContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.head_right_ly)
    LinearLayout headRightLy;
    @BindView(R.id.school_search_et)
    EditText schoolSearchEt;
    @BindView(R.id.school_search_iv)
    ImageView schoolSearchIv;
    @BindView(R.id.school_search_ly)
    LinearLayout schoolSearchLy;
    @BindView(R.id.recyclerView)
    PullLoadMoreRecyclerView recyclerView;

    AddSchoolContract.Presenter presenter;
    SchoolAdapter adapter;
    int p = 1;
    String keyword;
    List<SchoolBean> list;
    @BindView(R.id.root_ly)
    LinearLayout rootLy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_circle);
        ButterKnife.bind(this);

        headTitleTv.setText("添加圈子");

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
        presenter = new AddSchoolPresenter(this);
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

            }
        });

    }

    /**
     * 搜索
     */
    private void goSearch() {
        keyword = schoolSearchEt.getText().toString();
        if (EmptyUtils.isNotEmpty(keyword)) {
            //清空数据
            list.clear();
            presenter.lists(1, keyword);
        } else {
            ToastUtils.showShortToast("请输入搜索内容");
        }
    }


    @OnClick({R.id.head_back_ly, R.id.head_right_ly, R.id.school_search_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                //返回
                finish();
                closeKeyboard();
                break;
            case R.id.head_right_ly:
                //我的圈子
                startActivity(new Intent().setClass(this, MyCircleActivity.class));
                break;
            case R.id.school_search_ly:
                //搜索学校
                goSearch();
                break;
            default:
                break;
        }
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
    public void liftData(String info) {
        ToastUtils.showShortToast(info);
        EventBus.getDefault().post(Constants.ADD_CIRCLE.ON_CHANGE);
        finish();
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
            super(R.layout.add_school_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, final SchoolBean item) {
            helper.setText(R.id.tv_title, item.getName());
            TextView followTv = helper.getView(R.id.tv_follow);
            followTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //加关注
                    presenter.follow(item.getId(), "1");

                }
            });
        }
    }

    /**
     * 隐藏软键盘
     */
    private void closeKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootLy.getWindowToken(), 0);
    }


}
