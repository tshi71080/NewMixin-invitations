package com.liuniukeji.mixin.ui.login;

import android.content.Context;
import android.support.annotation.NonNull;


import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class SchoolPresenter implements SchoolContract.Presenter {
    SchoolContract.View mView;
    Context context;

    public SchoolPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void lists(int p, String keyword) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("p", p);
        if(EmptyUtils.isNotEmpty(keyword)){
            httpParams.put("keyword", keyword);
        }
        OkGoRequest.post(UrlUtils.getSchoolList, context,
                httpParams, new JsonCallback<LazyResponse<List<SchoolBean>>>() {
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
    public void attachView(@NonNull SchoolContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
