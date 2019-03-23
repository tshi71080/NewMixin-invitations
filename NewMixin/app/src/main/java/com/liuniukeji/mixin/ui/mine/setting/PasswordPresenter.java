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


public class PasswordPresenter implements PasswordContract.Presenter {
    PasswordContract.View mView;
    Context context;

    public PasswordPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void submit(String old_password, String password, String re_password) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("old_password", old_password);
        httpParams.put("password", password);
        httpParams.put("re_password", re_password);

        OkGoRequest.post(UrlUtils.modifyPassword, context,
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
    public void attachView(@NonNull PasswordContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
