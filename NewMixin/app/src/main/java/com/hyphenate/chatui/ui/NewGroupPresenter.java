package com.hyphenate.chatui.ui;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;


public class NewGroupPresenter implements NewGroupContract.Presenter {
    NewGroupContract.View mView;
    Context context;

    public NewGroupPresenter(Context ctx) {
        this.context = ctx;
    }


    @Override
    public void createGroup(String im_id, String name, String description,
                            File logo, String membersonly, String is_pwd) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", im_id);
        httpParams.put("name", name);
        httpParams.put("description", description);

        httpParams.put("logo", logo);

        httpParams.put("membersonly", membersonly);
        httpParams.put("is_pwd", is_pwd);

        OkGoRequest.post(UrlUtils.createOrUpdateGroup, context,
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
    public void attachView(@NonNull NewGroupContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
