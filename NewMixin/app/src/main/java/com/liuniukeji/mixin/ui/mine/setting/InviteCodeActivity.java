package com.liuniukeji.mixin.ui.mine.setting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.CommonResult;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 填写邀请码
 */
public class InviteCodeActivity extends AppCompatActivity implements InviteCodeContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.code_et)
    EditText codeEt;
    @BindView(R.id.code_save_tv)
    TextView codeSaveTv;
    InviteCodeContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_code);
        ButterKnife.bind(this);
        presenter = new InviteCodePresenter(this);
        presenter.attachView(this);
        headTitleTv.setText("填写邀请码");
    }

    @OnClick({R.id.head_back_ly, R.id.code_save_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.code_save_tv:
                String code = codeEt.getText().toString();
                if (EmptyUtils.isNotEmpty(code)) {
                    presenter.submit(code);
                } else {
                    ToastUtils.showShortToast("请填写邀请码");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(CommonResult result) {
        if (result.getStatus() == 1) {
            //填写成功，通知刷新数据
            EventBus.getDefault().post(Constants.USER_INFO.UPDATE);
        }

        if (EmptyUtils.isNotEmpty(result.getInfo())) {
            ToastUtils.showShortToast(result.getInfo());
            finish();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
}
