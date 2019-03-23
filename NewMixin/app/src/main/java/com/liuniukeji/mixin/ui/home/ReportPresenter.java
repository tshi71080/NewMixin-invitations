package com.liuniukeji.mixin.ui.home;

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


public class ReportPresenter implements ReportContract.Presenter {
    ReportContract.View mView;
    Context context;

    public ReportPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void submit(String content,String to_member_id,File photo_path) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("content", content);
        httpParams.put("to_member_id", to_member_id);

        if(null!=photo_path){
            httpParams.put("photo_path", photo_path);
        }

        OkGoRequest.post(UrlUtils.report, context,
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
    public void attachView(@NonNull ReportContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
