package com.liuniukeji.mixin.ui.login;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class GradePresenter implements GradeContract.Presenter {
    GradeContract.View mView;
    Context context;

    public GradePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void lists() {
        HttpParams httpParams = new HttpParams();
        //httpParams.put("p", p);
        OkGoRequest.post(UrlUtils.getGradeList, context,
                httpParams, new JsonCallback<LazyResponse<List<GradeBean>>>() {
                    @Override
                    public void onSuccess(LazyResponse<List<GradeBean>> bean, Call call, Response response) {
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
    public void attachView(@NonNull GradeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
