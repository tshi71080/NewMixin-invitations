package com.liuniukeji.mixin.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.ui.login.SchoolBean;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class AddSchoolPresenter implements AddSchoolContract.Presenter {
    AddSchoolContract.View mView;
    Context context;

    public AddSchoolPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void lists(int p, String keyword) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("p", p);
        if (EmptyUtils.isNotEmpty(keyword)) {
            httpParams.put("keyword", keyword);
        }
        OkGoRequest.post(UrlUtils.getSchoolList, context,
                httpParams, new JsonCallback<LazyResponse<List<SchoolBean>>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<List<SchoolBean>> bean, Call call, Response response) {
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
    public void follow(String school_id, String type) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("school_id", school_id);
        httpParams.put("type", type);

        OkGoRequest.post(UrlUtils.addOrCancelFocus, context,
                httpParams, new JsonCallback<LazyResponse<Void>>() {
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
    public void attachView(@NonNull AddSchoolContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
