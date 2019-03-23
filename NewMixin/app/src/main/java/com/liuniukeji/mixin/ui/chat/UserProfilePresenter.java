package com.liuniukeji.mixin.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.mine.UserInfo;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


public class UserProfilePresenter implements UserProfileContract.Presenter {
    UserProfileContract.View mView;
    Context context;

    public UserProfilePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getUserInfo(String im_name) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_name", im_name);
        OkGoRequest.post(UrlUtils.getUserInfo, context,
                httpParams, new JsonCallback<LazyResponse<UserInfo>>(context, false) {
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
    public void attachView(@NonNull UserProfileContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
