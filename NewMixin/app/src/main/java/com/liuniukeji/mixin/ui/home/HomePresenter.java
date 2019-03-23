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

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class HomePresenter implements HomeContract.Presenter {
    HomeContract.View mView;
    Context context;

    public HomePresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getSchoolList() {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        OkGoRequest.post(UrlUtils.getFollowSchoolTop, context,
                httpParams, new JsonCallback<LazyResponse<List<FollowSchoolInfo>>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<List<FollowSchoolInfo>> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftSchoolData(bean.getData());
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
    public void attachView(@NonNull HomeContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
