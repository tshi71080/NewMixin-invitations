package com.liuniukeji.mixin.ui.mine.setting;

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
 * 修改密码
 */
public class ChangePswActivity extends AppCompatActivity implements PasswordContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.original_psw_et)
    EditText originalPswEt;
    @BindView(R.id.new_psw_et)
    EditText newPswEt;
    @BindView(R.id.confirm_psw_et)
    EditText confirmPswEt;
    @BindView(R.id.change_psw_sure_tv)
    TextView changePswSureTv;

    PasswordContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        ButterKnife.bind(this);
        headTitleTv.setText("修改密码");

        presenter = new PasswordPresenter(this);
        presenter.attachView(this);
    }


    @OnClick({R.id.head_back_ly, R.id.change_psw_sure_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.change_psw_sure_tv:
                String originalPsw = originalPswEt.getText().toString();
                String newPsw = newPswEt.getText().toString();
                String confirmPsw = confirmPswEt.getText().toString();

                //输入为空&前后一致性校验
                if (EmptyUtils.isNotEmpty(originalPsw) && EmptyUtils.isNotEmpty(newPsw)
                        && EmptyUtils.isNotEmpty(confirmPsw)) {

                    if (newPsw.equals(confirmPsw)) {
                        //新密码前后确认一致
                        presenter.submit(originalPsw, newPsw, confirmPsw);
                    } else {
                        ToastUtils.showShortToast("新密码前后确认不一致");
                    }
                } else {
                    if (EmptyUtils.isEmpty(originalPsw)) {
                        ToastUtils.showShortToast("请输入原密码");
                    } else if (EmptyUtils.isEmpty(newPsw)) {
                        ToastUtils.showShortToast("请输入新密码");
                    } else {
                        ToastUtils.showShortToast("请输入确认新密码");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(String info) {
        ToastUtils.showShortToast(info);
        finish();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {
        ToastUtils.showShortToast("密码修改失败");

    }
}
