package com.liuniukeji.mixin.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.db.DemoDBManager;
import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.XyqApplication;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.areacode.AreaCodeActivity;
import com.liuniukeji.mixin.ui.main.MainActivity;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.TimeUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okgo.model.HttpParams;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 注册
 */

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.register_top_tip_tv)
    TextView registerTopTipTv;
    @BindView(R.id.login_tip2_tv)
    TextView loginTip2Tv;
    @BindView(R.id.register_top_tip_ly)
    LinearLayout registerTopTipLy;
    @BindView(R.id.nickname_et)
    EditText nickNameEt;
    @BindView(R.id.gender_tv)
    TextView genderTv;
    @BindView(R.id.user_base_info_ly)
    LinearLayout userBaseInfoLy;
    @BindView(R.id.school_name_et)
    EditText schoolNameEt;
    @BindView(R.id.department_et)
    EditText departmentEt;
    @BindView(R.id.grade_et)
    EditText gradeEt;
    @BindView(R.id.school_info_ly)
    LinearLayout schoolInfoLy;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.cer_code_et)
    EditText cerCodeEt;
    @BindView(R.id.get_cer_code_tv)
    TextView getCerCodeTv;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.invite_code_et)
    EditText inviteCodeEt;
    @BindView(R.id.need_base_info_ly)
    LinearLayout needBaseInfoLy;
    @BindView(R.id.next_step_tv)
    TextView nextStepTv;
    @BindView(R.id.confirm_password_et)
    EditText confirmPasswordEt;
    @BindView(R.id.tv_area_code)
    TextView areaCodeView;

    /*SchoolBean schoolBean;
    DepartmentBean departmentBean;
    GradeBean gradeBean;*/
    long seconds = Constants.CODE_TIME;

    RegisterContract.Presenter presenter;
    @BindView(R.id.avatar_iv)
    ImageView avatarIv;

    File photo1;
    private int IMAGE_PICKER = 0x00002;
    String path;
    private String area_code = "86";//手机区号，默认
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        headTitleTv.setText("注册");

        schoolNameEt.setFocusable(false);
        departmentEt.setFocusable(false);
        gradeEt.setFocusable(false);

        presenter = new RegisterPresenter(this);
        presenter.attachView(this);

    }

    @OnClick({R.id.head_back_ly, R.id.next_step_tv, R.id.school_name_et,R.id.avatar_iv,
            R.id.department_et, R.id.grade_et, R.id.get_cer_code_tv, R.id.gender_tv,R.id.ll_phone_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.avatar_iv:
                //选择头像
                ImagePicker imagePicker2 = ImagePicker.getInstance();
                imagePicker2.setMultiMode(false);
                Intent intent12 = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent12, IMAGE_PICKER);
                break;
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.next_step_tv:
                //注册信息赋值
                RegisterInfo info = new RegisterInfo();
                String nickName = nickNameEt.getText().toString().trim();
                //String phone = phoneEt.getText().toString();
                //String code = cerCodeEt.getText().toString();
                String password = passwordEt.getText().toString().trim();
                //String confirmPassword = confirmPasswordEt.getText().toString();
                String inviteCode = inviteCodeEt.getText().toString();

                if(EmptyUtils.isEmpty(nickName)){
                    ToastUtils.showShortToast("请输入用户名");
                    return;
                }
                /*if (EmptyUtils.isEmpty(phone)) {
                    ToastUtils.showShortToast("请填写电话号码");
                    return;
                }

                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.showShortToast("请填写验证码");
                    return;
                }*/

                if (EmptyUtils.isEmpty(password)) {
                    ToastUtils.showShortToast("请填写密码");
                    return;
                }
                info.setReal_name(nickName);
                //info.setPhone(phone);
                //info.setCode(code);
                info.setPassword(password);
                info.setInvite_code(inviteCode);
                info.setArea_code(area_code);

                //用户头像
                info.setPhoto_path(photo1);

                //提交注册
                presenter.register(info);

                break;

            case R.id.gender_tv:
                //性别选择
                showGenderDialog();
                break;
            case R.id.school_name_et:
                //学校选择
                Intent intent1 = new Intent();
                intent1.setClass(this, SchoolActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.department_et:
                //院系选择
                /*if (null != schoolBean && EmptyUtils.isNotEmpty(schoolBean.getId())) {
                    Intent intent2 = new Intent();
                    intent2.setClass(this, DepartmentActivity.class);
                    intent2.putExtra("school_id", schoolBean.getId());
                    startActivityForResult(intent2, 2);
                } else {
                    ToastUtils.showShortToastSafe("请先正确选择学校");
                }*/
                break;
            case R.id.grade_et:
                //年级选择
                Intent intent3 = new Intent();
                intent3.setClass(this, GradeActivity.class);
                startActivityForResult(intent3, 3);
                break;
            case R.id.get_cer_code_tv:
                //获取验证码
                if (getCerCodeTv.getText().toString().contains("秒")) {
                    ToastUtils.showShortToastSafe("请不要频繁发送验证码");
                    return;
                }
                if (TextUtils.isEmpty(phoneEt.getText().toString())) {
                    ToastUtils.showShortToastSafe("请输入手机号");
                } else {
                    HttpParams httpParams = new HttpParams();
                    httpParams.put("phone", phoneEt.getText().toString());
                    httpParams.put("type", 1);
                    httpParams.put("area_code", area_code);
                    OkGoRequest.post(UrlUtils.smsCode, RegisterActivity.this, httpParams,
                            new JsonCallback<LazyResponse<Void>>(RegisterActivity.this, true) {

                                @Override
                                public void onSuccess(LazyResponse<Void> voidLazyResponse, Call call, Response response) {
                                    super.onSuccess(voidLazyResponse, call, response);
                                    ToastUtils.showShortToastSafe("验证码已发送");
                                    getCerCodeTv.post(runnable);
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                }
                            });
                }
                break;
            case R.id.ll_phone_code:
                Intent intent = new Intent(this,AreaCodeActivity.class);
                startActivityForResult(intent,Constants.GET_AREACODE_REQUESTCODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case 1:
                    //学校信息
                    schoolBean = (SchoolBean) data.getSerializableExtra("SchoolBean");
                    schoolNameEt.setText(schoolBean.getName());
                    //清空院系信息
                    departmentEt.setText(null);
                    break;
                case 2:
                    //院系信息
                    departmentBean = (DepartmentBean) data.getSerializableExtra("DepartmentBean");
                    departmentEt.setText(departmentBean.getName());
                    break;
                case 3:
                    //年级信息
                    gradeBean = (GradeBean) data.getSerializableExtra("GradeBean");
                    gradeEt.setText(gradeBean.getValue());
                    break;
                default:
                    break;
            }
        }*/

        if(requestCode==Constants.GET_AREACODE_REQUESTCODE&&resultCode==Constants.GET_AREACODE_RESULTCODE){
            if(data!=null){
                area_code = data.getStringExtra(Constants.ARECODE);
                areaCodeView.setText("+"+area_code);
            }
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> lists = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                path = lists.get(0).path;
                Glide.with(this).load(path).into(avatarIv);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
            photo1 = new File(path);

        }

    }



    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds--;
            if (getCerCodeTv == null) {
                return;
            }
            if (seconds == 0) {
                getCerCodeTv.removeCallbacks(runnable);
                getCerCodeTv.setText("获取验证码");
                seconds = Constants.CODE_TIME;
                return;
            }
            getCerCodeTv.setText(TimeUtils.getDaoJiShi(seconds));
            getCerCodeTv.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getCerCodeTv.removeCallbacks(runnable);
    }

    @Override
    public void phoneCodeState(boolean state) {
        //验证码校验返回结果
    }

    @Override
    public void liftData(UserBean userBean) {
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

        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(currentUsername);
        dialog = new Dialog(this);
        View progressView = LayoutInflater.from(this).inflate(R.layout.layout_progress,null);
        TextView title = progressView.findViewById(R.id.tv_title);
        title.setText("注册成功,登录中...");
        dialog.setContentView(progressView);
        dialog.show();
        EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

                        // update current user's display name for APNs
                        boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                                XyqApplication.currentUserNick.trim());
                        if (!updatenick) {
                            Log.e("LoginActivity", "update current user nick fail");
                        }

                        if (!RegisterActivity.this.isFinishing() && dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        // get user's info (this should be get from App's server or 3rd party service)
                        DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                        Intent intent = new Intent(RegisterActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        setResult(Constants.to_register_result_code);
                        Log.e("+++++++register","注册页面关闭");
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    /*** 性别选择对话框**/
    private void showGenderDialog() {
        final Dialog mDialog = new Dialog(this, R.style.my_dialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.select_gender_layout, null);

        RadioGroup rg = (RadioGroup) root.findViewById(R.id.gender_rg);
        final RadioButton rb1 = (RadioButton) root.findViewById(R.id.gender_rb1);
        final RadioButton rb2 = (RadioButton) root.findViewById(R.id.gender_rb2);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mDialog.dismiss();
                if (rb1.getId() == checkedId) {
                    genderTv.setText(rb1.getText());
                    genderTv.setTextColor(getResources().getColor(R.color.tx33));
                } else if (rb2.getId() == checkedId) {
                    genderTv.setText(rb2.getText());
                    genderTv.setTextColor(getResources().getColor(R.color.tx33));

                }
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        };
        rb1.setOnClickListener(onClickListener);
        rb2.setOnClickListener(onClickListener);
        //对话框通用属性设置
        setDialog(mDialog, root, 1);
    }


    /***弹窗通用设置**/
    private void setDialog(Dialog mDialog, ViewGroup root, int type) {
        mDialog.setContentView(root);
        Window dialogWindow = mDialog.getWindow();
        if (type == 1) {
            dialogWindow.setGravity(Gravity.CENTER);
        } else {
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
        // 添加动画
        //dialogWindow.setWindowAnimations(R.style.dialog_style);

        // 获取对话框当前的参数值
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        // 新位置X坐标
        lp.x = 0;
        // 新位置Y坐标
        lp.y = -20;
        // 宽度
        lp.width = (int) getResources().getDisplayMetrics().widthPixels;
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度

        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        // 透明度
        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }


}


