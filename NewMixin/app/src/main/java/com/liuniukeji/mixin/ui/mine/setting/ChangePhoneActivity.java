package com.liuniukeji.mixin.ui.mine.setting;

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
import com.liuniukeji.mixin.util.Constants;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.util.currency.PhoneUtils;
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
 * 修改手机号
 */

public class ChangePhoneActivity extends AppCompatActivity implements PhoneContract.View {

    @BindView(R.id.head_back_ly)
    LinearLayout headBackLy;
    @BindView(R.id.head_title_tv)
    TextView headTitleTv;
    @BindView(R.id.change_phone_et)
    EditText changePhoneEt;
    @BindView(R.id.change_phone_save_tv)
    TextView changePhoneSaveTv;
    @BindView(R.id.cer_code_et)
    EditText cerCodeEt;
    @BindView(R.id.get_cer_code_tv)
    TextView getCerCodeTv;

    long seconds = Constants.CODE_TIME;
    PhoneContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        headTitleTv.setText("修改手机号");

        presenter = new PhonePresenter(this);
        presenter.attachView(this);


    }


    @OnClick({R.id.head_back_ly, R.id.change_phone_save_tv, R.id.get_cer_code_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back_ly:
                finish();
                break;
            case R.id.change_phone_save_tv:
                //保存数据
                String num = changePhoneEt.getText().toString();
                String code = cerCodeEt.getText().toString();

                if (EmptyUtils.isNotEmpty(num) && EmptyUtils.isNotEmpty(code)) {
                    if (PhoneUtils.isChinaPhoneLegal(num)) {
                        //保存数据
                        presenter.submit(num, code);
                    } else {
                        ToastUtils.showShortToastSafe("请输入正确电话号码");
                    }
                } else {
                    ToastUtils.showShortToastSafe("请输入完整电话号码和验证码");
                }
                break;
            case R.id.get_cer_code_tv:

                //获取验证码
                if (getCerCodeTv.getText().toString().contains("秒")) {
                    ToastUtils.showShortToastSafe("请不要频繁发送验证码");
                    return;
                }
                if (TextUtils.isEmpty(changePhoneEt.getText().toString())) {
                    ToastUtils.showShortToastSafe("请输入手机号");
                } else {
                    HttpParams httpParams = new HttpParams();
                    httpParams.put("phone", changePhoneEt.getText().toString());
                    httpParams.put("type", 3);
                    OkGoRequest.post(UrlUtils.smsCode, ChangePhoneActivity.this, httpParams,
                            new JsonCallback<LazyResponse<Void>>(ChangePhoneActivity.this, true) {

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
    public void liftData(String info) {
        ToastUtils.showShortToastSafe(info);
        finish();
    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onNetError() {
        ToastUtils.showShortToastSafe("修改失败");

    }


}
