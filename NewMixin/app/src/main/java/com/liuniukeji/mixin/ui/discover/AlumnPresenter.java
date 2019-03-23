package com.liuniukeji.mixin.ui.discover;

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

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class AlumnPresenter implements AlumnContract.Presenter {
    AlumnContract.View mView;
    Context context;

    public AlumnPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getAlumnInfo() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.getAlumnInfo, context,
                httpParams, new JsonCallback<LazyResponse<AlumnInfo>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<AlumnInfo> bean, Call call, Response response) {
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
    public void locationMe() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.locateToMe, context,
                httpParams, new JsonCallback<LazyResponse<AlumnMe>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<AlumnMe> bean, Call call, Response response) {
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
    public void getMember(String school_class, String school_department_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("school_class", school_class);
        httpParams.put("school_department_id",school_department_id);

        OkGoRequest.post(UrlUtils.getAlumnMember, context,
                httpParams, new JsonCallback<LazyResponse<List<AlumnMember>>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<List<AlumnMember>> bean, Call call, Response response) {
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
    public void attachView(@NonNull AlumnContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
