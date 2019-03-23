package com.liuniukeji.mixin.ui.discover;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.liuniukeji.mixin.util.currency.EmptyUtils;
import com.liuniukeji.mixin.net.JsonCallback;
import com.liuniukeji.mixin.net.LazyResponse;
import com.liuniukeji.mixin.net.OkGoRequest;
import com.liuniukeji.mixin.net.UrlUtils;
import com.liuniukeji.mixin.util.AccountUtils;
import com.lzy.okgo.model.HttpParams;

import okhttp3.Call;
import okhttp3.Response;


public class GroupDetailPresenter implements GroupDetailContract.Presenter {
    GroupDetailContract.View mView;
    Context context;

    public GroupDetailPresenter(Context ctx) {
        this.context = ctx;
    }


    @Override
    public void getGroupInfo(String im_id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("im_id", im_id);
        OkGoRequest.post(UrlUtils.getGroupInfo, context,
                httpParams, new JsonCallback<LazyResponse<GroupInfo>>(context, false) {
                    @Override
                    public void onSuccess(LazyResponse<GroupInfo> bean, Call call, Response response) {
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
    public void joinInGroup(String group_id, String password, String note) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("group_id", group_id);

        if (EmptyUtils.isNotEmpty(password)) {
            httpParams.put("password", password);
        }
        if (EmptyUtils.isNotEmpty(note)) {
            httpParams.put("note", note);
        }

        OkGoRequest.post(UrlUtils.applyGroup, context,
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
    public void attachView(@NonNull GroupDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }


}
