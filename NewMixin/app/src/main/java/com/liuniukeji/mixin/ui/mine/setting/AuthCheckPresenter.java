package com.liuniukeji.mixin.ui.mine.setting;

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

import okhttp3.Call;
import okhttp3.Response;

/**
 * 实名认证检查
 */

public class AuthCheckPresenter implements AuthCheckContract.Presenter {
    AuthCheckContract.View mView;
    Context context;

    public AuthCheckPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getData() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));

        OkGoRequest.post(UrlUtils.authCheck, context,
                httpParams, new JsonCallback<LazyResponse<AuthInfo>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<AuthInfo> bean, Call call, Response response) {
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
    public void attachView(@NonNull AuthCheckContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
