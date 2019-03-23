package com.liuniukeji.mixin.ui.mine.baseinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的昵称
 * (修改功能)
 */
public class ChangeNicknameActivity extends AppCompatActivity implements SignatureContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.signature_content_et)
    EditText signatureContentEt;
    @BindView(R.id.signature_save_tv)
    TextView signatureSaveTv;

    SignatureContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        ButterKnife.bind(this);
        headTitleTv.setText("我的昵称");
        presenter = new SignaturePresenter(this);
        presenter.attachView(this);
        signatureContentEt.setHint("请输入昵称");
        //赋值
        signatureContentEt.setText(AccountUtils.getUser(this).getReal_name());
    }

    @OnClick({R.id.head_back_ly, R.id.signature_save_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.signature_save_tv:
                //保存数据
                String content = signatureContentEt.getText().toString();
                if (EmptyUtils.isNotEmpty(content)) {
                    presenter.changeNickname(content);
                } else {
                    ToastUtils.showShortToast("请填写昵称");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(CommonInfo info) {
        if (null != info) {
            ToastUtils.showShortToast("修改成功");
            //更新本地存储数据，签名
            UserBean user = AccountUtils.getUser(this);
            user.setReal_name(info.getReal_name());
            //重新保存数据
            AccountUtils.saveUserCache(this, user);
            //通知其他页面刷新相关数据
            EventBus.getDefault().post(Constants.USER_INFO.UPDATE);
            finish();
        } else {
            ToastUtils.showShortToast("修改失败");
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }
}
