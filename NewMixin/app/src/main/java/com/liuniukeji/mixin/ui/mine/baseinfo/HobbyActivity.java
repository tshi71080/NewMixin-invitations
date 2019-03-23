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
 * 我的爱好
 * 修改功能
 */
public class HobbyActivity extends AppCompatActivity implements HobbyContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.hobby_content_et)
    EditText hobbyContentEt;
    @BindView(R.id.hobby_save_tv)
    TextView hobbySaveTv;

    HobbyContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);
        ButterKnife.bind(this);
        presenter = new HobbyPresenter(this);
        presenter.attachView(this);
        headTitleTv.setText("我的爱好");
        setData();
    }

    private void setData() {
        UserBean user = AccountUtils.getUser(this);
        hobbyContentEt.setText(user.getInterests());
    }

    @OnClick({R.id.head_back_ly, R.id.hobby_save_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.hobby_save_tv:
                String hobby = hobbyContentEt.getText().toString();
                if (EmptyUtils.isNotEmpty(hobby)) {
                    presenter.submit(hobby);
                } else {
                    ToastUtils.showShortToast("请输入兴趣爱好");
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
            //更新本地存储数据
            UserBean user = AccountUtils.getUser(this);
            user.updateInterests(info);
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
