package com.liuniukeji.mixin.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuniukeji.mixin.R;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.areacode.AreaCodeActivity;
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.TimeUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.lzy.okgo.model.HttpParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 密码找回
 */

public class GetPswActivity extends AppCompatActivity implements GetPswContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.cer_code_et)
    EditText cerCodeEt;
    @BindView(R.id.get_cer_code_tv)
    TextView getCerCodeTv;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.confirm_psw_et)
    EditText confirmPswEt;
    @BindView(R.id.get_psw_tv)
    TextView getPswTv;
    @BindView(R.id.tv_area_code)
    TextView areaCodeView;
    private String area_code = "86";//手机区号，默认

    GetPswContract.Presenter presenter;
    long seconds = Constants.CODE_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_psw);
        ButterKnife.bind(this);
        headTitleTv.setText("密码找回");

        presenter = new GetPswPresenter(this);
        presenter.attachView(this);


    }


    @OnClick({R.id.head_back_ly, R.id.get_cer_code_tv, R.id.get_psw_tv,R.id.ll_phone_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.get_cer_code_tv:
                //获取验证码
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
                    httpParams.put("type", 2);
                    httpParams.put("area_code", area_code);

                    OkGoRequest.post(UrlUtils.smsCode, GetPswActivity.this, httpParams,
                            new JsonCallback<LazyResponse<Void>>(GetPswActivity.this, true) {

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
            case R.id.get_psw_tv:
                //密码找回数据提交
                String phone = phoneEt.getText().toString();
                String code = cerCodeEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confirm = confirmPswEt.getText().toString();

                if (EmptyUtils.isEmpty(phone)) {
                    ToastUtils.showShortToastSafe("手机号码不能为空");
                    return;
                }
                if (EmptyUtils.isEmpty(code)) {
                    ToastUtils.showShortToastSafe("验证码不能为空");
                    return;
                }
                if (EmptyUtils.isEmpty(password)) {
                    ToastUtils.showShortToastSafe("新密码不能为空");
                    return;
                }

                if (EmptyUtils.isEmpty(confirm)) {
                    ToastUtils.showShortToastSafe("请确认密码");
                    return;
                }

                if(!password.equals(confirm)){
                    ToastUtils.showShortToastSafe("密码前后确认不一致");
                    return;
                }

                presenter.getBack(phone, code, password,area_code);

                break;
                //获取区号
            case R.id.ll_phone_code:
                Intent intent = new Intent(this,AreaCodeActivity.class);
                startActivityForResult(intent,Constants.GET_AREACODE_REQUESTCODE);
                break;
            default:
                break;
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
    public void phoneCodeState(boolean state) {

    }

    @Override
    public void liftData(String state) {
        if (EmptyUtils.isNotEmpty(state)) {
            ToastUtils.showShortToast(state);
            finish();
        }
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
        getCerCodeTv.removeCallbacks(runnable);
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
    }
}
