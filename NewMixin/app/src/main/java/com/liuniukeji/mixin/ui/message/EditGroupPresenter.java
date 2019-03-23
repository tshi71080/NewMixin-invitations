package com.liuniukeji.mixin.ui.message;

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

import okhttp3.Call;
import okhttp3.Response;


public class EditGroupPresenter implements EditGroupContract.Presenter {
    EditGroupContract.View mView;
    Context context;

    public EditGroupPresenter(Context ctx) {
        this.context = ctx;
    }


    @Override
    public void buildGroup(int type, String name, String color, String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("name", name);
        if (type == 1) {
            //新增
            httpParams.put("color", color);

        } else if (type == 2) {
            //修改
            httpParams.put("color", color);
            httpParams.put("id", id);
        } else {
            return;
        }
        OkGoRequest.post(UrlUtils.addOrEditFriendGroup, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftBuildGroupInfo(bean.getInfo());
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
    public void deleteGroup(String id) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("token", AccountUtils.getUserToken(context));
        httpParams.put("id", id);
        OkGoRequest.post(UrlUtils.deleteFriendGroup, context,
                httpParams, new JsonCallback<LazyResponse<Void>>(context, true) {
                    @Override
                    public void onSuccess(LazyResponse<Void> bean, Call call, Response response) {
                        super.onSuccess(bean, call, response);
                        if (mView != null) {
                            mView.liftBuildGroupInfo(bean.getInfo());
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
    public void attachView(@NonNull EditGroupContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {

    }

}
