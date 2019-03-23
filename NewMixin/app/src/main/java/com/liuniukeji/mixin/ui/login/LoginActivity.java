package com.liuniukeji.mixin.ui.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatui.Constant;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.db.DemoDBManager;
import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.XyqApplication;
import com.liuniukeji.mixin.ui.areacode.AreaCodeActivity;
import com.liuniukeji.mixin.ui.main.MainActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.BDLoacationHelper;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.WxShareAndLoginUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.login_tip2_tv)
    TextView loginTip2Tv;
    @BindView(R.id.login_phone_et)
    EditText loginPhoneEt;
    @BindView(R.id.login_psw_et)
    EditText loginPswEt;
    @BindView(R.id.login_forget_psw_tv)
    TextView loginForgetPswTv;
    @BindView(R.id.login_tv)
    TextView loginTv;
    @BindView(R.id.wx_login_iv)
    ImageView wxLoginIv;
    @BindView(R.id.qq_login_iv)
    ImageView qqLoginIv;
    @BindView(R.id.tv_area_code)
    TextView areaCodeView;

    LoginContract.Presenter presenter;

    private boolean progressShow;
    private boolean autoLogin = false;
    private static final String TAG = "LoginActivity";
    private String area_code = "86";//手机区号，默认



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new LoginPresenter(this);
        presenter.attachView(this);

        //判断存在登录用户信息
        if (AccountUtils.getUser(this) != null) {
            //环信账号登录状态判断
            if (DemoHelper.getInstance().isLoggedIn()) {
                autoLogin = true;
                startActivity(new Intent(this, MainActivity.class));
                return;
            }
        }
        //开始定位
        BDLoacationHelper.getInstance().start();
    }


    @OnClick({R.id.login_tip2_tv, R.id.login_forget_psw_tv, R.id.login_tv,
            R.id.wx_login_iv, R.id.qq_login_iv,R.id.ll_phone_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_tip2_tv:
                //去注册
                toRegister();
                break;
            case R.id.login_forget_psw_tv:
                //密码找回
                findPassword();
                break;
            case R.id.login_tv:
                //登录
                String phone = loginPhoneEt.getText().toString();
                String psw = loginPswEt.getText().toString();

                if (EmptyUtils.isEmpty(phone)) {
                    ToastUtils.showShortToast("请输入用户名");
                    break;
                }
                if (EmptyUtils.isEmpty(psw)) {
                    ToastUtils.showShortToast("请输入密码");
                    break;
                }
                //提交登录信息，APP后台账号体系登录
                presenter.login(phone, psw,area_code);
                break;
            case R.id.wx_login_iv:
                //微信登录
                WxShareAndLoginUtils.WxLogin(this);
                break;
            case R.id.qq_login_iv:
                //QQ登录
                break;
                //选择区号
            case R.id.ll_phone_code:
                Intent intent = new Intent(LoginActivity.this,AreaCodeActivity.class);
                startActivityForResult(intent,Constants.GET_AREACODE_REQUESTCODE);
                break;
            default:
                break;
        }
    }

    @Override
    public void toMain(UserBean userBean) {
        /**
         * 成功登录APP后台账号之后
         * 处理环信账号登录
         */

        if (null != userBean) {
            //环信IM账号登录
            imLogin(userBean);
            //获取所有联系人以及群组
            presenter.getAllConnect();
            //获取备注好友列表信息
            presenter.getRemarkList();
        }
    }

    @Override
    public void findPassword() {
        //找回密码
        //ToastUtils.showShortToast("密码找回");

        startActivity(new Intent().setClass(this,GetPswActivity.class));
    }

    @Override
    public void toRegister() {
        //去注册
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent,Constants.to_register_request_code);
        //finish();
    }

    @Override
    public void getConnectSuccess(ConnectBean connectBean) {
       AccountUtils.saveUserConnect(connectBean.getGroups());
       AccountUtils.saveUserConnectfriend(connectBean.getFriends());
    }

    @Override
    public void getRemarklist(List<MyRemarkFriendBean> remarkFriendBeanList) {
        //保存用户备注好友的信息
        try {
            String userResult = JSON.toJSONString(remarkFriendBeanList);
            MMKV.defaultMMKV().putString(Constants.USERREMARKINFO,userResult);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {

    }

    /**
     * 环信IM账号登录
     */
    private void imLogin(final UserBean userBean) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUsername = userBean.getIm_name();
        String currentPassword = userBean.getIm_password();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                //Log.d(TAG, "EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);


        final long start = System.currentTimeMillis();
        // call login method
        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "login: onSuccess");


                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        XyqApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }

                // get user's info (this should be get from App's server or 3rd party service)
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);

                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG, "login: onError: " + code);
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WeChatInfoBean weChatInfoBean){
        Log.e("++++++++++++++",weChatInfoBean.toString());
        presenter.loginByWx(weChatInfoBean.getOpenid(),weChatInfoBean.getHeadimgurl(),weChatInfoBean.getNickname(),
                MMKV.defaultMMKV().decodeString("lng"),MMKV.defaultMMKV().decodeString("lat"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.GET_AREACODE_REQUESTCODE&&resultCode==Constants.GET_AREACODE_RESULTCODE){
            if(data!=null){
                area_code = data.getStringExtra(Constants.ARECODE);
                areaCodeView.setText("+"+area_code);
            }
        }

        if(requestCode==Constants.to_register_request_code&&resultCode==Constants.to_register_result_code){
            Log.e("+++++++login","登录页面关闭");
            finish();
        }
    }
}
