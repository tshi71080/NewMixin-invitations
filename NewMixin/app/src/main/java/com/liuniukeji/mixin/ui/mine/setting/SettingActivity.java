package com.liuniukeji.mixin.ui.mine.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.login.LoginActivity;
import com.liuniukeji.mixin.util.ACache;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.FileCacheUtils;
import com.liuniukeji.mixin.util.currency.AppUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.widget.WebViewActivity;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.login.LoginActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 设置
 */

public class SettingActivity extends AppCompatActivity implements SettingContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.setting_change_psw_rl)
    RelativeLayout settingChangePswRl;
    @BindView(R.id.setting_change_phone_rl)
    RelativeLayout settingChangePhoneRl;
    @BindView(R.id.real_name_state_tv)
    TextView realNameStateTv;
    @BindView(R.id.setting_real_name_auth_rl)
    RelativeLayout settingRealNameAuthRl;
    @BindView(R.id.invite_code_tip_tv)
    TextView inviteCodeTipTv;
    @BindView(R.id.setting_write_invite_code_rl)
    RelativeLayout settingWriteInviteCodeRl;
    @BindView(R.id.cache_size_tv)
    TextView cacheSizeTv;
    @BindView(R.id.setting_clear_cache_rl)
    RelativeLayout settingClearCacheRl;
    @BindView(R.id.setting_feedback_rl)
    RelativeLayout settingFeedbackRl;
    @BindView(R.id.version_name_tv)
    TextView versionNameTv;
    @BindView(R.id.setting_version_update_rl)
    RelativeLayout settingVersionUpdateRl;
    @BindView(R.id.setting_about_us_rl)
    RelativeLayout settingAboutUsRl;
    @BindView(R.id.quit_tv)
    TextView quitTv;

    SettingContract.Presenter presenter;
    AuthInfo authInfo;

    boolean isEdit = true;
    private String cachSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        headTitleTv.setText("设置");

        presenter = new SettingPresenter(this);
        presenter.attachView(this);

        //获取实名认证信息
        presenter.getData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            cachSize = FileCacheUtils.getTotalCacheSize(SettingActivity.this);
            cacheSizeTv.setText(cachSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.setting_change_psw_rl, R.id.setting_change_phone_rl, R.id.setting_real_name_auth_rl,
            R.id.setting_write_invite_code_rl, R.id.setting_clear_cache_rl, R.id.setting_feedback_rl,
            R.id.setting_version_update_rl, R.id.setting_about_us_rl, R.id.head_back_ly, R.id.quit_tv})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.head_back_ly:
                //返回
                finish();
                break;
            case R.id.setting_change_psw_rl:
                //修改密码
                intent.setClass(this, ChangePswActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_change_phone_rl:
                //修改手机号
                intent.setClass(this, ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_real_name_auth_rl:
                //实名认证
                Bundle bundle = new Bundle();
                bundle.putSerializable("AuthInfo", authInfo);
                bundle.putBoolean("isEdit", isEdit);
                intent.putExtras(bundle);
                intent.setClass(this, RealNameAuthActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_write_invite_code_rl:
                //填写邀请码
                intent.setClass(this, InviteCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_clear_cache_rl:
                try {
                    FileCacheUtils.clearAllCache(SettingActivity.this);
                    cacheSizeTv.setText("0k");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ToastUtils.showShortToast("已清除缓存");
                break;
            case R.id.setting_feedback_rl:
                //意见反馈
                intent.setClass(this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_version_update_rl:
                //版本更新
                presenter.checkVersion();
                break;
            case R.id.setting_about_us_rl:
                //关于我们
                intent.putExtra("title", "关于我们");
                intent.putExtra("url", UrlUtils.aboutUs);
                intent.setClass(this, WebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.quit_tv:
                //安全退出
                showLoginOutDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 安全退出
     */
    private void quitSafely() {
        EMClient.getInstance().logout(true, new EMCallBack() {

            @Override
            public void onSuccess() {
                AccountUtils.clearUserConnect();
                AccountUtils.clearUserCache(getApplicationContext());
                MMKV.defaultMMKV().remove(Constants.USERREMARKINFO);
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                //删除别名
                JPushInterface.deleteAlias(SettingActivity.this, 1111);
                startActivity(intent);
                //finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                AccountUtils.clearUserConnect();
                AccountUtils.clearUserCache(getApplicationContext());
            }
        });

    }

    /***退出登录确认对话框**/
    private void showLoginOutDialog() {
        String text1 = "确定要安全退出登录吗?";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage(text1);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                quitSafely();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void liftData(AuthInfo info) {
        if (null != info) {
            //赋值
            authInfo = info;
            //0未提交，1通过,2提交审核,3审核驳回
            String type = info.getIs_audit();
            switch (type) {
                case "0":
                    realNameStateTv.setText("未认证");
                    break;
                case "2":
                    realNameStateTv.setText("审核中");
                    isEdit = false;
                    break;
                case "1":
                    realNameStateTv.setText("已认证");
                    isEdit = false;
                    break;
                case "3":
                    realNameStateTv.setText("未通过");
                    break;
                default:
                    realNameStateTv.setText("未认证");
                    break;
            }
        } else {
            realNameStateTv.setText("未认证");
            authInfo = new AuthInfo();
        }
    }

    @Override
    public void liftData(VersionInfo info) {
        if (null != info) {
            int code =AppUtils.getAppVersionCode(this);
            String scode = info.getANDROID_VERSION().getCode();
            if (EmptyUtils.isNotEmpty(scode)) {
                if (Integer.valueOf(scode) > code) {
                    //服务器版本高于本机版本，提示更新
                    showUpdateDialog(info.getANDROID_VERSION().getUrl());
                } else {
                    ToastUtils.showShortToast("已经是最新版本啦");
                }
            }
        } else {
            ToastUtils.showShortToast("未获取到版本更新信息");
        }
    }

    /**
     * 显示更新对话框
     */
    private void showUpdateDialog(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle("提示");
        builder.setMessage("发现新的软件版本，现在去更新吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                if (url.contains("http")) {
                    intent.setData(Uri.parse(url));
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast("软件下载地址无效");
                }
            }
        });
        builder.show();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Constants.USER_INFO state) {
        switch (state) {
            case REAL_NAME:
                //重新获取实名认证信息
                presenter.getData();
                break;
            default:
                break;
        }
    }


}
