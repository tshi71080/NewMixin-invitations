package com.liuniukeji.mixin.ui.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;


public class GetPswPresenter implements GetPswContract.Presenter {
    GetPswContract.View mView;
    Context context;

    public GetPswPresenter(Context context) {
        this.context = context;
    }

    @Override

    public void attachView(@NonNull GetPswContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }


    @Override
    public void getBack(String phone, String code, String password,String area_code) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("phone", phone);
        httpParams.put("code", code);
        httpParams.put("password", password);
        httpParams.put("area_code", area_code);



        OkGoRequest.post(UrlUtils.forgetPassword, context, httpParams, new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.optInt("status") == 1) {
                        //ToastUtils.showShortToast(json.optString("info"));
                    } else {
                        //ToastUtils.showShortToast(json.optString("info"));
                    }
                    mView.liftData(json.optString("info"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtils.showShortToast("网络连接异常,请稍后重试");
            }
        });
    }


    @Override
    public void getCode(String phone, int type) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", phone);
        httpParams.put("type", type);
        OkGoRequest.post(UrlUtils.smsCode, context, httpParams, new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.optInt("status") == 1) {
                        ToastUtils.showShortToast(json.optString("info"));
                    } else {
                        ToastUtils.showShortToast(json.optString("info"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtils.showShortToast("网络连接异常,请稍后重试");
            }
        });
    }

    @Override
    public void checkCode(String user_phone, String sms_code) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", user_phone);
        httpParams.put("sms_code", sms_code);
        OkGoRequest.post(UrlUtils.checkSmsCode, context, httpParams, new com.lzy.okgo.callback.StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.optInt("status") == 1) {
                        mView.phoneCodeState(true);
                    } else {
                        ToastUtils.showShortToast(json.optString("info"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtils.showShortToast("网络连接异常,请稍后重试");
            }
        });
    }

}
