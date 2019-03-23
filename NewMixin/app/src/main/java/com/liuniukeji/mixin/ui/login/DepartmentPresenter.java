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


public class DepartmentPresenter implements DepartmentContract.Presenter {
    DepartmentContract.View mView;
    Context context;

    public DepartmentPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void lists(int p, String school_id, String keyword) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("p", p);
        httpParams.put("school_id", school_id);

        if (EmptyUtils.isNotEmpty(keyword)) {
            httpParams.put("keyword", keyword);
        }
        OkGoRequest.post(UrlUtils.getDepartmentList, context,
                httpParams, new JsonCallback<LazyResponse<List<DepartmentBean>>>() {
                    @Override
                    public void onSuccess(LazyResponse<List<DepartmentBean>> bean, Call call, Response response) {
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
    public void attachView(@NonNull DepartmentContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
