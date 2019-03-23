package com.liuniukeji.mixin.ui.mine.baseinfo;

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


public class BirthdayPresenter implements BirthdayContract.Presenter {
    BirthdayContract.View mView;
    Context context;

    public BirthdayPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void submit(String birthday,String isShow) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("birthday", birthday);
        httpParams.put("is_show_birthday", isShow);
        OkGoRequest.post(UrlUtils.birthday, context,
                httpParams, new JsonCallback<LazyResponse<CommonInfo>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<CommonInfo> bean, Call call, Response response) {
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
    public void attachView(@NonNull BirthdayContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
