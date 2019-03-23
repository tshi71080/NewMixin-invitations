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
import com.liuniukeji.mixin.ui.login.UserBean;
import com.liuniukeji.mixin.util.AccountUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的地址
 * 修改功能
 */

public class AddressActivity extends AppCompatActivity implements AddressContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.address_content_et)
    EditText addressContentEt;
    @BindView(R.id.address_save_tv)
    TextView addressSaveTv;
    AddressContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        headTitleTv.setText("我的地址");
        presenter = new AddressPresenter(this);
        presenter.attachView(this);
        setData();
    }

    /**
     * 页面控件赋值
     */
    private void setData() {
        UserBean user = AccountUtils.getUser(this);
        String address = user.getAddress();
        addressContentEt.setText(address);
    }

    @OnClick({R.id.head_back_ly, R.id.address_save_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.address_save_tv:
                String address = addressContentEt.getText().toString();
                if (EmptyUtils.isNotEmpty(address)) {
                    //保存数据
                    presenter.submit(address);
                } else {
                    ToastUtils.showShortToast("请填写地址");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void liftData(CommonInfo info) {
        if (null != info) {
            ToastUtils.showShortToast("修改地址成功");
            //更新本地存储数据
            UserBean user = AccountUtils.getUser(this);
            user.updatAddress(info);
            //重新保存数据
            AccountUtils.saveUserCache(this, user);
            //通知其他页面刷新相关数据
            EventBus.getDefault().post(Constants.USER_INFO.UPDATE);
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
