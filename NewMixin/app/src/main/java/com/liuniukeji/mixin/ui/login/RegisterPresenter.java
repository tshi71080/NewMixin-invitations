package com.liuniukeji.mixin.ui.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;


import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.CommonResult;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class RegisterPresenter implements RegisterContract.Presenter {
    RegisterContract.View mView;
    Context context;

    public RegisterPresenter(Context context) {
        this.context = context;
    }

    @Override

    public void attachView(@NonNull RegisterContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void register(RegisterInfo registerInfo) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("username", registerInfo.getReal_name());
        //httpParams.put("phone", registerInfo.getPhone());
        //httpParams.put("code", registerInfo.getCode());
        httpParams.put("password", registerInfo.getPassword());
        //httpParams.put("invite_code", registerInfo.getInvite_code());
        httpParams.put("lng", registerInfo.getLng());
        httpParams.put("lat", registerInfo.getLat());
        httpParams.put("area_code", registerInfo.getArea_code());
        if(registerInfo.getPhoto_path()!=null){
            httpParams.put("photo_path", registerInfo.getPhoto_path());
        }

        OkGoRequest.post(UrlUtils.register, context, httpParams, new JsonCallback<LazyResponse<UserBean>>(context, false) {
            @Override
            public void onSuccess(LazyResponse<UserBean> userBeanLazyResponse, Call call, Response response) {
                super.onSuccess(userBeanLazyResponse, call, response);
                if (mView != null && userBeanLazyResponse.getStatus() == 1) {
                    UserBean userBean= userBeanLazyResponse.getData();
                    AccountUtils.saveUserCache(context, userBean);
                    AccountUtils.saveUserType(context, 1);
                    ToastUtils.showShortToast("用户登录成功");
                    Log.d("token", "token" + userBean.getToken());

                    mView.liftData(userBean);
                }
            }
        });
    }

    @Override
    public void getCode(String phone, int type,String area_code) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", phone);
        httpParams.put("type", type);
        httpParams.put("area_code", area_code);
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

    @Override
    public void getAllConnect() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("id", AccountUtils.getUser(context).getId());
        OkGoRequest.post(UrlUtils.getAllConstants, context, httpParams, new JsonCallback<LazyResponse<ConnectBean>>(context,false) {
            @Override
            public void onSuccess(LazyResponse<ConnectBean> lazyResponse, Call call, Response response) {
                super.onSuccess(lazyResponse, call, response);
                if(mView != null && lazyResponse.getStatus() == 1) {
                    mView.getConnectSuccess(lazyResponse.getData());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });

    }

    @Override
    public void getRemarkList() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.getRemarkList, context, httpParams, new JsonCallback<LazyResponse<List<MyRemarkFriendBean>>>(context,false) {
            @Override
            public void onSuccess(LazyResponse<List<MyRemarkFriendBean>> lazyResponse, Call call, Response response) {
                super.onSuccess(lazyResponse, call, response);
                if(mView != null && lazyResponse.getStatus() == 1) {
                    mView.getRemarklist(lazyResponse.getData());
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

}
