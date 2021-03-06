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


public class GroupNearbyPresenter implements GroupNearbyContract.Presenter {
    GroupNearbyContract.View mView;
    Context context;

    public GroupNearbyPresenter(Context ctx) {
        this.context = ctx;
    }

    @Override
    public void getGroupNearbyList(int p, String lng, String lat) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("p", p);
        httpParams.put("lng", lng);
        httpParams.put("lat", lat);
        OkGoRequest.post(UrlUtils.getGroupNearbyList, context,
                httpParams, new JsonCallback<LazyResponse<List<GroupNearbyInfo>>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<List<GroupNearbyInfo>> bean, Call call, Response response) {
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
    public void attachView(@NonNull GroupNearbyContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
