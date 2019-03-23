package com.liuniukeji.mixin.ui.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hyphenate.easeui.bean.MyRemarkFriendBean;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.ToastUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class LoginPresenter implements LoginContract.Presenter {
    LoginContract.View mView;
    Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    @Override

    public void attachView(@NonNull LoginContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void login(String mobile, String pwd,String area_code) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("username", mobile);
        httpParams.put("password", pwd);
        //httpParams.put("area_code", area_code);
        OkGoRequest.post(UrlUtils.login, context, httpParams,
                new JsonCallback<LazyResponse<UserBean>>(context, true) {

            @Override
            public void onSuccess(LazyResponse<UserBean> lazyResponse, Call call, Response response) {
                super.onSuccess(lazyResponse, call, response);
                if (mView != null && lazyResponse.getStatus() == 1) {
                    UserBean userBean= lazyResponse.getData();
                    AccountUtils.saveUserCache(context, userBean);
                    AccountUtils.saveUserType(context, 1);
                    ToastUtils.showShortToast("用户登录成功");
                    Log.d("token", "token" + userBean.getToken());

                    mView.toMain(userBean);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    //微信登录
    @Override
    public void loginByWx(String openId, String userLogo, String nick_name, String lng, String lat) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("third_type", 1);//第三方登录类型
        httpParams.put("openid", openId);
        httpParams.put("nickname", nick_name);
        httpParams.put("real_name", nick_name);
        httpParams.put("lng", lng);
        httpParams.put("lat", lat);
        httpParams.put("photo_path", userLogo);

        OkGoRequest.post(UrlUtils.loginByWx, context, httpParams,
                new JsonCallback<LazyResponse<UserBean>>(context, true) {

                    @Override
                    public void onSuccess(LazyResponse<UserBean> lazyResponse, Call call, Response response) {
                        super.onSuccess(lazyResponse, call, response);
                        if (mView != null && lazyResponse.getStatus() == 1) {
                            UserBean userBean= lazyResponse.getData();
                            AccountUtils.saveUserCache(context, userBean);
                            AccountUtils.saveUserType(context, 1);
                            ToastUtils.showShortToast("用户登录成功");
                            Log.d("token", "token" + userBean.getToken());
                            mView.toMain(userBean);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
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
