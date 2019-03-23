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

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 实名认证
 */

public class AuthPresenter implements AuthContract.Presenter {
    AuthContract.View mView;
    Context context;

    public AuthPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void submit(AuthInfo authInfo, File file1, File file2) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("real_name", authInfo.getReal_name());
        httpParams.put("school_id", authInfo.getSchool_id());
        httpParams.put("school_department_id", authInfo.getSchool_department_id());
        httpParams.put("school_class", authInfo.getSchool_class());
        if (null != file1) {
            httpParams.put("photo_front", file1);
        }
        if (null != file2) {
            httpParams.put("photo_back", file2);
        }

        OkGoRequest.post(UrlUtils.submitAuthCheck, context,
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
    public void attachView(@NonNull AuthContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
