package com.liuniukeji.mixin.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 群组密码设置
 */
public class GroupPswActivity extends AppCompatActivity {


    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.group_psw_et)
    EditText groupPswEt;
    @BindView(R.id.confirm_group_psw_et)
    EditText confirmGroupPswEt;
    @BindView(R.id.open_group_psw_sw)
    Switch openGroupPswSw;
    @BindView(R.id.ok_btn)
    TextView okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_psw);
        ButterKnife.bind(this);
        headTitleTv.setText("群密码");
    }


    @OnClick({R.id.head_back_ly, R.id.ok_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.ok_btn:
                String psw = groupPswEt.getText().toString();
                String confirmPsw = confirmGroupPswEt.getText().toString();
                //是否开启群密码: 0不开启 1开启;
                String isPsw = openGroupPswSw.isChecked() ? "1" : "0";
                if (EmptyUtils.isNotEmpty(psw)) {
                    if (EmptyUtils.isNotEmpty(confirmPsw) && psw.equals(confirmPsw)) {
                        Intent intent = new Intent();
                        intent.putExtra("groupPsw", psw);
                        intent.putExtra("isPsw", isPsw);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ToastUtils.showShortToast("请确认输入的密码前后一致");
                    }
                } else {
                    ToastUtils.showShortToast("请设置群密码");
                }
                break;
            default:
                break;
        }
    }
}
