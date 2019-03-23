package com.liuniukeji.mixin.ui.areacode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.base.BaseActivity;
import com.liuniukeji.mixin.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AreaCodeActivity extends BaseActivity implements AreaCodeContract.View {

    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.area_code_recycle)
    RecyclerView areaCodeRecycle;

    private List<AreaCodeBean> areaCodeBeanList = new ArrayList<>();

    private AreaCodeAdapter areaCodeAdapter;

    private AreaCodeContract.Presenter presenter;

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_area_code);
        ButterKnife.bind(this);
    }

    @Override
    protected void processLogic() {
        headTitleTv.setText("选择国家/地区");

        presenter = new AreaCodePresenter(this);
        presenter.attachView(this);

        areaCodeRecycle.setLayoutManager(new LinearLayoutManager(this));
        areaCodeAdapter = new AreaCodeAdapter(areaCodeBeanList);
        areaCodeAdapter.bindToRecyclerView(areaCodeRecycle);
        areaCodeRecycle.setAdapter(areaCodeAdapter);
        presenter.getAreacodeList();
        areaCodeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(Constants.ARECODE,areaCodeBeanList.get(position).getCode());
                setResult(Constants.GET_AREACODE_RESULTCODE,intent);
                finish();
            }
        });
    }

    @OnClick({R.id.head_back_ly})
    public void onViewClicked(){
        finish();
    }

    @Override
    public void showAreacodeList(List<AreaCodeBean> beans) {
        areaCodeBeanList.clear();
        areaCodeBeanList.addAll(beans);
        areaCodeAdapter.setNewData(areaCodeBeanList);
        if(areaCodeBeanList.size()==0){
            areaCodeAdapter.setEmptyView(R.layout.empty_layout);
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
}
