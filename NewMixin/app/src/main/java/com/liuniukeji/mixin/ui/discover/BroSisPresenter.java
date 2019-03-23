package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.home.MomentInfo;
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


public class BroSisPresenter implements BroSisContract.Presenter {
    BroSisContract.View mView;
    Context context;

    public BroSisPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getMomentList(int p) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("p", p);
        OkGoRequest.post(UrlUtils.getQuestions, context,
                httpParams, new JsonCallback<LazyResponse<List<MomentInfo>>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<List<MomentInfo>> bean, Call call, Response response) {
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
    public void addOrCancelFocus(String member_id, String type) {

        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("member_id", member_id);
        httpParams.put("type", type);
        OkGoRequest.post(UrlUtils.addOrCancelUserFocus, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
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
    public void likeMoments(String moments_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("moments_id", moments_id);
        OkGoRequest.post(UrlUtils.likeMoments, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
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
    public void attachView(@NonNull BroSisContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
