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
 * 群组名称设置
 */
public class GroupNameActivity extends AppCompatActivity {


    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.group_name_et)
    EditText groupNameEt;
    @BindView(R.id.ok_btn)
    TextView okBtn;
    String nameValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);
        ButterKnife.bind(this);
        headTitleTv.setText("群名称");
        nameValue=getIntent().getStringExtra("nameValue");
        if(EmptyUtils.isNotEmpty(nameValue)){
            groupNameEt.setText(nameValue);
        }
    }

    @OnClick({R.id.head_back_ly, R.id.ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.ok_btn:
                String name = groupNameEt.getText().toString();
                if (EmptyUtils.isNotEmpty(name)) {
                    Intent intent = new Intent();
                    intent.putExtra("groupName", name);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showShortToast("请填写群组名称");
                }
                break;
            default:
                break;
        }
    }
}
