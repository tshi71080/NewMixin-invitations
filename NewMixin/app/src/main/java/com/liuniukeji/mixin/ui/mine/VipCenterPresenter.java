package com.liuniukeji.mixin.ui.mine;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class VipCenterPresenter implements VipCenterContract.Presenter {
    VipCenterContract.View mView;
    Context context;

    public VipCenterPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getVipMonth() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.getVipMonth, context,
                httpParams, new JsonCallback<LazyResponse<List<VipMonth>>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<List<VipMonth>> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftData(bean.getData());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                        }
                    }
                });
    }

    @Override
    public void buyVip(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("id", id);
        OkGoRequest.post(UrlUtils.buyVip, context,
                httpParams, new JsonCallback<LazyResponse<String>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<String> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftData(bean.getInfo());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                        }
                    }
                });
    }


    @Override
    public void getUserInfo() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.getUserInfo, context,
                httpParams, new JsonCallback<LazyResponse<UserInfo>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<UserInfo> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftData(bean.getData());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        if (mView != null) {
                            mView.onNetError();
                        }
                    }
                });
    }


    @Override
    public void attachView(@NonNull VipCenterContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
