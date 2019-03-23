package com.liuniukeji.mixin.ui.mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请自媒体
 */
public class SelfMediaActivity extends AppCompatActivity {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_media);
        ButterKnife.bind(this);
        headTitleTv.setText("申请自媒体");

    }


    @OnClick(R.id.head_back_ly)
    public void onViewClicked() {
        finish();
    }
}
