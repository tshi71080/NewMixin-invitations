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


public class PhonePresenter implements PhoneContract.Presenter {
    PhoneContract.View mView;
    Context context;

    public PhonePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void submit(String num, String code) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("phone", num);
        httpParams.put("code", code);
        OkGoRequest.post(UrlUtils.modifyPhone, context,
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
    public void attachView(@NonNull PhoneContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
