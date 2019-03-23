package com.liuniukeji.mixin.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群组简介设置
 */
public class GroupIntroActivity extends AppCompatActivity {


    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.group_intro_et)
    EditText groupIntroEt;
    @BindView(R.id.ok_btn)
    TextView okBtn;
    String introValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_intro);
        ButterKnife.bind(this);
        headTitleTv.setText("群简介");
        introValue=getIntent().getStringExtra("introValue");
        if(EmptyUtils.isNotEmpty(introValue)){
            groupIntroEt.setText(introValue);
        }
    }


    @OnClick({R.id.head_back_ly, R.id.ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.ok_btn:
                String intro = groupIntroEt.getText().toString();
                if (EmptyUtils.isNotEmpty(intro)) {
                    Intent intent = new Intent();
                    intent.putExtra("groupIntro", intro);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showShortToast("请填写群组简介");
                }
                break;
            default:
                break;
        }
    }
}
